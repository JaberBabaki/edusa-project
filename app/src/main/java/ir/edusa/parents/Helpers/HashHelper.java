package ir.edusa.parents.Helpers;

public class HashHelper {


    public static String Calculate_Password(String apiKey, String Authentication_Key) {
        if (!ValidatorHelper.isValidString(apiKey) || !ValidatorHelper.isValidString(Authentication_Key)) {
            return "";
        }
        String Password = "";
        String Key1 = apiKey;
        String Key2 = Authentication_Key;
        int Key2_Length = Key2.length();
        for (int i = Key1.length() - 1; i >= 0; i--) {
            Password = Password + (((int) Key1.charAt(i) - 20) * ((int) Key2.charAt(i % Key2_Length) - 10));
        }
        return Password;
    }
}
