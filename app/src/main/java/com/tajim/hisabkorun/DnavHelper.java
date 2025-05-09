package com.tajim.hisabkorun;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DnavHelper extends AppCompatActivity {
    public static TextView name_dnav;
    Toolbar tool_dnav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dnav_helper);
        name_dnav = findViewById(R.id.name_dnav);
        tool_dnav = findViewById(R.id.tool_dnav);

        setSupportActionBar(tool_dnav);
        tool_dnav.setNavigationOnClickListener(v->{
            onBackPressed();
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_dnav, new BugFragment());
        fragmentTransaction.commit();



    }
}