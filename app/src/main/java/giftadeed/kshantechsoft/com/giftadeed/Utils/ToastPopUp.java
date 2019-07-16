package giftadeed.kshantechsoft.com.giftadeed.Utils;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.Gravity;
import android.widget.Toast;

public class ToastPopUp {

    public static Toast toast, mToast;

    public static void show(Context context, String msg) {
        try {
            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public static void displayToast(Context context, String message) {
        if (mToast != null)
            mToast.cancel();
        mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static void showMsgLessTime(Context context1, String msg1) {

        try {

            Toast toast = Toast.makeText(context1, msg1, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    static AlertDialog alertDialog;

    public static void showOkDialogBox(Context ctx, String msg, String alertok) {

        try {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
            alertDialogBuilder.setMessage(msg);


            alertDialogBuilder.setPositiveButton(alertok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            if (alertDialog != null && alertDialog.isShowing())
                                alertDialog.dismiss();

                        }
                    });

            alertDialog = alertDialogBuilder.create();

            alertDialog.show();
        } catch (Exception e) {
        }

    }


}