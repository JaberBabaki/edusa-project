package ir.edusa.parents.Network;


public class ApiConstants {

    //    public final static String API_PATH = "http://www.sepana.ir:8001/api/Tracking/";
//    public final static String API_PATH = "http://79.175.133.52/api/Tracking/";
    public static String API_PATH = "https://www.edusa.ir/api/Tracking/";


    public static String API_LOGIN = API_PATH + "Login";
    public static String API_USER_INFO = API_PATH + "Get_Passenger_Device_Info";
    public static String API_SET_FCM_TOKEN = API_PATH + "Subscribe_FCM_Token_In_Server";
    public static String API_GET_DRIVER_LAST_LOCATION = API_PATH + "Get_Driver_Last_Location";
    public static String API_START_LIVE_TRACKING = API_PATH + "Start_FCM_Driver_Tracking";
    public static String API_STOP_LIVE_TRACKING = API_PATH + "Stop_FCM_Driver_Tracking";
    public static String API_PING = API_PATH + "Ping";
    public static String API_REGISTER_DEVICE = API_PATH + "Register_Device";
    public static String API_GET_ROUTE = API_PATH + "Get_Driver_Tracking_Points";
    public static String API_SET_ALARM_POINT = API_PATH + "Set_Passenger_Alarm_Location_Point";
    public static String API_SEND_MESSAGE_POINT = API_PATH + "Message_2_Server";
    public static String API_SET_ADDRESS_LOCATION = API_PATH + "Set_Passenger_Address_Location_Point";
    public static String API_UPDATE_LINK = API_PATH + "Get_Android_Application_Download_Url";
    public static String API_DOWN_STREAM_REGISTER = API_PATH + "Client_2_Server_Register_Process";
    public static String API_SEND_ABSENCE = API_PATH + "Set_Passenger_Absence";
    public static String API_UPLOAD_IMAGE = API_PATH + "Upload_Image";
    public static String API_COMMAND_ACK = API_PATH + "Acknowledge_Command_Handled_By_Device";
    public static String API_COMMAND_QUEUE = API_PATH + "Commands_In_Server_Queue_For_The_Device";


    public final static int ENABLE_GPS_CODE = 1987;
    public final static int RELOGIN_ERROR_CODE = 198374;
    public final static int NETWORK_ERROR_CODE = 198323;


    public final static int ERROR_CODE_GENERAL = -100;

    public final static String DEFAULT_PRIVATE_KEY = "23415";


    public final static int METHOD_POST = 1;
    public final static int METHOD_GET = 2;
    public final static int METHOD_PUT = 3;
    public final static int METHOD_DELETE = 4;
}
