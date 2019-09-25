package com.immotor.collectData.exception;

public class ServiceException extends  RuntimeException{


    private Integer resultCode;
    private String message;

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ServiceException(Integer resultCode, String message) {
        super(message);
        this.message=message;

        this.resultCode = resultCode;
        System.out.println(message+":"+resultCode);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public Integer getResultCode() {
        return resultCode;
    }
    @Override
    public String toString() {
        return message  ;
    }
}
