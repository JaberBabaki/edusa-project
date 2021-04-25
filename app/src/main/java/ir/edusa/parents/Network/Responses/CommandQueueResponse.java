package ir.edusa.parents.Network.Responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pouya on 8/26/15.
 */
public class CommandQueueResponse extends AbstractResponse implements Serializable {
    //   JSONObject responseParams;
    @SerializedName("Result_Data")
    private Data data;


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data {
        private ArrayList<Object> commands = new ArrayList<>();

        public ArrayList<Object> getCommands() {
            return commands;
        }

        public void setCommands(ArrayList<Object> commands_4_Device) {
            commands = commands_4_Device;
        }
    }
}
