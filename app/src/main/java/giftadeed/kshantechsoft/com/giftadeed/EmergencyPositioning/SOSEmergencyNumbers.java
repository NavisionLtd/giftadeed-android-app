package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DatabaseAccess;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SOSEmergencyNumbers extends AppCompatActivity {
    private static final int PICK_CONTACT = 1000;
    public static final int RequestPermissionCode = 1;
    int flag = 0;
    EditText et_contact_1, et_contact_2;
    TextInputLayout et_contact_1_layout, et_contact_2_layout;
    ImageView contactBook1, contactBook2;
    Button btnSaveNumbers;
    DatabaseAccess databaseAccess;
    int contactsCount;
    String callingfrom = "";
    ArrayList<Contact> contactArrayList;
    SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_numbers);
        et_contact_1 = (EditText) findViewById(R.id.first_contact);
        et_contact_2 = (EditText) findViewById(R.id.second_contact);
        et_contact_1_layout = (TextInputLayout) findViewById(R.id.first_contact_textinput);
        et_contact_2_layout = (TextInputLayout) findViewById(R.id.second_contact_textinput);
        contactBook1 = (ImageView) findViewById(R.id.contact_book_1);
        contactBook2 = (ImageView) findViewById(R.id.contact_book_2);
        btnSaveNumbers = (Button) findViewById(R.id.btn_save_numbers);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sessionManager = new SessionManager(SOSEmergencyNumbers.this);
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
                Log.d("contact1_2", "" + contact.getContactName1() + "," + contact.getContactName2());
                if (contact.getContactName1() == null) {
                    et_contact_1_layout.setHint(getResources().getString(R.string.family_or_friend));
                } else {
                    et_contact_1_layout.setHint(contact.getContactName1());
                }

                if (contact.getContactName2() == null) {
                    et_contact_2_layout.setHint(getResources().getString(R.string.family_or_friend));
                } else {
                    et_contact_2_layout.setHint(contact.getContactName2());
                }
            }
        }

        et_contact_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_contact_1.getText().length() == 0) {
                    et_contact_1_layout.setHint(getResources().getString(R.string.family_or_friend));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_contact_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_contact_2.getText().length() == 0) {
                    et_contact_2_layout.setHint(getResources().getString(R.string.family_or_friend));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        contactBook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    // All Permissions Granted
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                    flag = 1;
                } else {
                    requestPermission();
                    Toast.makeText(SOSEmergencyNumbers.this, "Please allow app permissions", Toast.LENGTH_LONG).show();
                }
            }
        });

        contactBook2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    // All Permissions Granted
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                    flag = 2;
                } else {
                    requestPermission();
                    Toast.makeText(SOSEmergencyNumbers.this, "Please allow app permissions", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSaveNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((et_contact_1.getText().length() > 0) || (et_contact_2.getText().length() > 0)) {
                    Log.d("values", et_contact_1.getText().toString() + "," + et_contact_2.getText().toString() + "," + et_contact_1_layout.getHint().toString() + "," + et_contact_2_layout.getHint().toString());
                    databaseAccess.updateContact("1", et_contact_1.getText().toString(), et_contact_2.getText().toString(), et_contact_1_layout.getHint().toString(), et_contact_2_layout.getHint().toString());
                    ToastPopUp.displayToast(SOSEmergencyNumbers.this, getResources().getString(R.string.contact_saved));
                } else if ((et_contact_1.getText().length() == 0) && (et_contact_2.getText().length() == 0)) {
                    Log.d("values", "All Blank");
                    databaseAccess.updateContact("1", "", "", "", "");
                    ToastPopUp.displayToast(SOSEmergencyNumbers.this, getResources().getString(R.string.contact_saved));
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
                        String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        // Todo something when contact number selected
                        if (contactNumber.length() > 0) {
                            if (flag == 1) {
                                et_contact_1.setText(contactNumber);
                                et_contact_1_layout.setHint(contactName);
                            } else if (flag == 2) {
                                et_contact_2.setText(contactNumber);
                                et_contact_2_layout.setHint(contactName);
                            }
                        } else {
                            ToastPopUp.displayToast(SOSEmergencyNumbers.this, getResources().getString(R.string.blank_contact_number));
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
            SOSEmergencyNumbers.this.finish();
        } else {
            Intent i = new Intent(getBaseContext(), TaggedneedsActivity.class);
            startActivity(i);
        }
    }

    //-------------------requests all permissions-------------------------------------------------------
    private void requestPermission() {
        ActivityCompat.requestPermissions(SOSEmergencyNumbers.this, new String[]
                {
                        CAMERA,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE,
                        ACCESS_FINE_LOCATION,
                        READ_CONTACTS
                }, RequestPermissionCode);
    }

    //---------------------------------check permissions method-------------------------------------
    public boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
//        int SixthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED;
//                SixthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }
}
