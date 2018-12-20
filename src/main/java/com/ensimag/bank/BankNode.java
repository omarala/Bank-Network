package com.ensimag.bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import com.ensimag.api.bank.*;
import com.ensimag.api.message.EnumMessageType;
import com.ensimag.api.message.IAck;
import com.ensimag.api.message.IMessage;
import com.ensimag.api.message.IResult;
import com.ensimag.api.node.INode;
import com.ensimag.message.Ack;
import com.ensimag.message.Result;
import com.ensimag.bank.IDManager;

import javax.security.auth.login.AccountNotFoundException;
import javax.xml.soap.Node;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BankNode extends UnicastRemoteObject implements IBankNode {
    private static final long serialVersionUID = 6646971932452402756L;
    private IBank bank;
    private LinkedList<INode<IBankMessage>> neighboors;
    private HashMap<Long, LinkedList<INode<IBankMessage>>> messageIdToWaitingNeighboor;
    private HashMap<Long, Long> firstSenderToMessageId;
    private HashMap<Long, IBankMessage> receivedMessages;
    private HashMap<Long,List<IResult <? extends Serializable>>> answersByMessageId;
    private LinkedList<Long> transmissionComplete;

    //same idManager shared between all bankNodes with rmi
    private IIDManager idManager;


    public BankNode(IBank bank, IIDManager idManager) throws RemoteException {

        this.bank = bank;
        this.neighboors = new LinkedList<INode<IBankMessage>>();
        this.messageIdToWaitingNeighboor = new HashMap<Long, LinkedList<INode<IBankMessage>>>();
        this.firstSenderToMessageId = new HashMap<Long, Long>();
        this.receivedMessages = new HashMap<Long, IBankMessage>();
        this.answersByMessageId = new HashMap<Long,List<IResult<? extends Serializable>>>();
        this.transmissionComplete = new LinkedList<Long>();
        this.idManager = idManager;

    }

    public long getId() {
        return bank.getBankId();
    }

    public void onMessage(IBankMessage bankMessage) throws RemoteException {
        System.out.println("Bank n° " + bank.getBankId() + " received message n° " + bankMessage.getMessageId() + " from Bank n° :" + bankMessage.getSenderId());
        INode<IBankMessage> senderNode = findNode(bankMessage.getSenderId());
        if (receivedMessages.containsKey(bankMessage.getMessageId())) {
            System.out.println("Message seen, ack sent");
            sendAck(bankMessage);
        } else {
            System.out.println("Message n° " + bankMessage.getMessageId() + " is seen for the first time");
            receivedMessages.put(bankMessage.getMessageId(), bankMessage);
            // save who sent the message for the first time
            firstSenderToMessageId.put(senderNode.getId(), bankMessage.getMessageId());

            //switching on message type
            switch (bankMessage.getMessageType()) {
            case SINGLE_DEST:
                // execute the action if we're the destination
                if (bankMessage.getDestinationBankId() == bank.getBankId()) {
                    try {
                        System.out.println("I am the destination");
                        // automatically send ack to the sender
                        sendAck(bankMessage);
                        // execute the action
                        Serializable resSerializable = bankMessage.getAction().execute(this);
                        // create result to send back
                        Result result = new Result(bankMessage.getMessageId(), resSerializable);
                        // create a delivery message
                        BankMessage returnResMessage = new BankMessage(bank.getBankId(), idManager.nextMessageId(),
                                bank.getBankId(), bankMessage.getOriginalBankSenderId(), new DeliveryAction(result),
                                EnumMessageType.DELIVERY);
                        // send back result message
                        System.out.println("I am sending back result " + returnResMessage);
                        senderNode.onMessage(returnResMessage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // transmit the message to our neighboors
                } else {
                    // creating waiting neighboor list
                    LinkedList<INode<IBankMessage>> neighboorList = deepCopyNeighboors();
                    // removing the sender
                    neighboorList.remove(senderNode);

                    boolean neighboorsAvailable = neighboorList.size()>0?true:false;

                    if(neighboorsAvailable){
                        for (INode<IBankMessage> neighboor : neighboorList) {
                            IBankMessage newMessage = new BankMessage(bank.getBankId(), bankMessage.getMessageId(),
                                    bankMessage.getOriginalBankSenderId(), bankMessage.getDestinationBankId(),
                                    bankMessage.getAction(), EnumMessageType.SINGLE_DEST);
                            neighboor.onMessage(newMessage);
                        }

                    }

                    else{
                        System.out.println("je devrais etre la");
                        sendAck(bankMessage);
                    }
                }
                break;
            case BROADCAST:
                // I'll execute the message anyway then transmit the message
                try {
                    // Executing the action
                    Serializable resSerializable = bankMessage.getAction().execute(this);
                    // Creating result from data
                    Result result = new Result(bankMessage.getMessageId(), resSerializable);
                    // Sending back result with Delivery type
                    BankMessage returnResMessage = new BankMessage(bank.getBankId(), idManager.nextMessageId(),
                            bank.getBankId(), bankMessage.getOriginalBankSenderId(), new DeliveryAction(result),
                            EnumMessageType.DELIVERY);
                    // send back result message
                    senderNode.onMessage(returnResMessage);

                    // Now we will transmit the original message to neighboors

                    // We create message neighboorList
                    LinkedList<INode<IBankMessage>> neighboorList = deepCopyNeighboors();
                    neighboorList.remove(senderNode);
                    messageIdToWaitingNeighboor.put(bankMessage.getMessageId(), neighboorList);

                    boolean neighboorsAvailable = neighboorList.size()>0?true:false;

                    if(neighboorsAvailable){
                        for (INode<IBankMessage> neighboor : neighboorList) {
                            IBankMessage newMessage = new BankMessage(bank.getBankId(), bankMessage.getMessageId(),
                                    bankMessage.getOriginalBankSenderId(), bankMessage.getDestinationBankId(),
                                    bankMessage.getAction(), EnumMessageType.BROADCAST);
                            neighboor.onMessage(newMessage);
                        }

                    }
                    else{
                        sendAck(bankMessage);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case DELIVERY:
                try{
                    //We are the destination for the result message
                    System.out.println("I am bank n° " + bank.getBankId() + " the bankMessage destination is : " + bankMessage.getDestinationBankId());

                    if (bankMessage.getDestinationBankId() == bank.getBankId()){
                        // We execute the message to take the data
                        IResult result = (IResult)bankMessage.getAction().execute(this);
                            System.out.println("Result received for message n° " + bankMessage.getMessageId());
                            System.out.println("Transmission succeeded for Message n° " + bankMessage.getMessageId());
                            System.out.println("Result delivered is : " + result.getData());
                            transmissionComplete.add(result.getMessageId());
                            transmissionComplete.add(bankMessage.getMessageId());


                        if(!answersByMessageId.containsKey(result.getMessageId())){
                            answersByMessageId.put(result.getMessageId(), new LinkedList<IResult<? extends Serializable>>());
                        }



                    }else{
                        //We transmit the message like before to our neighbours
                        LinkedList<INode<IBankMessage>> neighboorList = deepCopyNeighboors();
                        neighboorList.remove(senderNode);
                        messageIdToWaitingNeighboor.put(bankMessage.getMessageId(), neighboorList);
                        boolean neighboorsAvailable = neighboorList.size()>0?true:false;

                        if(neighboorsAvailable){
                            for (INode<IBankMessage> neighboor : neighboorList) {
                                IBankMessage newMessage = new BankMessage(bank.getBankId(), bankMessage.getMessageId(),
                                        bankMessage.getOriginalBankSenderId(), bankMessage.getDestinationBankId(),
                                        bankMessage.getAction(), EnumMessageType.DELIVERY);
                                neighboor.onMessage(newMessage);
                            }
                        }
                        else {
                            sendAck(bankMessage);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            }
        }
    }

    public void onAck(IAck iAck) throws RemoteException {
        System.out
                .println("Bank n° " + bank.getBankId() + " received ack for the message n° " + iAck.getAckMessageId() + " from Bank n° " + iAck.getAckSenderId());
        if (!messageIdToWaitingNeighboor.containsKey(iAck.getAckMessageId())) {
            System.out.println("Unexpected ack received");
        } else {
            INode<IBankMessage> senderAckNode = findNode(iAck.getAckSenderId());
            if(!messageIdToWaitingNeighboor.get(iAck.getAckMessageId()).contains(senderAckNode)){
                System.out.println("Unexpected ack received from Bank n° " + iAck.getAckSenderId());
            }
            else{
                messageIdToWaitingNeighboor.get(iAck.getAckMessageId()).remove(senderAckNode);
                // if last ack is received
                if (messageIdToWaitingNeighboor.get(iAck.getAckMessageId()).isEmpty()) {

                    IBankMessage originalBankMessage = receivedMessages.get(iAck.getAckMessageId());
                    if(bank.getBankId() == originalBankMessage.getOriginalBankSenderId()){
                        System.out.println("Transmission succeeded with confirmation for message n° " + iAck.getAckMessageId());
                        transmissionComplete.add(iAck.getAckMessageId());
                    }
                    else{
                        sendAck(originalBankMessage);
                    }
                }
                messageIdToWaitingNeighboor.remove(iAck.getAckMessageId());


            }




        }
    }

    public void sendAck(IBankMessage bankMessage) throws RemoteException {
        INode<IBankMessage> senderNode = findNode(bankMessage.getSenderId());
        IAck newAck = new Ack(bank.getBankId(), bankMessage.getMessageId());
        System.out.println("Sending back ack to : BankNode" + senderNode.getId());
        senderNode.onAck(newAck);
    }

    public void addNeighboor(INode<IBankMessage> var1) throws RemoteException {
        try {
            if(var1 != null){
                this.neighboors.add(var1);
            }else{
                System.out.println(this);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeNeighboor(INode<IBankMessage> iNode) throws RemoteException {
        try {
            neighboors.remove(iNode);
        } catch (Exception e) {
            throw new RemoteException("Impossible to remove the neighbour");
        }
    }

    public INode<IBankMessage> findNode(long id) throws RemoteException {
        if(id == bank.getBankId()){
            return this;
        }
        for (INode<IBankMessage> element : neighboors) {
            if (element.getId() == id) {
                return element;
            }
        }
        return null;
    }

    public List<IResult<? extends Serializable>> getResultForMessage(long l) throws RemoteException {
        if(transmissionComplete.contains(l)){
            if(answersByMessageId.containsKey(l)){
                return answersByMessageId.get(l);
            }else{
                return new LinkedList<IResult<? extends Serializable>>();
            }
        }
        return null;
    }

    public Boolean deliverResult(IResult<Serializable> iResult) throws RemoteException {
        return null;
    }

    public List<IAccount> getAccounts() throws RemoteException {
        return bank.getAccounts();
    }

    public IAccount getAccount(long l) throws AccountNotFoundException, RemoteException {
        return bank.getAccount(l);
    }

    public IAccount openAccount(IUser iUser) throws RemoteException {
        return bank.openAccount(iUser);
    }

    public boolean closeAccount(long l) throws AccountNotFoundException, RemoteException {
        return bank.closeAccount(l);
    }

    public LinkedList<INode<IBankMessage>> deepCopyNeighboors() {
        LinkedList<INode<IBankMessage>> newList = new LinkedList<INode<IBankMessage>>();
        for (INode<IBankMessage> node : neighboors) {
            newList.add(node);

        }
        return newList;
    }

    public LinkedList<INode<IBankMessage>> getNeighboors() {
        return neighboors;
    }

    @Override
    public String toString(){
        String res = ""+getId() + " -> ";
        for(INode bankNode:neighboors){
            try{
                res+= bankNode.getId() +", ";

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return res;
    }
}
