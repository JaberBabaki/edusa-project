package ir.edusa.parents.Models;

import java.io.Serializable;

public class Command implements Serializable {
    public static final int COMMAND_CODE_MESSAGE = 5;
    public static final int COMMAND_CODE_GROUP_MESSAGE = 7;

    private int Command_Unique_Code;
    private int Command_Code;


    public Command() {
    }

    public int getCommand_Unique_Code() {
        return Command_Unique_Code;
    }

    public void setCommand_Unique_Code(int command_Unique_Code) {
        Command_Unique_Code = command_Unique_Code;
    }

    public int getCommand_Code() {
        return Command_Code;
    }

    public void setCommand_Code(int command_Code) {
        Command_Code = command_Code;
    }


}