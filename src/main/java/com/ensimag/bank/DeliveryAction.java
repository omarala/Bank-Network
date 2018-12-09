package com.ensimag.bank;

import java.io.Serializable;

import com.ensimag.api.bank.IBankAction;
import com.ensimag.api.bank.IBankNode;
import com.ensimag.message.Result;

public class DeliveryAction implements IBankAction {

    private Result result;

    public DeliveryAction(Result result) {
        this.result = result;
    }

    public Serializable execute(IBankNode node) throws Exception {
        return result;
    }

}
