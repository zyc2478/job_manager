package com.zyc.jobmanager.exception;

public class ServerException extends Exception {

    public ServerException(int internalError) {
        System.out.println("Error，发生了系统内部错误！");
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
