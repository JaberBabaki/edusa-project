package ir.edusa.parents.Models;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private int Command_Reference_Code;
    private String Message_Text = "";
    private boolean Is_Group_Message;
    private boolean Ack_Is_Neccessary;
    private boolean isFromPanel;
    private Date date;

    public Message() {
    }

    public Message(int command_Reference_Code, String message_Text, boolean is_Group_Message, boolean ack_Is_Neccessary) {
        Command_Reference_Code = command_Reference_Code;
        Message_Text = message_Text;
        Is_Group_Message = is_Group_Message;
        Ack_Is_Neccessary = ack_Is_Neccessary;
    }

    public boolean isFromPanel() {
        return isFromPanel;
    }

    public void setFromPanel(boolean fromPanel) {
        isFromPanel = fromPanel;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCommand_Reference_Code() {
        return Command_Reference_Code;
    }

    public void setCommand_Reference_Code(int command_Reference_Code) {
        Command_Reference_Code = command_Reference_Code;
    }

    public String getMessage_Text() {
        return Message_Text;
    }

    public void setMessage_Text(String message_Text) {
        Message_Text = message_Text;
    }

    public boolean is_Group_Message() {
        return Is_Group_Message;
    }

    public void setIs_Group_Message(boolean is_Group_Message) {
        Is_Group_Message = is_Group_Message;
    }

    public boolean isAck_Is_Neccessary() {
        return Ack_Is_Neccessary;
    }

    public void setAck_Is_Neccessary(boolean ack_Is_Neccessary) {
        Ack_Is_Neccessary = ack_Is_Neccessary;
    }
}