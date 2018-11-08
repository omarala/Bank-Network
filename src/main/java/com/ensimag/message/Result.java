package com.ensimag.message;

import com.ensimag.api.message.IResult;

import java.io.Serializable;

public class Result implements IResult<Serializable> {
    private long messageId;
    private Serializable data;

    public Result(long messageId, Serializable data) {
        this.messageId = messageId;
        this.data = data;
    }

    public long getMessageId() {
        return messageId;
    }

    public Serializable getData() {
        return data;
    }
}
