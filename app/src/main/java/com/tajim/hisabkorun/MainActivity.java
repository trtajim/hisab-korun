package com.tajim.hisabkorun;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    TextInputEditText edMoneyPaona1, edMoneyDena1, edMobile1, details_ed_main, edName1;
    TextInputLayout edMoneyPaona1H, edMoneyDena1H, edMobile1H, details_ed_mainH, edName1H;

    Button btn1;
    Toolbar toolbar_main;
    TextView txt_main;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    BottomSheetDialog sheetDialog;
    SQLiteDataBaseHisab sqLiteDataBaseHisab;

    @Override
    public void onBackPressed() {


        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edMoneyPaona1 = findViewById(R.id.edMoneyPaona1);
        edMoneyPaona1H = findViewById(R.id.edMoneyPaona1H);
        edMoneyDena1H = findViewById(R.id.edMoneyDena1H);
        edMobile1H = findViewById(R.id.edMobile1H);
        details_ed_mainH = findViewById(R.id.details_edit_mainH);
        edName1H = findViewById(R.id.edName1H);
        edMoneyDena1 = findViewById(R.id.edMoneyDena1);
        edName1 = findViewById(R.id.edName1);
        edMobile1 = findViewById(R.id.edMobile1);
        txt_main = findViewById(R.id.txt_main);
        btn1 = findViewById(R.id.btn1);
        details_ed_main = findViewById(R.id.details_edit_main);
        sqLiteDataBaseHisab = new SQLiteDataBaseHisab(this);
        toolbar_main = findViewById(R.id.toolbar_main);
        sheetDialog = new BottomSheetDialog(this);
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        createdialouge(this);

        setSupportActionBar(toolbar_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar_main.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        if (HomeActivity.mainCon.equals("edit")){
            txt_main.setText("এডিট করুন ");

            edName1.setText(HomeFragment.name_home);
            edMobile1.setText("0"+HomeFragment.mobile_home);
            edMoneyDena1H.setVisibility(View.GONE);
            edMoneyPaona1H.setVisibility(View.GONE);
            details_ed_mainH.setVisibility(View.GONE);
            btn1.setText("Edit");
            btn1.setOnClickListener(v->{

                String name = edName1.getText().toString();
                String mobile = edMobile1.getText().toString();
                if (name.length()!= 0 && mobile.length()!=0){


                    if (mobile.startsWith("01") && mobile.length()==11){

                    }else {
                        sqLiteDataBaseHisab.makeToast(this, "Enter a valid phone number");
                        return;
                    }

                }else {
                    sqLiteDataBaseHisab.makeToast(this,"সকল তথ্য দিন " );
                    return;
                }
                Cursor cursor = sqLiteDataBaseHisab.srcDataSQLbyName(name);
                if (cursor != null && cursor.moveToFirst()) {
                    String namee = cursor.getString(1);
                    if (name.equals(namee)){
                        showDiaouge(this, ()->{
                            sqLiteDataBaseHisab.updateDataSQl(HomeFragment.id_home, edName1.getText().toString(), Integer.parseInt(edMobile1.getText().toString()));
                            sqLiteDataBaseHisab.makeToast(this, "Updated successfully");
                            startActivity(new Intent(this, HomeActivity.class));
                            finish();


                        });

                    }else {
                        sqLiteDataBaseHisab.makeToast(this, "দুঃখিত , এই নামটি আগে থেকেই রয়েছে ");
                    }


                }else{}
            });



        }else if (HomeActivity.mainCon.equals("edit_pass")){
            txt_main.setText("পাসওয়ার্ড পরিবর্তন");
            edMoneyPaona1H.setVisibility(View.GONE);
            edName1H.setVisibility(View.GONE);
            details_ed_mainH.setVisibility(View.GONE);

            edMoneyDena1H.setHint("New Password");;
            edMoneyDena1H.setStartIconDrawable(null);
            edMobile1H.setHint("Confirm new password");
            edMobile1H.setHelperTextEnabled(false);
            edMobile1H.setCounterEnabled(false);
            edMobile1H.setPrefixText("");
            edMobile1H.setStartIconDrawable(null);

            btn1.setOnClickListener(v->{

                String pass = edMoneyDena1.getText().toString();
                String new_pass = edMobile1.getText().toString();
                if (pass.length()>3){

                    if (pass.equals(new_pass)){
                        showDiaouge(this, ()->{
                            editor.putString("pass_usr",pass);
                            editor.apply();
                            LoginActivity.pass_usr = pass;


                            sqLiteDataBaseHisab.makeToast(this, "Pass changed successfully");
                            startActivity(new Intent(this, HomeActivity.class));
                            finish();

                        });
                    }else {
                        edMobile1.requestFocus();
                        edMobile1.setError("Pass not matched");
                    }


                }else{
                    edMoneyDena1.requestFocus();
                    edMoneyDena1.setError("Min. 4 Character");
                }
            });




        }else {

            btn1.setOnClickListener(v -> {
                String spaona = edMoneyPaona1.getText().toString();
                String sdena = edMoneyDena1.getText().toString();
                String name = edName1.getText().toString();
                String mobile = edMobile1.getText().toString();
                int paona, dena;
                if (name.length() != 0 ) {
                    //    if (mobile.length() == 0 || (mobile.startsWith("01") && mobile.length() == 11)) {
                    if (mobile.length() == 0 || (mobile.startsWith("01") && mobile.length() == 11)){

                    }else {
                        sqLiteDataBaseHisab.makeToast(this, "Enter a valid phone number");
                        return;
                    }
                    if (spaona.length() != 0 && sdena.length() != 0) {
                        paona = Integer.parseInt(spaona);
                        dena = Integer.parseInt(sdena);

                    } else if (sdena.length() != 0 && spaona.length() == 0) {
                        dena = Integer.parseInt(sdena);
                        paona = 0;

                    } else if (spaona.length() != 0 && sdena.length() == 0) {
                        paona = Integer.parseInt(spaona);
                        dena = 0;


                    } else {
                        sqLiteDataBaseHisab.makeToast(this, "সকল তথ্য দিন ");
                        return;
                    }
                } else {
                    sqLiteDataBaseHisab.makeToast(this, "সকল তথ্য দিন ");
                    return;
                }
                showDiaouge(this, () -> {

                    Cursor cursor = sqLiteDataBaseHisab.srcDataSQLbyName(name);
                    if (cursor != null && cursor.moveToFirst()) {
                        sqLiteDataBaseHisab.makeToast(this, "দুঃখিত , এই নামটি আগে থেকেই রয়েছে ");
                    } else {
                        Random rnd = new Random();
                        int minValue = 5; // Minimum value to avoid light colors
                        int red = minValue +1+ rnd.nextInt(256 - minValue);
                        int green = minValue +3+ rnd.nextInt(256 - minValue);
                        int blue = minValue +2+ rnd.nextInt(256 - minValue);
                        int color = Color.argb(255, red, green, blue);
                        int pos;
                        if (mobile.length()!=0){
                             pos = sqLiteDataBaseHisab.insertDataSQL(name, paona, dena, Integer.parseInt(mobile), color);
                        }else {
                             pos = sqLiteDataBaseHisab.insertDataSQL(name, paona, dena, 0, color);


                        }

                        sqLiteDataBaseHisab.makeToast(this, "সম্পন্ন ");

                        long currentTimeMillis = System.currentTimeMillis();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm a", Locale.getDefault());
                        String formattedTime = sdf.format(new Date(currentTimeMillis));

                        sqLiteDataBaseHisab.updateHistory(pos ,  paona-dena,dena ,paona,formattedTime, details_ed_main.getText().toString(), name);
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }



                });


            });

        }


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
                ed_bd.setError("ভুল পাসওয়ার্ড ");


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
                    ed_bd.setError("ভুল পাসওয়ার্ড ");


                }

            }
        });


    }

}