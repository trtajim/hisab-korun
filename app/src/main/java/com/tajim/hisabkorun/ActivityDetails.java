package com.tajim.hisabkorun;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ActivityDetails extends AppCompatActivity {
    TextView name_details, money_details,  circle_txt_dlt, number_details, txt_date_details;
    TextInputEditText dilam_edit_details, pelam_edit_details, details_edit_details;
    Button btn_details;
    int paona_details, dena_details;
    Toolbar toolbar_dlt;
    Context context = ActivityDetails.this;
    SQLiteDataBaseHisab sqLiteDataBaseHisab;
    BottomSheetDialog sheetDialog;
    int mobile_number = HomeFragment.mobile_home;
    String msg_body;
    CheckBox chk_msg_dtl;
    HomeActivity homeActivity;
    public static int id_dtl;
    RelativeLayout op1_dtl;



    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityDetails.this, HomeActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dlt_tool, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();

        if (item_id == R.id.dlt_tool) {
            snackbar("delete", "Canceled", ()->{
                showDiaouge(this, () -> {
                    sqLiteDataBaseHisab.deleteDataSQLbyID(HomeFragment.id_home);
                    onBackPressed();
                    sqLiteDataBaseHisab.makeToast(this, "Done");
                });

            });

        } else if (item_id == R.id.dtl_tool) {
            HomeActivity.allHistory = false;
            id_dtl = HomeFragment.id_home;
            startActivity(new Intent(this, SeeDetailsActivity.class));

        } else if (item_id == R.id.msg_tool){

            sendsms();

        }else {


            sqLiteDataBaseHisab.makeToast(this, "edit");
            HomeActivity.mainCon = "edit";
            startActivity(new Intent(this, MainActivity.class));
            finish();

        }

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        btn_details = findViewById(R.id.btn_details);
        name_details = findViewById(R.id.name_details);
        money_details = findViewById(R.id.money_details);
        pelam_edit_details = findViewById(R.id.pelam_edit_details);
        dilam_edit_details = findViewById(R.id.dilam_edit_details);
        circle_txt_dlt = findViewById(R.id.circle_txt_dtl);
        txt_date_details = findViewById(R.id.txt_date_details);
        toolbar_dlt = findViewById(R.id.toolbar_details);
        chk_msg_dtl = findViewById(R.id.chk_msg_dtl);
        number_details = findViewById(R.id.number_details);
        details_edit_details = findViewById(R.id.details_edit_details);
        op1_dtl = findViewById(R.id.op_1_dtl);
        op1_dtl.setOnClickListener(v->{
            chk_msg_dtl.setChecked(true);
        });
        homeActivity = new HomeActivity();
        sheetDialog = new BottomSheetDialog(this);


        txt_date_details.setText(HomeFragment.date_home);
        if (HomeFragment.statuss!= null) {
            if (HomeFragment.statuss.equals("paona")) {

                money_details.setText("To receive: " + HomeFragment.money_home + "৳");
                msg_body = "'According to the records of hisab korun, " + LoginActivity.name_usr + " still has an outstanding amount of " + HomeFragment.money_home + "৳ from you. Please make the payment at your earliest convenience.'";

            } else {

                money_details.setText("To pay: " + HomeFragment.money_home + "৳");
                msg_body = "'According to the records of 'Hisab Korun', you will receive " + HomeFragment.money_home + "৳ from " + LoginActivity.name_usr;


            }
        }else {

            startActivity(new Intent(this, HomeActivity.class));
        }
        createdialouge(this);
        chk_msg_dtl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chk_msg_dtl.isChecked()) {
                    askSms();


                } else {
                }
            }
        });


        sqLiteDataBaseHisab = new SQLiteDataBaseHisab(this);
        String mobileN = String.valueOf(HomeFragment.mobile_home);
        name_details.setText(HomeFragment.name_home);
        if (mobileN.length()== 10){
            number_details.setText("0"+HomeFragment.mobile_home);
        }else {}



        circle_txt_dlt.setText(HomeFragment.first_letter_home);



        setSupportActionBar(toolbar_dlt);


        toolbar_dlt.setNavigationOnClickListener(v -> {
            onBackPressed();
        });


        btn_details.setOnClickListener(v -> {
            String pelam = dilam_edit_details.getText().toString();
            String dilam = pelam_edit_details.getText().toString();
            if (pelam.length() != 0 && dilam.length() != 0) {
                paona_details = Integer.parseInt(pelam);
                dena_details = Integer.parseInt(dilam);

            } else if (dilam.length() != 0 && pelam.length() == 0) {
                dena_details = Integer.parseInt(dilam);
                paona_details = 0;

            } else if (pelam.length() != 0 && dilam.length() == 0) {
                paona_details = Integer.parseInt(pelam);
                dena_details = 0;

            } else {
                sqLiteDataBaseHisab.makeToast(this, "Please enter all information");
                return;
            }

            showDiaouge(this, () -> {


                if (chk_msg_dtl.isChecked()) {
                    sendsms();
                } else {
                }

                sqLiteDataBaseHisab.makeToast(this, "done ");

                sqLiteDataBaseHisab.editDataSQLbyID(HomeFragment.id_home, paona_details, dena_details);
                Cursor cursor = sqLiteDataBaseHisab.getResultSQL(HomeFragment.id_home);
                int result = 0;
                while (cursor.moveToNext()){
                     result =  cursor.getInt(4);
                }
                long currentTimeMillis = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm a", Locale.getDefault());
                String formattedTime = sdf.format(new Date(currentTimeMillis));
                sqLiteDataBaseHisab.updateHistory(HomeFragment.id_home,  result+(paona_details-dena_details), dena_details, paona_details , formattedTime, details_edit_details.getText().toString(), HomeFragment.name_home);
                startActivity(new Intent(this, HomeActivity.class));
                finish();

            });


        });


    }

    private void createdialouge(Context context) {
        View view = getLayoutInflater().inflate(R.layout.bottom_dialouge, null, false);
        EditText ed_bd = view.findViewById(R.id.ed_bd);
        Button btn_bd = view.findViewById(R.id.btn_bd);

        btn_bd.setOnClickListener(v -> {
            if (ed_bd.getText().toString().equals(LoginActivity.pass_usr)) {
                sheetDialog.dismiss();

            } else {
                sheetDialog.dismiss();
                ed_bd.setError("Wrong Password");


            }

        });
        sheetDialog.setContentView(view);
    }

    private void showDiaouge(Context contexts, Runnable onSuccess) {
        sheetDialog.show();

        View view = getLayoutInflater().inflate(R.layout.bottom_dialouge, null);
        sheetDialog.setContentView(view);
        EditText ed_bd = view.findViewById(R.id.ed_bd);
        Button btn_bd = view.findViewById(R.id.btn_bd);
        ed_bd.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(ed_bd, InputMethodManager.SHOW_IMPLICIT);
        btn_bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = ed_bd.getText().toString();

                if (password.equals(LoginActivity.pass_usr)) {
                    sheetDialog.dismiss();
                    onSuccess.run();
                } else {

                    ed_bd.setError("Wrong Password");


                }

            }
        });


    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                } else {

                }
            });


    private void sendsms() {


        String numberr = "" + 0 + mobile_number;
        String body = msg_body;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) ==
                    PackageManager.PERMISSION_GRANTED) {
                if (numberr.length()!=11){
                    Toast.makeText(this, "You haven't added number, message send failed",Toast.LENGTH_SHORT).show();

                }else{
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    ArrayList<String> messageParts = smsManager.divideMessage(body);
                    smsManager.sendMultipartTextMessage(numberr, null, messageParts, null, null);
                    sqLiteDataBaseHisab.makeToast(context, "Message sent !");

                } catch (Exception e) {
                    sqLiteDataBaseHisab.makeToast(context, "failed to send message" + e.getMessage());
                    Log.e("SMS Error", e.getMessage());
                    e.printStackTrace();
                }}
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.SEND_SMS)) {

                snackbar("sms_ask","you are unable to send message", ()->{
                    requestPermissionLauncher.launch(android.Manifest.permission.SEND_SMS);

                });

            } else {
                snackbar("sms", "you are unable to send message",()->{

                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:com.tajim.hisabkorun"));
                    startActivity(intent);
                    sqLiteDataBaseHisab.makeToast(ActivityDetails.this, "Permissions > SMS > Allow");


                });
                requestPermissionLauncher.launch(android.Manifest.permission.SEND_SMS);
            }
        }

    }

    private void askSms() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) ==
                    PackageManager.PERMISSION_GRANTED) {

            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.SEND_SMS)) {

                snackbar("sms_ask","you are unable to send message", ()->{
                    requestPermissionLauncher.launch(android.Manifest.permission.SEND_SMS);

                });



            } else {
                snackbar("sms", "you are unable to send message",()->{
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:com.tajim.hisabkorun"));
            startActivity(intent);
            sqLiteDataBaseHisab.makeToast(ActivityDetails.this, "Permissions > SMS > Allow");


                        });
                requestPermissionLauncher.launch(android.Manifest.permission.SEND_SMS);
            }
        }

    }

    private void snackbar(String status, String toast, Runnable runnable){

        View alertLayout = getLayoutInflater().inflate(R.layout.alert_custom, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        chk_msg_dtl.setChecked(false);
                    }
                });
        alertDialogBuilder.setView(alertLayout);
        AlertDialog alertDialog = alertDialogBuilder.create();
        int dpWidth = 320;
        float density = this.getResources().getDisplayMetrics().density;
        int widthInPixels = (int) (dpWidth * density);
        alertDialog.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = widthInPixels;
        alertDialog.getWindow().setAttributes(layoutParams);
        TextView allow_cus = alertLayout.findViewById(R.id.allow_cus);
        TextView decline_cus = alertLayout.findViewById(R.id.decline_cus);
        LottieAnimationView lottieAnimationView = alertLayout.findViewById(R.id.animationView);

        TextView text_cus = alertLayout.findViewById(R.id.txt_cus);
        if (status.equals("delete")){
            lottieAnimationView.setAnimation(R.raw.delete_3);
            lottieAnimationView.playAnimation();
            lottieAnimationView.loop(true);

            int widthInDp = 90;
            int heightInDp = 90;
            float scale = getResources().getDisplayMetrics().density; // Get the screen density
            int widthInPx = (int) (widthInDp * scale + 0.5f);  // Convert dp to pixels
            int heightInPx = (int) (heightInDp * scale + 0.5f);  // Convert dp to pixels

            ViewGroup.LayoutParams params = lottieAnimationView.getLayoutParams();
            params.width = widthInPx;  // Set the width in pixels
            params.height = heightInPx; // Set the height in pixels
            lottieAnimationView.setLayoutParams(params);
            text_cus.setText("Are you sure to delete? you won't be able to recover it");
            allow_cus.setText("Delete");
        }else if (status.equals("sms_ask")){
            text_cus.setText("To send sms, allow hisab korun to access sms services in the next popup");

        }

        allow_cus.setOnClickListener(v -> {
            chk_msg_dtl.setChecked(false);
            runnable.run();

            alertDialog.dismiss();
        });

        decline_cus.setOnClickListener(v -> {
            alertDialog.dismiss();
            sqLiteDataBaseHisab.makeToast(ActivityDetails.this, toast);
            chk_msg_dtl.setChecked(false);
        });
    }
}