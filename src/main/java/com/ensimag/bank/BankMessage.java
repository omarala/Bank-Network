package com.ensimag.bank;

import com.ensimag.api.bank.IBankAction;
import com.ensimag.api.bank.IBankMessage;
import com.ensimag.api.message.EnumMessageType;
import com.ensimag.api.message.IAction;

public class BankMessage implements IBankMessage {
    private long senderId;
    private long messageId;
    private long originalBankSenderId;
    private long destinationBankId;
    private IBankAction action;
    private EnumMessageType messageType;

    public BankMessage(long senderId, long messageId, long originalBankSenderId, long destinationBankId, IBankAction action, EnumMessageType messageType) {
        this.senderId = senderId;
        this.messageId = messageId;
        this.originalBankSenderId = originalBankSenderId;
        this.destinationBankId = destinationBankId;
        this.action = action;
        this.messageType = messageType;
    }


    public IBankAction getAction() {
        return action;
    }

    public IBankMessage clone() {
        return this;
    }

    public long getSenderId() {
        return senderId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setSenderId(long var1) {
        this.senderId = var1;
    }

    public long getOriginalBankSenderId() {
        return originalBankSenderId;
    }

    public long getDestinationBankId() {
        return destinationBankId;
    }

    public EnumMessageType getMessageType() {
        return messageType;
    }
}
