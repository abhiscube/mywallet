package com.paygilant.mywallet.OnWallet;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paygilant.mywallet.Contact.ContactActivity;
import com.paygilant.mywallet.MainActivity;
import com.paygilant.mywallet.R;
//import com.paygilant.deviceidetification.fingerprintdialog.FingerprintAuthenticationDialogFragment;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.Cipher;


import static com.paygilant.mywallet.MainActivity.forceLTRSupported;

public class Amount extends AppCompatActivity {
//    public static final String SERVER_URL = https://api.paygilant.com:8443/pmfd-med/1.1/";
    public static final String ZERO = "0";
    public static final String DOT = ".";
    public static final String DEL = "DEL";
    public static final String CURR_CODE = "CURR_CODE";
    public static final String THANK_YOU_MASSAGE = "Paygilant appreciates your cooperation";
    private static final int PICK_CONTACT=1;
    private static final int PICK_CONTACT_FROM_ACTIVITY=2;
    public static final int READ_CONTACTS_CALL_PERMISSION = 50;
    public String PIN_CODE = "1111";
    public boolean isScanFirst = false;

    public boolean isScanFirst() {
        return isScanFirst;
    }

    public void setScanFirst(boolean scanFirst) {
        isScanFirst = scanFirst;
    }

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String DIALOG_FRAGMENT_TAG = "myFragment";
    private static final String SECRET_MESSAGE = "Very secret message";
    private static final String KEY_NAME_NOT_INVALIDATED = "key_not_invalidated";
    public static final String DEFAULT_KEY_NAME = "default_key";
    public Bitmap bitmap;
    String [] results = {"LOW","STEP UP","HIGH","NOT ENOUGH DATA"};
    View view;
    private String cNumber;
//    private String amount;
    private String userID;
//    private String uuid;
//    private PaygilantManager paygilantHandler;
//    private PaygilantScreenListener actionManager;
    boolean isPassPinCode=true;
    private String currCode;
    ImageView i1, i2, i3, i4;
    EditText amountVal;
    TextView currPincode,message_error,textPay;
    ImageView lock_image;
    Button send;
    LinearLayout amountValLin,pinCodeRotate;
    Cipher defaultCipher;
    int riskLevelGlobal = -1;

//    private KeyStore mKeyStore;
//    private KeyGenerator mKeyGenerator;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Amount", "onCreate");
//        Singlton.getInstance().setContactModel(new ContactModel("",""));
        setContentView(R.layout.activity_amount);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ui_appBar_start)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        forceLTRSupported(this);

        Intent intent = getIntent();
        currCode = intent.getStringExtra(CURR_CODE);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userID = preferences.getString("PHONECONNECT", "");
        i1 =  findViewById(R.id.imageview_circle1);
        i2 =  findViewById(R.id.imageview_circle2);
        i3 =  findViewById(R.id.imageview_circle3);
        i4 =  findViewById(R.id.imageview_circle4);
        textPay = findViewById(R.id.textPay);
        send = findViewById(R.id.send);
        lock_image = findViewById(R.id.lock_image);
        amountVal =  findViewById(R.id.amountVal);
        amountValLin = findViewById(R.id.amountValLin);
        pinCodeRotate = findViewById(R.id.pinCodeRotate);
        currPincode = findViewById(R.id.currPincode);
        message_error = findViewById(R.id.message_error);
        amountVal.requestFocus();
        amountVal.setInputType(InputType.TYPE_CLASS_NUMBER);
        amountVal.setFocusableInTouchMode(true);
        isPassPinCode = true;
//        currPincode.clearAnimation();
//        currPincode.startAnimation(getBlinkAnimation(500));
        i1.setVisibility(View.GONE);
        i2.setVisibility(View.GONE);
        i3.setVisibility(View.GONE);
        i4.setVisibility(View.GONE);
        textPay.setVisibility(View.GONE);
        lock_image.setVisibility(View.INVISIBLE);
        amountValLin.setVisibility(View.VISIBLE);
        currPincode.setTextColor(getResources().getColor(R.color.ui_appBar_start));
//        currPincode.setText("Select Contact");
        ViewGroup.LayoutParams params = amountVal.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        amountVal.setLayoutParams(params);
        amountVal.setText(Singlton.getInstance().getAmount());
        amountVal.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
        send.setText("Next");
        amountVal.setRawInputType(InputType.TYPE_NULL);
        send.setBackgroundColor(getResources().getColor(R.color.colorConnectButton));
        send.setTextColor(getResources().getColor(R.color.unselected_text));
        int min = 1000;
        int max = 9999;
        Random r = new Random();
        bitmap = getImage(Environment.getExternalStorageDirectory()+File.separator+ "test.png");

        int resultPinCode = r.nextInt(max - min + 1) + min;
        PIN_CODE = resultPinCode+"";
//        currPincode.setText("Enter code "+PIN_CODE);

        amountVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.d("PINCODE_PRESS", "onKey: screen key pressed");

                checKPinCode(s);
            }
        });

        setButtonsListeners();
//        paygilantHandler = PaygilantManager.getInstance();

//        actionManager = paygilantHandler.startNewScreenListener(PaygilantManager.ScreenListenerType.PAYMENT_SCREEN, 4);

//        actionManager.setTouchToAllChildren(v);
        currPincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPassPinCode){
                    onChooseContactClicked();
                }
            }
        });



//        try {
//            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
//        } catch (KeyStoreException e) {
//            throw new RuntimeException("Failed to get an instance of KeyStore", e);
//        }
//        try {
//            mKeyGenerator = KeyGenerator
//                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
//        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
//            throw new RuntimeException("Failed to get an instance of KeyGenerator", e);
//        }
//        Cipher cipherNotInvalidated;
//        try {
//            defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
//                    + KeyProperties.BLOCK_MODE_CBC + "/"
//                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
//            cipherNotInvalidated = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
//                    + KeyProperties.BLOCK_MODE_CBC + "/"
//                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
//            throw new RuntimeException("Failed to get an instance of Cipher", e);
//        }
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

//        KeyguardManager keyguardManager = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            keyguardManager = getSystemService(KeyguardManager.class);
//        }
//        FingerprintManager fingerprintManager = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            fingerprintManager = getSystemService(FingerprintManager.class);
//        }
        Button purchaseButton = findViewById(R.id.send);
//        Button purchaseButtonNotInvalidated = findViewById(R.id.send);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////            purchaseButtonNotInvalidated.setEnabled(true);
////            purchaseButtonNotInvalidated.setOnClickListener(
////                    new PurchaseButtonClickListener(cipherNotInvalidated,
////                            KEY_NAME_NOT_INVALIDATED));
//        } else {
//            // Hide the purchase button which uses a non-invalidated key
//            // if the app doesn't work on Android N preview
////            purchaseButtonNotInvalidated.setVisibility(View.GONE);
////            findViewById(R.id.purchase_button_not_invalidated_description).setVisibility(View.GONE);
//        }

//        if (!keyguardManager.isKeyguardSecure()) {
//            // Show a message that the user hasn't set up a fingerprint or lock screen.
////            Toast.makeText(this,
////                    "Secure lock screen hasn't set up.\n"
////                            + "Go to 'Settings -> Security -> Fingerprint' to set up a fingerprint",
////                    Toast.LENGTH_LONG).show();
////            purchaseButton.setEnabled(false);
////            purchaseButtonNotInvalidated.setEnabled(false);
//            return;
//        }

        // Now the protection level of USE_FINGERPRINT permission is normal instead of dangerous.
        // See http://developer.android.com/reference/android/Manifest.permission.html#USE_FINGERPRINT
        // The line below prevents the false positive inspection from Android Studio
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            // noinspection ResourceType
//            if (!fingerprintManager.hasEnrolledFingerprints()) {
////                purchaseButton.setEnabled(false);
//                // This happens when no fingerprints are registered.
////                Toast.makeText(this,
////                        "Go to 'Settings -> Security -> Fingerprint' and register at least one" +
////                                " fingerprint",
////                        Toast.LENGTH_LONG).show();
//                return;
//            }
//        }
//        createKey(DEFAULT_KEY_NAME, true);
//        createKey(KEY_NAME_NOT_INVALIDATED, false);
//        purchaseButton.setEnabled(true);
//        purchaseButton.setOnClickListener(
//                new PurchaseButtonClickListener(defaultCipher, DEFAULT_KEY_NAME));



        amountVal.setText(Singlton.getInstance().getAmount());
        if (!Singlton.getInstance().getCanAccess()) {
            Singlton.getInstance().setCanAccess(true);
        }
    }


    /**
     * change view and message when get correct pincode
     * @param s
     */
    private void checKPinCode(Editable s){
        message_error.setText("");
        Singlton.getInstance().setAmount(s.toString());


            switch (s.length()) {
                case 0:
                    i1.setImageResource(R.drawable.circle_pin_code);
                    i2.setImageResource(R.drawable.circle_pin_code);
                    i3.setImageResource(R.drawable.circle_pin_code);
                    i4.setImageResource(R.drawable.circle_pin_code);
                    break;
                case 1:
                    i1.setImageResource(R.drawable.circle_pin_code);
                    i2.setImageResource(R.drawable.circle_pin_code);
                    i3.setImageResource(R.drawable.circle_pin_code);
                    i4.setImageResource(R.drawable.circle_pin_code2);
                    break;

                case 2:
                    i1.setImageResource(R.drawable.circle_pin_code);
                    i2.setImageResource(R.drawable.circle_pin_code);
                    i3.setImageResource(R.drawable.circle_pin_code2);
                    i4.setImageResource(R.drawable.circle_pin_code2);
                    break;

                case 3:
                    i1.setImageResource(R.drawable.circle_pin_code);
                    i2.setImageResource(R.drawable.circle_pin_code2);
                    i3.setImageResource(R.drawable.circle_pin_code2);
                    i4.setImageResource(R.drawable.circle_pin_code2);

                    break;
                case 4:
                    i1.setImageResource(R.drawable.circle_pin_code2);
                    i2.setImageResource(R.drawable.circle_pin_code2);
                    i3.setImageResource(R.drawable.circle_pin_code2);
                    i4.setImageResource(R.drawable.circle_pin_code2);

                    break;
            }
            if(!isPassPinCode) {
            send.setBackgroundColor(getResources().getColor(R.color.colorConnectButton));
            send.setTextColor(getResources().getColor(R.color.unselected_text));
            currPincode.setText("Enter code "+PIN_CODE);
        }else{
            if (cNumber != null) {
                send.setBackgroundColor(getResources().getColor(R.color.ui_appBar_start));
                send.setTextColor(getResources().getColor(R.color.ui_appBar_light));
                send.setText(Singlton.getInstance().getAmount());
            }else{

                if (s.toString().equals("0")||s.toString().equals("")){

                    send.setBackgroundColor(getResources().getColor(R.color.colorConnectButton));
                    send.setTextColor(getResources().getColor(R.color.unselected_text));
                }else {
                    send.setBackgroundColor(getResources().getColor(R.color.ui_appBar_start));
                    send.setTextColor(getResources().getColor(R.color.ui_appBar_light));
                }
//                currPincode.setAnimation(getBlinkAnimation(500));
            }
        }

    }

    private void setButtonsListeners(){

        ArrayList<LinearLayout> buttons = new ArrayList<LinearLayout>();
        buttons.add((LinearLayout) findViewById(R.id.button0));
        buttons.add((LinearLayout) findViewById(R.id.button1));
        buttons.add((LinearLayout) findViewById(R.id.button2));
        buttons.add((LinearLayout) findViewById(R.id.button3));
        buttons.add((LinearLayout) findViewById(R.id.button4));
        buttons.add((LinearLayout) findViewById(R.id.button5));
        buttons.add((LinearLayout) findViewById(R.id.button6));
        buttons.add((LinearLayout) findViewById(R.id.button7));
        buttons.add((LinearLayout) findViewById(R.id.button8));
        buttons.add((LinearLayout) findViewById(R.id.button9));
        buttons.add((LinearLayout) findViewById(R.id.buttonDot));
        buttons.add((LinearLayout) findViewById(R.id.buttonDel));

        for (LinearLayout button:buttons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if ((cNumber== null)&&(isPassPinCode)) {
//                        currPincode.clearAnimation();
//                        currPincode.setAnimation(getBlinkAnimation(500));
//                    }else {

                        onNumberClicked(v);
//                    }
                }
            });
        }
    }


    public void onNumberClicked(View view){
        if (!(view instanceof LinearLayout)){
            return;
        }
//        view.clearAnimation();
//        view.setAnimation(getBlinkAnimation(150));
        String buttonTxt = ((LinearLayout) view).getTag().toString();

        String amountText = amountVal.getText().toString();
            String newAmount = "";
            if (buttonTxt.equals(DEL)) {
                if (amountText.length() > 0) {
                    if (amountText.length() > 1) {
                        newAmount = amountText.substring(0, amountText.length() - 1);
                    } else {
                        newAmount = ZERO;
                    }
                }
            } else {

                if (amountText.equals(ZERO)) {
                    newAmount = buttonTxt;
                } else {
                    if (buttonTxt.equals(DOT)) {
                        if (amountText.contains(DOT)) {
                            return;
                        }
                    }
                    newAmount = amountText + buttonTxt;
                }
            }
            Singlton.getInstance().setAmount(newAmount);
            amountVal.setText(newAmount);

    }


    @Override
    public void onResume(){
        Log.d("Amount", "onResume");

//        actionManager.resumeListen();

        super.onResume();


        if (Singlton.getInstance().getContactModel()!=null) {
            cNumber = Singlton.getInstance().getContactModel().getNumber();
            textPay.setVisibility(View.VISIBLE);
            updateUI(R.id.currPincode, Singlton.getInstance().getContactModel().getName());
            send.setBackgroundColor(getResources().getColor(R.color.ui_appBar_start));
            send.setText("Pay € " + amountVal.getText().toString());
            currPincode.setTextColor(getResources().getColor(R.color.colorPaygilant));

            send.setTextColor(getResources().getColor(R.color.ui_appBar_light));
            amountVal.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            amountVal.setRawInputType(InputType.TYPE_NULL);



        }
    }
    @Override
    public void onStart(){
        Log.d("Amount", "onStart");
        super.onStart();
    }
    @Override
    public void onPause(){
        Log.d("Amount", "onPause");

        super.onPause();
//        actionManager.pauseListenToSensors();
    }

    @Override
    public void onStop(){
        Log.d("Amount", "onStop");

        super.onStop();
    }

    @Override
    public void onDestroy(){
        Log.d("Amount", "onDestory");
//        actionManager.finishScreenListener();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case( PICK_CONTACT_FROM_ACTIVITY):
                if (resultCode == Activity.RESULT_OK) {
//                    Toast.makeText(this, contactModel.toString(), Toast.LENGTH_LONG).show();
//                    actionManager = paygilantHandler.startNewScreenListener(PaygilantManager.ScreenListenerType.PAYMENT_SCREEN, 4);

                    cNumber = Singlton.getInstance().getContactModel().getNumber();
                    textPay.setVisibility(View.VISIBLE);
                    updateUI(R.id.currPincode,  Singlton.getInstance().getContactModel().getName());
                    send.setBackgroundColor(getResources().getColor(R.color.ui_appBar_start));
                    send.setText("Pay € " +amountVal.getText().toString());
                    currPincode.setTextColor(getResources().getColor(R.color.colorPaygilant));

                    send.setTextColor(getResources().getColor(R.color.ui_appBar_light));
                    amountVal.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
                    amountVal.setRawInputType(InputType.TYPE_NULL);





//                    currPincode.setTextColor(Color.BLACK);
                }

        }
    }

    /**
     * update text view ui with value
     * @param fieldID
     * @param value
     */
    public void updateUI(int fieldID, String value){
        currPincode =  findViewById(fieldID);
        currPincode.setText(value);
    }

    public Animation getBlinkAnimation(int duration){
        Animation animation = new AlphaAnimation(1, 0);         // Change alpha from fully visible to invisible
        animation.setDuration(duration);                             // duration - half a second
        animation.setInterpolator(new LinearInterpolator());    // do not alter animation rate
        animation.setRepeatCount(1);                            // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE);             // Reverse animation at the end so the button will fade back in
        return animation;
    }


    /**
     * when user click on approve send transaction to risk calculating
     * @param view
     */
    public void onApproveClicked(final View view){

        Singlton.getInstance().setAmount(amountVal.getText().toString());
        if (isPassPinCode){
            if (cNumber == null) {
                onChooseContactClicked();

            }else{
//                final KProgressHUD hud = KProgressHUD.create(Amount.this)
//                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);

                isScanFirst = false;
//                cNumber = cNumber.replaceAll("[^0-9]","");
//                if (cNumber.contains("507122881")||cNumber.contains("506700822")){

//                    final Timer timer = new Timer();
//                    final TimerTask task = new TimerTask() {
                Intent intent = new Intent(Amount.this, ResultActivityAmount.class);

                if (Singlton.getInstance().getCanAccess()){
                    intent.putExtra("RISK_RESULT", 0);

                }else {
                    intent.putExtra("RISK_RESULT", 3);
                }
                startActivity(intent);
                finish();

            }
        }else {
            if (amountVal.getText().toString().equals(PIN_CODE)){

            }
        }
    }

    public void onChooseContactClicked(){
//        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//        startActivityForResult(intent, PICK_CONTACT);

        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {



            Intent intent = new Intent(this, ContactActivity.class);
//            startActivityForResult(intent, PICK_CONTACT_FROM_ACTIVITY);
            startActivity(intent);
            finish();
        }else{
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                                Manifest.permission.READ_CONTACTS}
                        , READ_CONTACTS_CALL_PERMISSION);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.back, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.icon_back:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {

//        paygilantHandler = PaygilantManager.getInstance();
//        if (paygilantHandler != null)
//            paygilantHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d("PermissionsResult", "requestCode=" + requestCode + "permissions" + permissions + "grantResults" + grantResults);
    }

    //Abhishek Changes
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        if(cNumber!=null){

            onChooseContactClicked();
            cNumber = null;
        }else {
            Singlton.getInstance().setAmount("0");
            Singlton.getInstance().setContactModel(null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.app_name);
            alertDialogBuilder.setMessage("Do You Want To Exit App?");
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
           /* Intent intent = new Intent(this, Amount.class);
            startActivity(intent);
            finish();*/
        }
    }


    public class PurchaseButtonClickListener implements View.OnClickListener {

        Cipher mCipher;
        String mKeyName;
        int riskLevel;

        PurchaseButtonClickListener(Cipher cipher, String keyName) {
            mCipher = cipher;
            mKeyName = keyName;
        }

        @Override
        public void onClick(View view) {

        }
    }

    void saveImage(String pthAndFylTtlVar, Bitmap iptBmjVar)
    {
        try
        {
            FileOutputStream fylBytWrtrVar = new FileOutputStream(pthAndFylTtlVar);
            iptBmjVar.compress(Bitmap.CompressFormat.PNG, 100, fylBytWrtrVar);
            fylBytWrtrVar.close();
        }
        catch (Exception errVar) { errVar.printStackTrace(); }
    }
    Bitmap getImage(String pthAndFylTtlVar)
    {
        return BitmapFactory.decodeFile(pthAndFylTtlVar);
    }


}