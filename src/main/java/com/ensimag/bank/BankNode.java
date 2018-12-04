package com.ensimag.bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import com.ensimag.api.bank.*;
import com.ensimag.api.message.IAck;
import com.ensimag.api.message.IResult;
import com.ensimag.api.node.INode;
import com.ensimag.message.Ack;

import javax.security.auth.login.AccountNotFoundException;
import javax.xml.soap.Node;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BankNode extends UnicastRemoteObject implements IBankNode {
    private IBank bank;
    private LinkedList<INode<IBankMessage>> neighboors;
    private ArrayList<Long> waitNeighboors;
    private HashMap<INode, Long> firstSender;
    private ArrayList<Long> ReceivedMessageId;


    public BankNode() throws RemoteException{
        this.bank = new Bank();
        this.neighboors = new LinkedList<INode<IBankMessage>>();
    }
    public BankNode(IBank bank) throws RemoteException{

        this.bank = bank;
        this.neighboors = new LinkedList<INode<IBankMessage>>();
    }

    public long getId() {
        return bank.getBankId();
    }

    public void onMessage(IBankMessage iBankMessage) throws RemoteException {
        if(ReceivedMessageId.contains(iBankMessage.getMessageId())) {
            for (INode<IBankMessage> neighbour : neighboors) {
                if (neighbour.getId() == iBankMessage.getSenderId()) {
                    neighbour.onAck(new Ack(this.bank.getBankId(), iBankMessage.getMessageId()));
                    return;
                }
            }
        }else{

            if(iBankMessage.getDestinationBankId() == bank.getBankId())
            {
                try{
                    iBankMessage.getAction().execute(this);
                }
                catch(Exception e){
                    System.out.println(e);
                }
            }else{
                for(INode element:neighboors) {
                    if (element.getId() != iBankMessage.getSenderId()) {
                        element.onMessage(iBankMessage);
                        waitNeighboors.add(element.getId());
                    }else{
                        firstSender = element;
                    }
                }
            }
    }
    }

    public void onAck(IAck iAck) throws RemoteException {
        try{
            waitNeighboors.remove(iAck.getAckSenderId());
            //transmet ack pour le firstSender
            if(waitNeighboors.size() == 0 && firstSender != null){
                firstSender.onAck(iAck);
            }
        } catch(Exception e ){
            throw new RemoteException("Ack incorrect.");
        }
    }

    public void addNeighboor(INode<IBankMessage> var1) throws RemoteException
    {
        try{
            this.neighboors.add(var1);
        } catch (Exception e){
            throw new RemoteException("Impossible to add the neighbour");
        }
    }

    public void removeNeighboor(INode<IBankMessage> iNode) throws RemoteException {
        try{
            neighboors.remove(iNode);
        } catch(Exception e){
            throw new RemoteException("Impossible to remove the neighbour");
        }
    }

    public INode findNode(long id) throws RemoteException{
        for(INode element:neighboors){
            if (element.getId() == id){
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
}
