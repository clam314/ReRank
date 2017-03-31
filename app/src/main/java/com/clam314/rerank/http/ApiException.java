package com.clam314.rerank.http;

/**
 * Created by clam314 on 2017/3/31
 */

public class ApiException extends RuntimeException {
    private Status status;

    public ApiException(boolean error,String msg){
        status = new Status(error,msg);
    }

    public Status getStatus() {
        return status;
    }

    private static class Status{
        boolean error;
        String msg;

        private Status(){}

        private Status(boolean error,String msg){
            this.error = error;
            this.msg = msg;
        }
    }
}
