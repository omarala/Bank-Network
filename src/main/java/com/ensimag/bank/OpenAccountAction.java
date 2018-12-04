package com.ensimag.bank;

import com.ensimag.api.bank.IBankAction;
import com.ensimag.api.bank.IBankNode;
import com.ensimag.api.bank.IUser;

import java.io.Serializable;

public class OpenAccountAction implements IBankAction {
    private IUser user;
    public OpenAccountAction(IUser user) {
        this.user = user;
    }

    public Serializable execute(IBankNode node) throws Exception{
        return node.openAccount(user);
    }
}
