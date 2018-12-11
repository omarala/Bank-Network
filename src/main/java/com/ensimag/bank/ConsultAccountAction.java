package com.ensimag.bank;

import com.ensimag.api.bank.IAccount;
import com.ensimag.api.bank.IBankAction;
import com.ensimag.api.bank.IBankNode;

import java.io.Serializable;
import java.rmi.RemoteException;

public class ConsultAccountAction implements IBankAction {
    private long numberAccount;

    public ConsultAccountAction(long numberAccount) {
        this.numberAccount = numberAccount;
    }

    public Serializable execute(IBankNode iBankNode) throws RemoteException, Exception {
        try{
            IAccount account = iBankNode.getAccount(numberAccount);
            return account;
        }
        catch (RemoteException e ){
            throw e;
        } catch(Exception e){
            throw e;
        }
    }
}
