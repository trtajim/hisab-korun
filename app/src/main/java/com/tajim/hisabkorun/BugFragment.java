package com.tajim.hisabkorun;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class BugFragment extends Fragment {

    EditText ed_bug;
    Button positive_btn_bug, negative_btn_bug;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_bug, container, false);
        DnavHelper.name_dnav.setText("Bug Report");
        ed_bug = view.findViewById(R.id.ed_bug);
        positive_btn_bug = view.findViewById(R.id.btn_bug);
        negative_btn_bug = view.findViewById(R.id.cancel_button_bug);

        positive_btn_bug.setOnClickListener(v->{

            String body = ed_bug.getText().toString();
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.setPackage("com.google.android.gm"); // Gmail package name
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@trtajim.xyz"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "bug detected"+" com.tajim.hisabkorun");
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);

            try {
                startActivity(emailIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "No email client installed.", Toast.LENGTH_SHORT).show();
            }




        });
        negative_btn_bug.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();
        });


        return view;
    }
}