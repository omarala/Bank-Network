package com.ensimag.bank;

import com.ensimag.api.bank.IBankAction;
import com.ensimag.api.bank.IBankNode;

import java.io.Serializable;

public class CloseAccountAction implements IBankAction {
    private long numberAccount;
    private User user;

    public CloseAccountAction(long numberAccount, User user) {
        this.numberAccount = numberAccount;
        this.user = user;
    }

    public Serializable execute(IBankNode node) throws Exception {
        //return null;//node.closeAccount(user);
        try {
            if(node.closeAccount(numberAccount)){
                return "Account : " + numberAccount + " closed for client : "
                        + user.getName() + " " +user.getName();
            }
            return "Unable to closed account : " + numberAccount + " for Client : "
                    + user.getName() + " " +user.getName();
        } catch (Exception e) {
            throw e;
        }
    }
}
