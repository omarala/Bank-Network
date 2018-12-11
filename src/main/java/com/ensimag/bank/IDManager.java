package com.ensimag.bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by cadicn on 12/4/18.
 */
public class IDManager extends UnicastRemoteObject implements IIDManager{


    private long bankId = 1;
    private long messageId = 1;
    private long accountId = 1;

    public IDManager() throws RemoteException {
        super();
    }

    public long nextBankId() throws RemoteException{
        //long nextBankId = bankId;
        //bankId++;
        System.out.println("bankId : "+bankId);
        return bankId++;
    }

    public long nextAccountId() throws RemoteException {
        long nextAccountId = accountId;
        accountId++;
        return nextAccountId;
    }

    public long nextMessageId() throws RemoteException{
        long nextMessageId = messageId;
        messageId++;
        return nextMessageId;
    }
}
