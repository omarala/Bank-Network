package com.ensimag.bank;

import com.ensimag.api.bank.IAccount;
import com.ensimag.api.bank.IBankAction;
import com.ensimag.api.bank.IBankNode;
import com.ensimag.api.bank.IUser;

import javax.security.auth.login.AccountNotFoundException;
import java.io.Serializable;
import java.rmi.RemoteException;

public class CloseAccountAction implements IBankAction {
    IUser user;

    public CloseAccountAction(IUser user) {
        this.user = user;
    }

    public Serializable execute(IBankNode node) {
        //return null;//node.closeAccount(user);
        try {
            for(IAccount account : node.getAccounts()){
                if(account.getAccountUser().equals(user)){
                    return node.closeAccount(account.getAccountNumber());
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AccountNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
