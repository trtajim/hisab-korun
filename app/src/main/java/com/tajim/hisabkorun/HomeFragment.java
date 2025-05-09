package com.tajim.hisabkorun;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class HomeFragment extends Fragment {

    LottieAnimationView img1;
    TextView txt_paona1, txt_dena1,  empty_txt_home;
    ListView recycler_1;
    HashMap<String,String> hashMap = new HashMap<>();
    ArrayList<HashMap<String,String>> arrayListhisab = new ArrayList<>();
    ImageView no_data_img_home;
    Cursor cursor;
    SQLiteDataBaseHisab sqLiteDataBaseHisab;
    int totaldena = 0;
    int totalpaona = 0;
    public static String name_home, first_letter_home, date_home;
    public static int money_home;
    public static String statuss;
    public static int id_home, mobile_home;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        img1 = view.findViewById(R.id.img1);
        txt_paona1 = view.findViewById(R.id.txt_paona1);
        txt_dena1 = view.findViewById(R.id.txt_dena1);
        empty_txt_home = view.findViewById(R.id.empty_txt_home);
        recycler_1 = view.findViewById(R.id.recycler_1);
        no_data_img_home = view.findViewById(R.id.no_data_img_home);
        sqLiteDataBaseHisab = new SQLiteDataBaseHisab(getActivity());
        arrayListhisab = new ArrayList<>();

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HomeActivity.mainCon="";
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });


        cursor = sqLiteDataBaseHisab.getDataSQL();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()){
                String name  = cursor.getString(1);
                int paona = cursor.getInt(2);
                int dena = cursor.getInt(3);
                int position = cursor.getInt(0);
                int mobilee = cursor.getInt(4);
                int color = cursor.getInt(5);
                int inresult;


                if (dena > paona){
                    inresult = dena-paona;

                    totaldena = totaldena+inresult;
                    hashMap = new HashMap<>();
                    hashMap.put("name",name );
                    hashMap.put("money", String.valueOf(inresult));
                    hashMap.put("status","dena");
                    hashMap.put("pos", String.valueOf(position));
                    hashMap.put("mobile", String.valueOf(mobilee));
                    hashMap.put("color", String.valueOf(color));
                    arrayListhisab.add(hashMap);



                }else if (paona> dena){
                    inresult = paona - dena;

                    totalpaona = totalpaona+inresult;

                    hashMap = new HashMap<>();
                    hashMap.put("name",name );
                    hashMap.put("money", String.valueOf(inresult));
                    hashMap.put("status","paona");
                    hashMap.put("pos", String.valueOf(position));
                    hashMap.put("mobile", String.valueOf(mobilee));
                    hashMap.put("color", String.valueOf(color));
                    arrayListhisab.add(hashMap);

                }else {

                    inresult = 0;


                    hashMap = new HashMap<>();
                    hashMap.put("name",name );
                    hashMap.put("money", String.valueOf(inresult));
                    hashMap.put("status","paona");
                    hashMap.put("pos", String.valueOf(position));
                    hashMap.put("mobile", String.valueOf(mobilee));
                    hashMap.put("color", String.valueOf(color));
                    arrayListhisab.add(hashMap);



                }





            }

            txt_paona1.setText(totalpaona+"৳");
            txt_dena1.setText(totaldena+"৳");

        }else {

            empty_txt_home.setVisibility(View.VISIBLE);
            no_data_img_home.setVisibility(View.VISIBLE);


        }
        cursor.close();





        MyAdapter myAdapter = new MyAdapter();
        recycler_1.setAdapter(myAdapter);





        return view;
    }
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayListhisab.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view1 = layoutInflater.inflate(R.layout.layout, null);

            RelativeLayout main_layout;
            TextView circle_txt_lay, name_txt_lay, money_txt_layout, dena_txt, paona_txt;
            circle_txt_lay = view1.findViewById(R.id.circle_txt_lay);
            name_txt_lay = view1.findViewById(R.id.name_txt_lay);
            money_txt_layout = view1.findViewById(R.id.money_txt_layout);
            dena_txt = view1.findViewById(R.id.dena_txt);
            paona_txt = view1.findViewById(R.id.paona_txt);
            main_layout = view1.findViewById(R.id.main_layout);







            HashMap<String, String> hashMap = arrayListhisab.get(i);
            String name = hashMap.get("name");
            String money = hashMap.get("money");
            String status = hashMap.get("status");
            String posi = hashMap.get("pos");
            String mobi = hashMap.get("mobile");
            String color = hashMap.get("color");

            if (circle_txt_lay.getBackground() instanceof GradientDrawable) {
                ((GradientDrawable) circle_txt_lay.getBackground()).setColor(Integer.parseInt(color));
            } else {
                circle_txt_lay.getBackground().setColorFilter(Integer.parseInt(color), PorterDuff.Mode.SRC_IN);
            }


            name_txt_lay.setText(name);
            money_txt_layout.setText(money+"৳");
            String firstLetter = name.substring(0, 1).toUpperCase();
            circle_txt_lay.setText(firstLetter);

            if (status.equals("paona")) {
                paona_txt.setVisibility(View.VISIBLE);
                dena_txt.setVisibility(View.INVISIBLE);

            }else {
                dena_txt.setVisibility(View.VISIBLE);
                paona_txt.setVisibility(View.INVISIBLE);

            }

            main_layout.setOnClickListener( v-> {
                Cursor cursor1= sqLiteDataBaseHisab.getFullHistoryBYIdASC(true, Integer.parseInt(posi));
                while (cursor1.moveToNext()){

                    date_home = cursor1.getString(5);


                }

                if (status.equals("paona")) statuss = "paona";
                else {statuss = "dena";}
                name_home = name;
                first_letter_home = firstLetter;
                money_home = Integer.parseInt(money);
                id_home = Integer.parseInt(posi);
                mobile_home = Integer.parseInt(mobi);



                startActivity(new Intent(getActivity(), ActivityDetails.class));
                getActivity().finish();
            });


            return view1;
        }
    }

}





