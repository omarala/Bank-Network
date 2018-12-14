package com.ensimag.bank;

import com.ensimag.api.bank.IAccount;
import com.ensimag.api.bank.IBankAction;
import com.ensimag.api.bank.IBankNode;

import java.io.Serializable;

public class AddAmoutAction implements IBankAction {
    private long accountNumber;
    private int amount;

    public AddAmoutAction(long numberAccount, int amount) {
        this.accountNumber = numberAccount;
        this.amount = amount;
    }

    public Serializable execute(IBankNode iBankNode) throws Exception {
        try{
            IAccount account = iBankNode.getAccount(accountNumber);
            account.add(amount);
            return account;
        }catch(Exception e){
            throw e;
        }
    }
}
