package ir.edusa.parents.Helpers;

public class ValidatorHelper {

    private static final int MINIMUM_CREDIT_FOR_INCREASE_CREDIT = 100;
    private static final String MOBILE_PATTERN = "(\\+98|0)?9\\d{9}";

    //Hide Constructor
    private ValidatorHelper() {

    }

    //Is Valid String
    public static boolean isValidString(String s) {
        return (s != null && !s.equals(""));
    }

    //Is Valid Email
    public static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //Is Equal
    public static boolean isEqual(String first, String second) {
        return first.equals(second);
    }

    //Is Bigger Than Minimum Credit
    public static boolean isBiggerThanMinimumCredit(int credit) {
        if (credit >= MINIMUM_CREDIT_FOR_INCREASE_CREDIT) {
            return true;
        } else {
            return false;
        }
    }

    //Is Valid Phone Number
    public static boolean isValidPhoneNumber(String s) {
        return s != null && !s.equals("");
    }

    //Is Valid Phone Number
    public static boolean isValidCellphone(String s) {
        boolean correct = false;

        //Is Valid String
        if (isValidString(s)) {
            //Trim, Replace Space And Convert Number To English
            s = s.trim();
            s = s.replace(" ", "");
            s = NumberHelper.with().toEnglishNumber(s);

            //Check Pattern
            if ((s.length() <= 12) && (s.matches(MOBILE_PATTERN))) {
                correct = true;
            } else {
                correct = false;
            }
        }

        return correct;
    }

    public static String standardizePhone(String input) {
        input = NumberHelper.with().toEnglishNumber(input.trim().replace(" ", ""));
        if (input.startsWith("+98")) {
            return "0" + input.substring(3, input.length());
        }
//        else if (input.startsWith("0")) {
//            return input.substring(1, input.length());
//        }
        return input;
    }
}
