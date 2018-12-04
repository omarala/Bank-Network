package com.ensimag.bank;

import com.ensimag.api.bank.IAccount;
import com.ensimag.message.Result;

public class IAccountResult extends Result {
    public IAccountResult(long messageId, IAccount account){
        super(messageId, account);
    }
}
