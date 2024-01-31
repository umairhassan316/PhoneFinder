package com.findphone.whistleclapfinder.activity;

import static com.findphone.whistleclapfinder.SetionClasss.MyPrefsExtensionMainSetting.addMyPrefsExtensionMainSetting;
import static com.findphone.whistleclapfinder.SetionClasss.MyPrefsUseForMainSettingFlashLightModeSelect.addMyPrefsUseForMainSettingFlashLightModeSelect;
import static com.findphone.whistleclapfinder.SetionClasss.MyPrefsVibrationModeMainSetting.addMyPrefsMyPrefsVibrationModeMainSetting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.findphone.whistleclapfinder.R;
import com.findphone.whistleclapfinder.SetionClasss.LanguageHelper;


public class PermissionActivity extends AppCompatActivity {


    TextView nextBtn;
    private static final int REQUEST_MICKPHONE_PERMISSION = 1;
    private static final int REQUEST_WRITE_SETTINGS = 1;
    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 111;

    TextView audioBtn,mickPhoneBtn,overlayBtn;
    ImageView audioTickIcon,mickPhoneTickIcon,overlayTickIcon;
    boolean overlayIsChecked = false;
    boolean mickPhonesChecked = false;
    boolean audioChecked = false;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);




            nextBtn = findViewById(R.id.nextBtn);
            audioBtn = findViewById(R.id.audioBtn);
            mickPhoneBtn = findViewById(R.id.mickPhoneBtn);
            overlayBtn = findViewById(R.id.overlayBtn);
            overlayTickIcon = findViewById(R.id.overlayTickIcon);
            mickPhoneTickIcon = findViewById(R.id.mickPhoneTickIcon);
            audioTickIcon = findViewById(R.id.audioTickIcon);



        if (!Settings.canDrawOverlays(PermissionActivity.this)) {

            Log.d("overlaypermission","overlay permission is not allowed 4");
//            overlay_switch.setChecked(false);
            overlayTickIcon.setVisibility(View.GONE);
            overlayBtn.setVisibility(View.VISIBLE);

            overlayIsChecked = false;

        }else {
            Log.d("overlaypermission","overlay permission is allowed 4");
//            overlay_switch.setChecked(true);
            overlayTickIcon.setVisibility(View.VISIBLE);
            overlayBtn.setVisibility(View.GONE);

            overlayIsChecked = true;
        }

        overlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (!Settings.canDrawOverlays(PermissionActivity.this)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));

                        startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
                        Log.d("overlaypermission","overlay permission is not allowed 2");
                    }


            }
        });



        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {

            Log.d("checkonOnDisturblResult", "Permission is not granted. Opening settings...  onCreate");

            audioTickIcon.setVisibility(View.GONE);
            audioBtn.setVisibility(View.VISIBLE);
            audioChecked = false;

        }else {
            Log.d("checkonOnDisturblResult", "Permission is already granted.");
//            audio_switch.setChecked(true);

            audioTickIcon.setVisibility(View.VISIBLE);
            audioBtn.setVisibility(View.GONE);
            audioChecked = true;
        }


        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {

                    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    startActivity(intent);

                    Log.d("checkonOnDisturblResult", "Permission is not granted. Opening settings... audio_switch True Button");


                }


            }
        });



        /*this microphone permission this actually microphone permission*/


        boolean microPhonePermissionGranted = checkMicrophonePermission();

        if (!microPhonePermissionGranted) {

            Log.d("checkonOnDisturblResult", "Mickro Phone Permission is not granted");
//            microPhone_switch.setChecked(false);

            mickPhoneTickIcon.setVisibility(View.GONE);
            mickPhoneBtn.setVisibility(View.VISIBLE);


            mickPhonesChecked = false;

        }else {
            Log.d("checkonOnDisturblResult", "Bluetooth Permission is granted");
//            microPhone_switch.setChecked(true);

            mickPhoneTickIcon.setVisibility(View.VISIBLE);
            mickPhoneBtn.setVisibility(View.GONE);

            mickPhonesChecked = true;

        }



        mickPhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!microPhonePermissionGranted) {
                    requestMicroPhonePermission();

                    Log.d("checkonOnDisturblResult", "Brightness Permission is not granted");
                }else {
                    Log.d("checkonOnDisturblResult", "Brightness Permission is  granted");
                }


            }
        });




        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                audioChecked = true; // temparary set because Itel device inside not this permission fun
//                overlayIsChecked = true; // temparary set because Itel device inside not this permission fun

                if (overlayIsChecked && mickPhonesChecked &&  audioChecked){
                    Log.d("checkonOnDisturblResult","All Permission is Allow");

                    Intent intent = new Intent(PermissionActivity.this, HomeActivity.class);
                    startActivity(intent);

                    SharedPreferences prefs = getSharedPreferences("AllPermissionAllowSharedPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("AllPermissionAllowBooleanSharedPreferences",true);
                    editor.commit();



                    // below setion this setting into use extation screen light on off set if true then when start ring tune then screen on if false then screen not on use this SettingActivity
                    addMyPrefsExtensionMainSetting(PermissionActivity.this,true);

                    // ADD_FLASH_LIGHT_MODE_NAME  this class MyPrefsUseForMainSettingFlashLightModeSelect
                    addMyPrefsUseForMainSettingFlashLightModeSelect(PermissionActivity.this,"default");

                    // ADD_VIBRATION_MODE_NAME  this class MyPrefsVibrationModeMainSetting
                    addMyPrefsMyPrefsVibrationModeMainSetting(PermissionActivity.this,"defaultVibration");


                    // Set By Defualt First Time Language
//                        addMyPrefsLanguage(PermissionActivity.this,"en");
//                        setLocaleLanguage(PermissionActivity.this,"en");
                    LanguageHelper.setLocale(PermissionActivity.this,"en");



                }else {
                    Log.d("checkonOnDisturblResult","All Permission is not Allow");
                    Toast.makeText(PermissionActivity.this,"First Check All Permission",Toast.LENGTH_SHORT).show();
                }



            }
        });


    }


    private void requestMicroPhonePermission() {

        Log.d("checkblutoothpermission","requestMicroPhonePermission()");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MICKPHONE_PERMISSION);
    }


    private boolean checkMicrophonePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_MICKPHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Permission denied, disable the switch
                Log.d("sdfsdere11","Permission denied, disable the switch");

                mickPhoneTickIcon.setVisibility(View.GONE);
                mickPhoneBtn.setVisibility(View.VISIBLE);
                mickPhonesChecked = false;
            } else {
                // Permission granted

                // Perform MICKPHONE-related actions
                Log.d("sdfsdere11","Perform MICKPHONE-related actions");

                mickPhoneTickIcon.setVisibility(View.VISIBLE);
                mickPhoneBtn.setVisibility(View.GONE);
                mickPhonesChecked = true;
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("checkonOnDisturblResult", "onRestart");


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {

            Log.d("checkonOnDisturblResult", "Permission is not granted. Opening settings... onRestart  ");

//            audio_switch.setChecked(false);

            audioTickIcon.setVisibility(View.GONE);
            audioBtn.setVisibility(View.VISIBLE);


             audioChecked = false;


        }else {
            Log.d("checkonOnDisturblResult", "Permission is already granted.");
//            audio_switch.setChecked(true);

            audioTickIcon.setVisibility(View.VISIBLE);
            audioBtn.setVisibility(View.GONE);
            audioChecked = true;




        }


        if (!Settings.canDrawOverlays(PermissionActivity.this)) {

            Log.d("overlaypermission","overlay permission is not allowed 4");
//            overlay_switch.setChecked(false);
            overlayTickIcon.setVisibility(View.GONE);
            overlayBtn.setVisibility(View.VISIBLE);

             overlayIsChecked = false;

        }else {
            Log.d("overlaypermission","overlay permission is allowed 4");
//            overlay_switch.setChecked(true);
            overlayTickIcon.setVisibility(View.VISIBLE);
            overlayBtn.setVisibility(View.GONE);

            overlayIsChecked = true;
        }


        /*this microphone permission this actually microphone permission*/


        boolean microPhonePermissionGranted = checkMicrophonePermission();

        if (!microPhonePermissionGranted) {

            Log.d("checkonOnDisturblResult", "Bluetooth Permission is not granted");
//            microPhone_switch.setChecked(false);

            mickPhoneTickIcon.setVisibility(View.GONE);
            mickPhoneBtn.setVisibility(View.VISIBLE);
             mickPhonesChecked = false;

        }else {
            Log.d("checkonOnDisturblResult", "Bluetooth Permission is granted");
//            microPhone_switch.setChecked(true);

            mickPhoneTickIcon.setVisibility(View.VISIBLE);
            mickPhoneBtn.setVisibility(View.GONE);

            mickPhonesChecked = true;

        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("checkonOnDisturblResult", "onResume");
    }
}