package com.api.generator;

import java.sql.Timestamp;

public class TimeStamp {

    public TimeStamp() {
    }

    public long TimeStamps(long ts1) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long ts = timestamp.getTime();
        return ts;
    }
}
