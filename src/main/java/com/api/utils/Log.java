package com.api.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {

    public Logger Log;

    public Log(Class<?> clazz) {
        this.Log = LogManager.getLogger(clazz);
    }

    public Log(Object object) {
        this.Log = LogManager.getLogger(object);
    }

    public Log(String loggerName) {
        this.Log = LogManager.getLogger(loggerName);
    }

    public void info(String message) {
        this.Log.info(message);
    }

    public void info(String s, String message) {
        this.Log.info(message);
    }

    public void warn(String message) {
        this.Log.warn(message);
    }

    public void error(String message) {
        this.Log.error(message);
    }

    public void error(Throwable throwable) {
        this.Log.error(this.getStackTraceString(throwable));
    }

    private String getStackTraceString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.getBuffer().toString();
    }

    public void fatal(String message) {
        this.Log.fatal(message);
    }

    public void debug(String message) {
        this.Log.debug(message);
    }

    public void error(String[] messages) {
        String[] var2 = messages;
        int var3 = messages.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String msg = var2[var4];
            this.Log.error(msg);
        }

    }
}
