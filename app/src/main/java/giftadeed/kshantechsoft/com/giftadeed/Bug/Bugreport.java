package giftadeed.kshantechsoft.com.giftadeed.Bug;

import android.os.Build;

import java.lang.reflect.Field;

/**
 * Created by ADMIN on 19-02-2016.
 */
public class Bugreport {
    public void sendbug(String errormsg)
    {

        String subject="Bug Gift A Deed app";

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
        String message=builder.toString()+errormsg;
        SendMail sm = new SendMail("giftadeed2017@gmail.com", subject, message);

        //Executing sendmail to send email
        sm.execute();
    }
}
