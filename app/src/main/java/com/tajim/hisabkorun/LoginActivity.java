package com.tajim.hisabkorun;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;



public class LoginActivity extends AppCompatActivity {
    EditText name_ed_login , pass_ed_login, con_pass_ed_login;
    Button btn_login;
    SQLiteDataBaseHisab sqLiteDataBaseHisab;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static String pass_usr = "";
    public static String name_usr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        name_ed_login  = findViewById(R.id.name_ed_login);
        pass_ed_login  = findViewById(R.id.pass_ed_login);
        btn_login = findViewById(R.id.btn_login);
        con_pass_ed_login  = findViewById(R.id.con_pass_ed_login);
        sqLiteDataBaseHisab = new SQLiteDataBaseHisab(this);
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        pass_usr = sharedPreferences.getString("pass_usr","");
        name_usr = sharedPreferences.getString("name_usr","");


        int first = sharedPreferences.getInt("first",0);
        if (first == 1 ){
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }else {

            btn_login.setOnClickListener(v->{


                String name = name_ed_login.getText().toString();
                String pass = pass_ed_login.getText().toString();
                String con_pass = con_pass_ed_login.getText().toString();

                if (name.length() != 0 && pass.length()!=0 && con_pass.length() != 0){

                    if (pass.length() < 4) {
                        pass_ed_login.requestFocus();
                        pass_ed_login.setError("Min. 4 characters");
                    }else {

                        if (pass.equals(con_pass)){
                            editor.putString("name_usr", name);
                            editor.putString("pass_usr",pass);

                            editor.putInt("first",1);
                            editor.apply();
                            pass_usr = pass;
                            name_usr = name;
                            startActivity(new Intent(this, HomeActivity.class));
                            finish();

                        }else {con_pass_ed_login.requestFocus();
                            con_pass_ed_login.setError("Password not matched");}

                    }


                }else {
                    sqLiteDataBaseHisab.makeToast(this, "Please enter all information");
                }


            });

        }




    }

    }
