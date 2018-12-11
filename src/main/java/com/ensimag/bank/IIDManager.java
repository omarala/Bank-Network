package com.ensimag.bank;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public interface IIDManager extends Remote, Serializable {

    long nextBankId() throws RemoteException;
    long nextAccountId() throws RemoteException;

    long nextMessageId() throws RemoteException;

    long getNextBankId() throws RemoteException;
}
