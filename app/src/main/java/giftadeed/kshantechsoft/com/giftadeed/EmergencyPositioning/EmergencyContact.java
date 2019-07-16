package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DatabaseAccess;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;

public class EmergencyContact extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_CONTACT = 1000;
    int flag = 0;
    EditText et_contact_1, et_contact_2, et_contact_3;
    TextView msg1, msg2, msg3;
    ImageView clear1, clear2, clear3;
    Button btnCallSos;
    DatabaseAccess databaseAccess;
    int contactsCount;
    String callingfrom = "";
    ArrayList<Contact> contactArrayList;
    private AlertDialog alertDialogLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        et_contact_1 = (EditText) findViewById(R.id.et_first_contact);
        et_contact_2 = (EditText) findViewById(R.id.et_second_contact);
        et_contact_3 = (EditText) findViewById(R.id.et_third_contact);
        clear1 = (ImageView) findViewById(R.id.clear_1);
        clear2 = (ImageView) findViewById(R.id.clear_2);
        clear3 = (ImageView) findViewById(R.id.clear_3);
        msg1 = (TextView) findViewById(R.id.msg1);
        msg2 = (TextView) findViewById(R.id.msg2);
        msg3 = (TextView) findViewById(R.id.msg3);
        btnCallSos = (Button) findViewById(R.id.btn_call_sos);
        GPSTracker gps = new GPSTracker(EmergencyContact.this);
        if (!gps.isGPSEnabled) {
            gps.showSettingsAlert();
        }
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
                et_contact_3.setText(contact.getContact3());
            }
        }
        et_contact_1.setOnClickListener(this);
        et_contact_2.setOnClickListener(this);
        et_contact_3.setOnClickListener(this);

        clear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_contact_1.getText().length() > 0) {
                    et_contact_1.setText("");
                    et_contact_1.clearFocus();
                    databaseAccess.updateContact("1", "", et_contact_2.getText().toString());
                    msg1.setVisibility(View.VISIBLE);
                    msg1.setText("Contact removed");
                }
            }
        });

        clear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_contact_2.getText().length() > 0) {
                    et_contact_2.setText("");
                    et_contact_2.clearFocus();
                    databaseAccess.updateContact("1", et_contact_1.getText().toString(), "");
                    msg2.setVisibility(View.VISIBLE);
                    msg2.setText("Contact removed");
                }
            }
        });

        clear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_contact_3.getText().length() > 0) {
                    et_contact_3.setText("");
                    et_contact_3.clearFocus();
                    databaseAccess.updateContact("1", et_contact_1.getText().toString(), et_contact_2.getText().toString());
                    msg3.setVisibility(View.VISIBLE);
                    msg3.setText("Contact removed");
                }
            }
        });

        btnCallSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Latitude = String.valueOf(new GPSTracker(EmergencyContact.this).getLatitude());
                final String Longitude = String.valueOf(new GPSTracker(EmergencyContact.this).getLongitude());
                String phoneNumber = et_contact_1.getText().toString();
                String msgNumber1 = et_contact_2.getText().toString();
                String msgNumber2 = et_contact_3.getText().toString();
                String message1 = "Your friend is in emergency situation \n";
                String message2 = "http://maps.google.com/maps?saddr=" + Latitude + "," + Longitude;
                Log.d("message_link", message1 + message2);
                String numbers = "";
                if (!TextUtils.isEmpty(phoneNumber)) {
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

                    String dial = "tel:" + phoneNumber;
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(dial));
                    startActivity(intent);

                    //Stage-2 code
                    AlertDialog.Builder alert = new AlertDialog.Builder(EmergencyContact.this);
                    LayoutInflater li = LayoutInflater.from(EmergencyContact.this);
                    View confirmDialog = li.inflate(R.layout.giftneeddialog, null);
                    Button dialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_submit_mobileno);
                    Button dialogcancel = (Button) confirmDialog.findViewById(R.id.btn_Cancel_mobileno);
                    TextView dialogtext = (TextView) confirmDialog.findViewById(R.id.txtgiftneeddialog);

                    dialogtext.setText("Do you want to share your location?");
                    //-------------Adding our dialog box to the view of alert dialog
                    alert.setView(confirmDialog);
                    alert.setCancelable(false);
                    alertDialogLocation = alert.create();
                    alertDialogLocation.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                    alertDialogLocation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialogLocation.show();
                    alertDialogLocation.setCancelable(false);
                    dialogconfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialogLocation.dismiss();
                            Intent intent = new Intent(EmergencyContact.this, EmergencyStageTwo.class);
                            intent.putExtra("latitude", Latitude);
                            intent.putExtra("longitude", Longitude);
                            intent.putExtra("callingfrom", callingfrom);
                            startActivity(intent);
                            finish();
                        }
                    });
                    dialogcancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialogLocation.dismiss();
                        }
                    });
                } else {
                    et_contact_1.setFocusable(true);
                    et_contact_1.setError(getResources().getString(R.string.select_contact));
                    ToastPopUp.displayToast(EmergencyContact.this, getResources().getString(R.string.select_contact));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == et_contact_1) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
            flag = 1;
        } else if (view == et_contact_2) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
            flag = 2;
        } else if (view == et_contact_3) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
            flag = 3;
        }
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
                                et_contact_1.setError(null);
                                et_contact_1.setText(contactNumber);
                                msg1.setVisibility(View.VISIBLE);
                                msg1.setText("Contact saved");
                                databaseAccess.updateContact("1", et_contact_1.getText().toString(), et_contact_2.getText().toString());
                            } else if (flag == 2) {
                                et_contact_2.setText(contactNumber);
                                msg2.setVisibility(View.VISIBLE);
                                msg2.setText("Contact saved");
                                databaseAccess.updateContact("1", et_contact_1.getText().toString(), et_contact_2.getText().toString());
                            } else if (flag == 3) {
                                et_contact_3.setText(contactNumber);
                                msg3.setVisibility(View.VISIBLE);
                                msg3.setText("Contact saved");
                                databaseAccess.updateContact("1", et_contact_1.getText().toString(), et_contact_2.getText().toString());
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (callingfrom.equals("login")) {
            EmergencyContact.this.finish();
        } else {
            Intent i = new Intent(getBaseContext(), TaggedneedsActivity.class);
            startActivity(i);
        }
    }
}
