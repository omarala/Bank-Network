package com.ensimag.message;

import com.ensimag.api.message.IAck;

public class Ack implements IAck {

        long ackSenderId;
        long ackMessageId;

    public Ack(long ackSenderId, long ackMessageId)
        {
            this.ackSenderId = ackSenderId;
            this.ackMessageId = ackMessageId;
        }
        public long getAckSenderId()
        {
            return this.ackSenderId;
        }
        public long getAckMessageId()
        {
            return this.ackMessageId;
        }

}
