/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Bug;

import android.os.Build;

import java.lang.reflect.Field;

/**
 * Created by ADMIN on 19-02-2016.
 */
public class Bugreport {
    public void sendbug(String errormsg) {
        String subject = "Bug Gift A Deed app";
        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);
        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;
            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                //  e.printStackTrace();
            } catch (IllegalAccessException e) {
                // e.printStackTrace();
            } catch (NullPointerException e) {
                // e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ").append(fieldName).append(" : ");
                builder.append("sdk=").append(fieldValue);
            }
        }
        String message = builder.toString() + errormsg;
        String email_id = "giftadeed2017@gmail.com";
        SendMail sm = new SendMail(email_id, subject, message);
        //Executing sendmail to send email
        sm.execute();
    }
}