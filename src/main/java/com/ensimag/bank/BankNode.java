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
    private HashMap<INode<IBankMessage>, Long> firstSenderToMessageId;
    private HashMap<Long, IBankMessage> receivedMessages;

    public BankNode() throws RemoteException {
        this.bank = new Bank();
        this.neighboors = new LinkedList<INode<IBankMessage>>();
    }

    public BankNode(IBank bank) throws RemoteException {

        this.bank = bank;
        this.neighboors = new LinkedList<INode<IBankMessage>>();
        this.messageIdToWaitingNeighboor = new HashMap<Long, LinkedList<INode<IBankMessage>>>();
        this.firstSenderToMessageId = new HashMap<INode<IBankMessage>, Long>();
        this.receivedMessages = new HashMap<Long, IBankMessage>();
    }

    public long getId() {
        return bank.getBankId();
    }

    public void onMessage(IBankMessage bankMessage) throws RemoteException {
        System.out.print("Bank n° " + bank.getBankId() + " received message n° " + bankMessage.getMessageId());
        INode<IBankMessage> senderNode = findNode(bankMessage.getSenderId());
        if (receivedMessages.containsKey(bankMessage.getMessageId())) {
            System.out.println("Message seen, ack sent");
            sendAck(bankMessage);
        } else {
            receivedMessages.put(bankMessage.getMessageId(), bankMessage);
            firstSenderToMessageId.put(senderNode, bankMessage.getMessageId());
            switch (bankMessage.getMessageType()) {
            case SINGLE_DEST:
                // execute the action if we're the destination
                if (bankMessage.getDestinationBankId() == bank.getBankId()) {
                    try {
                        sendAck(bankMessage);
                        Serializable resSerializable = bankMessage.getAction().execute(this);
                        Result result = new Result(bankMessage.getMessageId(), resSerializable);
                        BankMessage returnResMessage = new BankMessage(bank.getBankId(), IDManager.nextBankId(),
                                bank.getBankId(), bankMessage.getOriginalBankSenderId(), new DeliveryAction(result),
                                EnumMessageType.DELIVERY);
                        // send back result message
                        senderNode.onMessage(returnResMessage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // transmit the message to our neighboors
                } else {
                    LinkedList<INode<IBankMessage>> neighboorList = deepCopyNeighboors();
                    neighboorList.remove(senderNode);
                    messageIdToWaitingNeighboor.put(bankMessage.getMessageId(), neighboorList);
                    for (INode<IBankMessage> neighboor : neighboorList) {
                        IBankMessage newMessage = new BankMessage(bank.getBankId(), bankMessage.getMessageId(),
                                bankMessage.getOriginalBankSenderId(), bankMessage.getDestinationBankId(),
                                bankMessage.getAction(), EnumMessageType.SINGLE_DEST);
                        neighboor.onMessage(newMessage);
                    }

                    if (neighboorList.size() == 0) {
                        sendAck(bankMessage);
                    }
                }
            case BROADCAST:
                // I'll execute the message anyway then transmit the message
                try {
                    Serializable resSerializable = bankMessage.getAction().execute(this);
                    Result result = new Result(bankMessage.getMessageId(), resSerializable);
                    BankMessage returnResMessage = new BankMessage(bank.getBankId(), IDManager.nextBankId(),
                            bank.getBankId(), bankMessage.getOriginalBankSenderId(), new DeliveryAction(result),
                            EnumMessageType.DELIVERY);
                    // send back result message
                    senderNode.onMessage(returnResMessage);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                LinkedList<INode<IBankMessage>> neighboorList = deepCopyNeighboors();
                neighboorList.remove(senderNode);
                messageIdToWaitingNeighboor.put(bankMessage.getMessageId(), neighboorList);
                for (INode<IBankMessage> neighboor : neighboorList) {
                    IBankMessage newMessage = new BankMessage(bank.getBankId(), bankMessage.getMessageId(),
                            bankMessage.getOriginalBankSenderId(), bankMessage.getDestinationBankId(),
                            bankMessage.getAction(), EnumMessageType.BROADCAST);
                    neighboor.onMessage(newMessage);
                }

                if (neighboorList.size() == 0) {
                    sendAck(bankMessage);
                }

            case DELIVERY:
            }
        }
    }

    public void onAck(IAck iAck) throws RemoteException {
        System.out
                .println("Bank n° " + bank.getBankId() + " received ack for the message n° " + iAck.getAckMessageId());
        if (!messageIdToWaitingNeighboor.containsKey(iAck.getAckMessageId())) {
            System.out.println("Unexpected ack received");
        } else {
            INode<IBankMessage> senderAckNode = findNode(iAck.getAckSenderId());
            messageIdToWaitingNeighboor.get(iAck.getAckMessageId()).remove(senderAckNode);
            // if last ack is received
            if (messageIdToWaitingNeighboor.get(iAck.getAckMessageId()).isEmpty()) {
                IBankMessage originalBankMessage = receivedMessages.get(iAck.getAckMessageId());
                sendAck(originalBankMessage);
            }
            messageIdToWaitingNeighboor.remove(iAck.getAckMessageId());
        }
    }

    public void sendAck(IBankMessage bankMessage) throws RemoteException {
        INode<IBankMessage> senderNode = findNode(bankMessage.getSenderId());
        IAck newAck = new Ack(bank.getBankId(), bankMessage.getMessageId());
        senderNode.onAck(newAck);
    }

    public void addNeighboor(INode<IBankMessage> var1) throws RemoteException {
        try {
            this.neighboors.add(var1);
        } catch (Exception e) {
            throw new RemoteException("Impossible to add the neighbour");
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
        for (INode<IBankMessage> element : neighboors) {
            if (element.getId() == id) {
                return element;
            }
        }
        return null;
    }

    public List<IResult<? extends Serializable>> getResultForMessage(long l) throws RemoteException {
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
}
