package ir.edusa.parents.Models;

import java.io.Serializable;

public class ZFError implements Serializable {
    private int Code;
    private String Message;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
