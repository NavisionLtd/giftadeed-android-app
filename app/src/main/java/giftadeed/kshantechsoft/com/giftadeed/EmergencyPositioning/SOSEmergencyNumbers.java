package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DatabaseAccess;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;

public class SOSEmergencyNumbers extends AppCompatActivity {
    private static final int PICK_CONTACT = 1000;
    int flag = 0;
    EditText et_contact_1, et_contact_2;
    ImageView contactBook1, contactBook2;
    Button btnSaveNumbers;
    DatabaseAccess databaseAccess;
    int contactsCount;
    String callingfrom = "";
    ArrayList<Contact> contactArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_numbers);
        et_contact_1 = (EditText) findViewById(R.id.first_contact);
        et_contact_2 = (EditText) findViewById(R.id.second_contact);
        contactBook1 = (ImageView) findViewById(R.id.contact_book_1);
        contactBook2 = (ImageView) findViewById(R.id.contact_book_2);
        btnSaveNumbers = (Button) findViewById(R.id.btn_save_numbers);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        callingfrom = getIntent().getStringExtra("callingfrom");
        contactsCount = databaseAccess.getContactsCount();
        if (contactsCount > 0) {
            contactArrayList = new ArrayList<>();
            contactArrayList = databaseAccess.getAllContacts();
            for (Contact contact : contactArrayList) {
                et_contact_1.setText(contact.getContact1());
                et_contact_2.setText(contact.getContact2());
            }
        }

        contactBook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
                flag = 1;
            }
        });

        contactBook2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
                flag = 2;
            }
        });

        btnSaveNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((et_contact_1.getText().length() > 0) || (et_contact_2.getText().length() > 0)) {
                    databaseAccess.updateContact("1", et_contact_1.getText().toString(), et_contact_2.getText().toString());
                    ToastPopUp.displayToast(SOSEmergencyNumbers.this, "Contacts saved");
                } else if ((et_contact_1.getText().length() == 0) && (et_contact_2.getText().length() == 0)) {
                    databaseAccess.updateContact("1", "", "");
                    ToastPopUp.displayToast(SOSEmergencyNumbers.this, "Contacts saved");
                }
                if (callingfrom.equals("login")) {
                    SOSEmergencyNumbers.this.finish();
                } else if (callingfrom.equals("set")) {
                    Intent i = new Intent(getBaseContext(), SOSOptionActivity.class);
                    startActivity(i);
                    SOSEmergencyNumbers.this.finish();
                } else {
                    Intent i = new Intent(getBaseContext(), TaggedneedsActivity.class);
                    startActivity(i);
                }
            }
        });

        /*btnCallSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Latitude = String.valueOf(new GPSTracker(SOSEmergencyNumbers.this).getLatitude());
                final String Longitude = String.valueOf(new GPSTracker(SOSEmergencyNumbers.this).getLongitude());
                String msgNumber1 = et_contact_1.getText().toString();
                String msgNumber2 = et_contact_2.getText().toString();
                String message1 = "Your friend is in emergency situation \n";
                String message2 = "http://maps.google.com/maps?saddr=" + Latitude + "," + Longitude;
                Log.d("message_link", message1 + message2);
                String numbers = "";
                if (!TextUtils.isEmpty(msgNumber1) && !TextUtils.isEmpty(msgNumber2)) {
                    numbers = msgNumber1 + ";" + msgNumber2;
                } else if (!TextUtils.isEmpty(msgNumber1) && TextUtils.isEmpty(msgNumber2)) {
                    numbers = msgNumber1;
                } else if (TextUtils.isEmpty(msgNumber1) && !TextUtils.isEmpty(msgNumber2)) {
                    numbers = msgNumber2;
                }

                if (numbers.length() > 0) {
                    Uri uri = Uri.parse("smsto:" + numbers);
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    it.putExtra("sms_body", message1 + message2);
                    startActivity(it);
                }
            }
        });*/
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String contactNumber = "";
                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            contactNumber = phones.getString(phones.getColumnIndex("data1"));
                        }
                        String contactNumberName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        // Todo something when contact number selected
                        if (contactNumber.length() > 0) {
                            if (flag == 1) {
                                et_contact_1.setText(contactNumber);
                            } else if (flag == 2) {
                                et_contact_2.setText(contactNumber);
                            }
                        } else {
                            ToastPopUp.displayToast(SOSEmergencyNumbers.this, "Blank contact number");
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (callingfrom.equals("login")) {
            SOSEmergencyNumbers.this.finish();
        } else if (callingfrom.equals("set")) {
            Intent i = new Intent(getBaseContext(), SOSOptionActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(getBaseContext(), TaggedneedsActivity.class);
            startActivity(i);
        }
    }
}
