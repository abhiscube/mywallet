package com.paygilant.mywallet.Contact;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;


import com.paygilant.mywallet.OnWallet.Amount;

import com.paygilant.mywallet.R;

import com.paygilant.mywallet.OnWallet.Singlton;


import java.util.ArrayList;

import static com.paygilant.mywallet.MainActivity.forceLTRSupported;


public class ContactActivity extends AppCompatActivity {

    private ListView listView;
    private CustomAdapter customAdapter;
    private ArrayList<ContactModel> contactModelArrayList;
    private SearchView searchViewContact;
  //  PaygilantScreenListener actionManager;
//    private PaygilantManager paygilantHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ui_appBar_start)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        forceLTRSupported(this);

        listView = (ListView) findViewById(R.id.listView);
        searchViewContact = (SearchView)findViewById(R.id.searchViewContact);
        contactModelArrayList = new ArrayList<>();

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            ContactModel contactModel = new ContactModel(name,phoneNumber);
            contactModelArrayList.add(contactModel);
            Log.d("name>>",name+"  "+phoneNumber);
        }
        phones.close();
        customAdapter = new CustomAdapter(this,contactModelArrayList);
        if (customAdapter.getCount()>0) {
            listView.setAdapter(customAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Singlton.getInstance().setContactModel(contactModelArrayList.get(position));

                    Intent intent = new Intent(ContactActivity.this,Amount.class);
                    startActivity(intent);
//                        Intent intent = new Intent();
//                        intent.putExtra("result", 2);
//
//                        setResult(RESULT_OK, intent);
                        //           actionManager.finishScreenListener();
                        finish();
                }
            });
        }

//        paygilantHandler = PaygilantManager.getInstance();
   //     actionManager = paygilantHandler.startNewScreenListener(PaygilantManager.ScreenListenerType.CONTACTS,5);
        View v = findViewById(R.id.activity_contact);

        if (!Singlton.getInstance().getCanAccess()) {

            Singlton.getInstance().setCanAccess(true);

        }


        searchViewContact.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

// do something on text submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                customAdapter.filter(text);
// do something when text changes
                return false;
            }
        });
    //    actionManager.setTouchToAllChildren(v);
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
    public void onResume(){
        Log.d("Amount", "onResume");

   //     actionManager.resumeListen();

        super.onResume();
    }
    @Override
    public void onPause(){
        Log.d("Amount", "onPause");

        super.onPause();
  //      actionManager.pauseListenToSensors();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Singlton.getInstance().setAmount("0");
        Singlton.getInstance().setContactModel(null);
        Intent intent = new Intent(ContactActivity.this,Amount.class);
        startActivity( intent);
        finish();
    }



}