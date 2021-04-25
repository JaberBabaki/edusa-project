package ir.edusa.parents.Network.Responses;


import ir.edusa.parents.Models.ZFError;

abstract public class AbstractResponse {
    public boolean Status;
    public ZFError Error;

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        this.Status = status;
    }

    public ZFError getError() {
        return Error;
    }

    public void setError(ZFError error) {
        this.Error = error;
    }
}
