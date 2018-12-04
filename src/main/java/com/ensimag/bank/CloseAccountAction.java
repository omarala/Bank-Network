package com.ensimag.bank;

import com.ensimag.api.bank.IBankAction;
import com.ensimag.api.bank.IBankNode;
import com.ensimag.api.bank.IUser;

import java.io.Serializable;

public class CloseAccountAction implements IBankAction {
    IUser user;

    public CloseAccountAction(IUser user) {
        this.user = user;
    }

    public Serializable execute(IBankNode node) {
        return null;//node.closeAccount(user);
    }
}
