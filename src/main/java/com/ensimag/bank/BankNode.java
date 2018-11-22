package com.ensimag.bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import com.ensimag.api.bank.*;
import com.ensimag.api.message.IAck;
import com.ensimag.api.message.IResult;
import com.ensimag.api.node.INode;

import javax.security.auth.login.AccountNotFoundException;
import javax.xml.soap.Node;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BankNode extends UnicastRemoteObject implements IBankNode {
    private long id;
    private IBank bank;
    private LinkedList<INode<IBankMessage>> neighboors;
    private ArrayList<Long> waitNeighboors;

    public BankNode(long id, IBank bank) throws RemoteException{
        this.id = id;
        this.bank = bank;
        this.neighboors = new LinkedList<INode<IBankMessage>>();
    }

    public long getId() {
        return id;
    }

    public void onMessage(IBankMessage iBankMessage) throws RemoteException {
        if(iBankMessage.getDestinationBankId() == id)
        {
            try{
                iBankMessage.getAction().execute(this);
            }
            catch(Exception e){
                System.out.println(e);
            }
        }else{
                for(INode element:neighboors) {
                    element.onMessage(iBankMessage);
                    waitNeighboors.add(element.getId());
                }
            }
    }

    public void onAck(IAck iAck) throws RemoteException {
        try{
            waitNeighboors.remove(iAck.getAckSenderId());
        } catch(Exception e ){
            throw new RemoteException("Ack incorrect.");
        }
    }

    public void addNeighboor(INode<IBankMessage> var1) throws RemoteException
    {
        this.neighboors.add(var1);
    }

    public void removeNeighboor(INode<IBankMessage> iNode) throws RemoteException {
        neighboors.remove(iNode);
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
