package com.paygilant.mywallet;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import com.paygilant.mywallet.OnWallet.Amount;
import com.paygilant.mywallet.OnWallet.Singlton;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
//    PaygilantManager paygilantHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ui_appBar_light)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        forceLTRSupported(this);
    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {

//        paygilantHandler = PaygilantManager.getInstance();
//        if (paygilantHandler != null)
//            paygilantHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d("PermissionsResult", "requestCode=" + requestCode + "permissions" + permissions + "grantResults" + grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void forceLTRSupported(AppCompatActivity activity)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

}