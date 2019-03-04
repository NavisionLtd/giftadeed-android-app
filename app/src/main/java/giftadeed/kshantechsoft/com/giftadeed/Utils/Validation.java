package giftadeed.kshantechsoft.com.giftadeed.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Patterns;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nilesh on 2/22/2016.
 */
public class Validation {

    public static String FILTER_CATEGORY = "All";
    //    public static float inital_radius=10.0f;
    public static float inital_radius = 10000.0f;
    //----------------------THis is validation part of Signup Page----------------------------------
    public static final String WARNING_VALID_MOBILE = "Please enter valid mobile number";
    public static final String WARNING_VALID_EMAIL = "Please enter valid mobile number";
    public static final String WARNING_VALID_OTP = "Please enter valid OTP";
    public static final String WARNING_VALID_PWD = "Please enter correct Password";

    public static final String WARNING_NEW_PWD = "Please enter new password";

    public static final String WARNING_NEW_NAME = "Please enter name";
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    // public static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp|jpeg))$)";

    public static final String PASSWORD_PATTERN = "((?!\\s)\\A)(\\s|(?<!\\s)\\S){4,20}\\Z";// "^[A-Za-z0-9]{4,20}$"
    //---------------------------make sure all entry value should be filled on over there------------
    // "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})"

    public static boolean isMatchPattern(String charSequence, String expression) {

        try {

            if (charSequence != null && charSequence.trim().length() > 0) {

                CharSequence input = charSequence;
                Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);

                Matcher matcher = pattern.matcher(input);
                if (matcher.matches()) {

                    System.out.println("Match......");
                    return true;
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("Not Match......");
            return false;
        }

        System.out.println("Not Match......");

        return false;
    }

    public static boolean validatePhoneNumber(String phoneNo) {
        // validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}"))
            return true;
            // validating phone number with -, . or spaces
        else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
            return true;
            // validating phone number with extension length from 3 to 5
        else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
            return true;
            // validating phone number where area code is in braces ()
        else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
            return true;
            // return false if nothing matches the input
        else
            return false;

    }

    public static boolean isValidPhoneNumber(String target) {
        if (target == null) {
            return false;
        } else {
            if (target.length() < 6 || target.length() > 13) {
                return false;
            } else {
                return Patterns.PHONE.matcher(target).matches();
            }
        }
    }

    public final static boolean isValidEmail(CharSequence target) {

        if (target == null) {

            return false;

        } else {

            return Patterns.EMAIL_ADDRESS.matcher(target).matches();

        }

    }

    public static Uri resIdToUri(Context context, int resId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FORESLASH + resId);
    }

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FORESLASH = "/";

    public static boolean isStringNullOrBlank(String str) {

        try {

            if (str == null) {
                return true;
            } else if (str.equals("null") || str.equals("") || (str != null && str.isEmpty())) {
                return true;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

    public static boolean isStringEqual(String str1, String st2) {

        try {

            if (str1 == null) {

                return false;

            } else if (st2 == null) {

                return false;

            } else if (str1.equalsIgnoreCase(st2)) {

                return true;

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

    public static boolean isNetworkAvailable(Context context) {

        NetworkInfo localNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return (localNetworkInfo != null) && (localNetworkInfo.isConnected());

    }

    public static final String networkError = "No network";

    public static void showNetworkError(Context ctx) {

        try {
            if (ctx != null) {
                Toast.makeText(ctx, networkError, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static final void showToast(Context mContext, String msg) {

        Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();

    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[A-Za-z][A-Za-z0-9_]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isSpacesContains(String str) {

        try {
            for (char c : str.toCharArray()) {
                if (str == null) {
                    return true;
                } else if (str.equals("null") || str.equals(" ") || Character.isWhitespace(c) || (str != null && str.isEmpty())) {
                    return true;
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

//remove whitespace as a first characters
    /*public   InputFilter ignoreFirstWhiteSpace() {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        if (dstart == 0)
                            return "";
                    }
                }
                return null;
            }
        };
    }*/
}
