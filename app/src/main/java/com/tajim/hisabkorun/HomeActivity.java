package com.tajim.hisabkorun;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {
    Toolbar toolbar_home;
    public static EditText src_ed_home;
    FrameLayout frame_home;
    Boolean src_open = false;
    MenuItem searchMenuItem;
    DrawerLayout main;
    SQLiteDataBaseHisab sqLiteDataBaseHisab;
    NavigationView navigationView;
    public static Boolean allHistory = false;
    public static String mainCon = "";
    View headerview;
    TextView circle_txt_dnav, txt_dnav;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        askSms();
        src_ed_home = findViewById(R.id.src_Ed_home);
        frame_home = findViewById(R.id.frame_home);
        toolbar_home = findViewById(R.id.toolbar_home);
        navigationView = findViewById(R.id.navigationViewHome);
        headerview = navigationView.getHeaderView(0);
        circle_txt_dnav = headerview.findViewById(R.id.circle_txt_dnav);
        txt_dnav = headerview.findViewById(R.id.txt_dnav);
        txt_dnav.setText(LoginActivity.name_usr);
        int themeColor = ContextCompat.getColor(this, R.color.theme);

        if (circle_txt_dnav.getBackground() instanceof GradientDrawable) {
            ((GradientDrawable) circle_txt_dnav.getBackground()).setColor(themeColor);
        } else {
            circle_txt_dnav.getBackground().setColorFilter(themeColor, PorterDuff.Mode.SRC_IN);
        }

        circle_txt_dnav.setText(LoginActivity.name_usr.substring(0, 1).toUpperCase());

        main = findViewById(R.id.main);
        sqLiteDataBaseHisab = new SQLiteDataBaseHisab(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_home, new HomeFragment());
        fragmentTransaction.commit();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, main, toolbar_home, R.string.closedrawer, R.string.opendraw);
        main.addDrawerListener(toggle);
        toggle.syncState();



        toolbar_home.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                searchMenuItem = item;

                if (src_ed_home.getVisibility() == View.GONE) {
                    src_open = true;
                    src_ed_home.setVisibility(View.VISIBLE);
                    toolbar_home.setTitle("");
                    toolbar_home.setNavigationIcon(null);
                    item.setIcon(R.drawable.baseline_cancel_24);
                    src_ed_home.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(src_ed_home.findFocus(), InputMethodManager.SHOW_IMPLICIT);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_home, new SearchFragment());
                    fragmentTransaction.commit();



                } else {

                    src_open = false;
                    src_ed_home.setVisibility(View.GONE);
                    toolbar_home.setTitle("Hisab Korun");
                    src_ed_home.setText("");
                    toolbar_home.setNavigationIcon(R.drawable.baseline_menu_24);
                    item.setIcon(R.drawable.baseline_search_24);
                    src_ed_home.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(src_ed_home.getWindowToken(), 0);
                    }
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_home, new HomeFragment());
                    fragmentTransaction.commit();



                }
                return false;
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if (item.getItemId() == R.id.trans_history){

                    allHistory = true;
                    startActivity(new Intent(HomeActivity.this, SeeDetailsActivity.class));
                    finish();
                    main.closeDrawer(GravityCompat.START);


                } else if (item.getItemId() == R.id.cng_pass) {
                    mainCon = "edit_pass";
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                    finish();
                    main.closeDrawer(GravityCompat.START);
                } else if (item.getItemId()==R.id.report_bug){
                    startActivity(new Intent(HomeActivity.this, DnavHelper.class));
                    main.closeDrawer(GravityCompat.START);
                }else if (item.getItemId()==R.id.contact_us) {

                    showCustomAlert(R.raw.email_red,"Want to contact us? Select a platform to continue","Linkedin","Github", ()->{
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/trtajim"));
                        startActivity(myIntent);

                    },()->{
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https:www.github.com/trtajim"));
                        startActivity(myIntent);

                    });
                }else if (item.getItemId()==R.id.log_out) {
                    showCustomAlert(R.raw.exit,"Are you sure you want to log out? by proceeding, all of your data will be deleted","Log out","Not now", ()->{
                        SharedPreferences sharedPreferences;
                        SharedPreferences.Editor editor;
                        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putInt("first",0);
                        editor.apply();
                        sqLiteDataBaseHisab.clearAllData();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        },()->{});

                    main.closeDrawer(GravityCompat.START);
                } else if (item.getItemId()==R.id.privacy_policy) {
                    Toast.makeText(HomeActivity.this, "Privacy Policy", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.trtajim.xyz/apps/hisab_korun/privacy_policy.html"));
                    startActivity(myIntent);
                } else if (item.getItemId()==R.id.terms_conditions) {
                    Toast.makeText(HomeActivity.this, "Terms and Conditions", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.trtajim.xyz/apps/hisab_korun/terms_conditions.html"));
                    startActivity(myIntent);
                }else {}
                return true;
            }
        });


    }
    private void showCustomAlert(int anim,String smessage, String sallowbutton, String sdecline_button, Runnable rallow, Runnable rdecline) {
        View alertLayout = getLayoutInflater().inflate(R.layout.alert_custom, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(alertLayout)
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        main.closeDrawer(GravityCompat.START);
                    }
                })
                .create();

        int widthInPixels = (int) (320 * getResources().getDisplayMetrics().density);
        alertDialog.show();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = widthInPixels;
        alertDialog.getWindow().setAttributes(layoutParams);

        TextView allowButton = alertLayout.findViewById(R.id.allow_cus);
        TextView declineButton = alertLayout.findViewById(R.id.decline_cus);
        LottieAnimationView animationView = alertLayout.findViewById(R.id.animationView);
        TextView messageText = alertLayout.findViewById(R.id.txt_cus);

        // Set up animation and layout params
        animationView.setAnimation(anim);
        animationView.playAnimation();
        animationView.loop(true);

        // Set width using RelativeLayout.LayoutParams
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) animationView.getLayoutParams();
        params.width = widthInPixels;
        animationView.setLayoutParams(params);


        messageText.setText(smessage);

        allowButton.setText(sallowbutton);
        declineButton.setText(sdecline_button);

        // Button click listeners
        allowButton.setOnClickListener(v -> {

            rallow.run();
            alertDialog.dismiss();

        });

        declineButton.setOnClickListener(v -> {

            rdecline.run();
            alertDialog.dismiss();
        });
    }
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                } else {

                }
            });
    private void askSms() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) ==
                    PackageManager.PERMISSION_GRANTED) {

            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.SEND_SMS)) {






            } else {

                requestPermissionLauncher.launch(android.Manifest.permission.SEND_SMS);
            }
        }

    }



    @Override
    public void onBackPressed() {
        if (src_open == true){
            src_open = false;
            src_ed_home.setVisibility(View.GONE);
            toolbar_home.setTitle("Hisab Korun");
            toolbar_home.setNavigationIcon(R.drawable.baseline_menu_24);
            if (searchMenuItem != null) {
                searchMenuItem.setIcon(R.drawable.baseline_search_24);
            }
            src_ed_home.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(src_ed_home.getWindowToken(), 0);
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_home, new HomeFragment());
            fragmentTransaction.commit();



        }else {
            showCustomAlert(R.raw.exit,"Are you sure you want to exit? There are many features left to explore","Exit","Not now", ()->{finishAndRemoveTask();},()->{});
        }


    }

}

