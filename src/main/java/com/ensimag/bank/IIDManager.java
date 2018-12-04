package com.ensimag.bank;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by cadicn on 12/4/18.
 * Classe pour partager les ID à travers le réseau
 */
public interface IIDManager extends Remote, Serializable{
    long nextBankId() throws RemoteException;
    long nextMessageId() throws RemoteException;
}
