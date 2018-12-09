package com.ensimag.bank;

import java.rmi.RemoteException;

/**
 * Created by cadicn on 12/4/18.
 */
public class IDManager {
    public static long bankId = 1;
    public static long messageId = 1;

    public IDManager() throws RemoteException {
        super();
    }

    public static long nextBankId() {
        long nextBankId = bankId;
        bankId++;
        return nextBankId;
    }

    public static long nextMessageId() {
        long nextMessageId = messageId;
        messageId++;
        return nextMessageId;
    }
}
