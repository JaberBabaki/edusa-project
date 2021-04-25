package ir.edusa.parents.Helpers;


import java.text.DecimalFormat;

import ir.edusa.parents.Utils.Log;

public class NumberHelper {

    private static NumberHelper numberHelper;
    private static DecimalFormat decimalFormat;
    private static String[] arabicNumbers = new String[]{"٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩", "،"};
    private static String[] persianNumbers = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹", "،"};
    private static String[] englishNumbers = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "٫"};

    //Hide Constructor
    private NumberHelper() {
        decimalFormat = new DecimalFormat("###,###,###,###,###");
    }

    //Instantiate Method
    public static synchronized NumberHelper with() {
        if (numberHelper == null) {
            numberHelper = new NumberHelper();
        }

        return numberHelper;
    }

    //To Persian Number
    public String toPersianNumber(String s) {
        if (!ValidatorHelper.isValidString(s)) {
            return "";
        }
        for (int i = 0; i < englishNumbers.length; i++) {
            s = s.replaceAll(englishNumbers[i], persianNumbers[i]);
        }
        return s;
    }

    //To English Number
    public String toEnglishNumber(String text) {
        if (!ValidatorHelper.isValidString(text)) {
            return "";
        }
        for (int i = 0; i < persianNumbers.length; i++) {
            text = text.replaceAll(persianNumbers[i], englishNumbers[i]);
            text = text.replaceAll(arabicNumbers[i], englishNumbers[i]);
        }

        return text;
    }

    //Format
    public String format(int i) {
        return decimalFormat.format(i);
    }

    //Format
    public String format(double i) {
        return decimalFormat.format(i);
    }

    //Formatted Persian Number
    public String formattedPersianNumber(String j) {

        int i = Integer.parseInt(ValidatorHelper.isValidString(j) ? j : "0");

        //Format
        String formattedString = format(i);

        //To Persian Number
        return toPersianNumber(formattedString);
    }

    //Formatted Persian Number
    public String formattedPersianNumber(int i) {

        //Format
        String formattedString = format(i);

        //To Persian Number
        return toPersianNumber(formattedString);
    }

    //Formatted Persian Number
    public String formattedPersianNumber(double i) {

        //Format
        String formattedString = format(i);

        //To Persian Number
        return toPersianNumber(formattedString);
    }

    //Has English Number
    public boolean hasEnglishNumber(String s) {
        for (int i = 0; i < englishNumbers.length; i++) {
            if (s.contains(englishNumbers[i])) {
                return true;
            }
        }

        return false;
    }

    public void calculateRate(int star1, int star2, int star3, int star4, int star5) {
        Log.e("RATING = ", (float) (star1 + star2 * 2 + star3 * 3 + star4 * 4 + star5 * 5) / (star1 + star2 + star3 + star4 + star5) + " ");

    }

    public String changeMinusPosition(String number) {
        if (ValidatorHelper.isValidString(number)) {
            if (number.startsWith("-")) {
                number = number.replace("-", "");
                number = number + "-";
            }
        }
        return number;
    }
}
