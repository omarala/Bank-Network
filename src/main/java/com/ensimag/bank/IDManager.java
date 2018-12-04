package com.ensimag.bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by cadicn on 12/4/18.
 */
public class IDManager extends UnicastRemoteObject implements IIDManager {
    public static long bankId = 1;
    public static long messageId = 1;

    public IDManager() throws RemoteException {
        super();
    }

    public long nextBankId() throws RemoteException {
        long nextBankId = bankId;
        bankId++;
        return nextBankId;
    }

    public long nextMessageId() throws RemoteException {
        long nextMessageId = messageId;
        messageId++;
        return nextMessageId;
    }
}
