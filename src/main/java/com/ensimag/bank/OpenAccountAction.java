package com.ensimag.bank;

import com.ensimag.api.bank.IAccount;
import com.ensimag.api.bank.IBankAction;
import com.ensimag.api.bank.IBankNode;
import com.ensimag.api.bank.IUser;

import java.io.Serializable;

public class OpenAccountAction implements IBankAction {

    private static final long serialVersionUID = -7897164078433528075L;
    private IUser user;

    public OpenAccountAction(IUser user) {
        this.user = user;
    }

    public Serializable execute(IBankNode node) throws Exception {
        try{
            IAccount account =  node.openAccount(user);
            return account;
        }
        catch(Exception e){
            throw e;
        }

    }
}
