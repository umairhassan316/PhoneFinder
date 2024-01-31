package com.findphone.whistleclapfinder.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.findphone.whistleclapfinder.R;
import com.findphone.whistleclapfinder.SetionClasss.LanguageHelper;
import com.findphone.whistleclapfinder.SetionClasss.MyPrefsOnBoardingScreen;

public class SplashActivity extends AppCompatActivity {

    // Define other variables as needed

    TextView continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        continueBtn = findViewById(R.id.continueBtn);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

               boolean onBoardingIsChecked = MyPrefsOnBoardingScreen.getMyPrefsOnBoardingScreen(SplashActivity.this);

               if (!onBoardingIsChecked){
                   Intent i = new Intent(SplashActivity.this, OnBoardingActivity.class);
                   startActivity(i);
               }else {


                   // check below sestion check all permission allow or not
                   SharedPreferences prefs = getSharedPreferences("AllPermissionAllowSharedPreferences", Context.MODE_PRIVATE);
                   boolean allPermissionAllowBooleanSharedPreferences = prefs.getBoolean("AllPermissionAllowBooleanSharedPreferences", false);

                   if (allPermissionAllowBooleanSharedPreferences) {
                       Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                       startActivity(i);

                       // Load the saved language
                       LanguageHelper.loadLocale(SplashActivity.this);

                   } else {
                       Intent i = new Intent(SplashActivity.this, PermissionActivity.class);
                       startActivity(i);
                   }



               }





                finish();

            }


        }, 2000);




    }


}
