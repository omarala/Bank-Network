package com.ensimag.bank;

import com.ensimag.api.bank.IAccount;
import com.ensimag.message.Result;

import java.util.LinkedList;

public class ListIAccountResult extends Result {
    public ListIAccountResult(long messageId, LinkedList<IAccount> list){
        super(messageId, list);
    }
}
