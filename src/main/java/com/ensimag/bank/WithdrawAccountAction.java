package com.ensimag.bank;

import com.ensimag.api.bank.IAccount;
import com.ensimag.api.bank.IBankAction;
import com.ensimag.api.bank.IBankNode;

import java.io.Serializable;

public class WithdrawAccountAction implements IBankAction {
    private long accountNumber;
    private int amount;

    public WithdrawAccountAction(long accountNumber, int amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public Serializable execute(IBankNode iBankNode) throws Exception {
        try{
            IAccount account = iBankNode.getAccount(accountNumber);
            account.remove(amount);
            return account;
        }catch(Exception e){
            throw e;
        }
    }
}
