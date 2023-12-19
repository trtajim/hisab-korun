package com.tajim.hisabkorun;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class SearchFragment extends Fragment {
    ArrayList <HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String,String> hashMap ;
    SQLiteDataBaseHisab sqLiteDataBaseHisab;
    ListView list_src_frg;
    ImageView img_src_frg;
    TextView txt_src_frg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        sqLiteDataBaseHisab = new SQLiteDataBaseHisab(getActivity());
        list_src_frg = view.findViewById(R.id.list_src_frg);
        img_src_frg = view.findViewById(R.id.no_data_img_src_frg);
        txt_src_frg = view.findViewById(R.id.empty_txt__src_frg);
        MyAdapter myAdapter = new MyAdapter();
        HomeActivity.src_ed_home.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayList = new ArrayList<>();

                Cursor cursor = sqLiteDataBaseHisab.srcDataSQLbyNameApproximate(charSequence.toString());
                if (cursor.getCount() != 0) {
                    img_src_frg.setVisibility(View.GONE);
                    txt_src_frg.setText("");
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
                            hashMap = new HashMap<>();
                            hashMap.put("name",name );
                            hashMap.put("money", String.valueOf(inresult));
                            hashMap.put("status","dena");
                            hashMap.put("pos", String.valueOf(position));
                            hashMap.put("mobile", String.valueOf(mobilee));
                            hashMap.put("color", String.valueOf(color));
                            arrayList.add(hashMap);



                        }else if (paona> dena){
                            inresult = paona - dena;
                            hashMap = new HashMap<>();
                            hashMap.put("name",name );
                            hashMap.put("money", String.valueOf(inresult));
                            hashMap.put("status","paona");
                            hashMap.put("pos", String.valueOf(position));
                            hashMap.put("mobile", String.valueOf(mobilee));
                            hashMap.put("color", String.valueOf(color));
                            arrayList.add(hashMap);

                        }else {
                            inresult = 0;
                            hashMap = new HashMap<>();
                            hashMap.put("name",name );
                            hashMap.put("money", String.valueOf(inresult));
                            hashMap.put("status","paona");
                            hashMap.put("pos", String.valueOf(position));
                            hashMap.put("mobile", String.valueOf(mobilee));
                            hashMap.put("color", String.valueOf(color));
                            arrayList.add(hashMap);



                        }





                    }

                    list_src_frg.setAdapter(myAdapter);



                }else {
                    img_src_frg.setVisibility(View.VISIBLE);
                    txt_src_frg.setText("কোনো তথ্য পাওয়া যায়নি । তথ্য খুঁজে পেতে সঠিক বানানে সার্চ করুন ");

                }
                cursor.close();
                myAdapter.notifyDataSetChanged();








            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;

    }
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
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







            HashMap<String, String> hashMap = arrayList.get(i);
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
                if (status.equals("paona")) HomeFragment.statuss = "paona";
                else {HomeFragment.statuss = "dena";}
                HomeFragment.name_home = name;
                HomeFragment.first_letter_home = firstLetter;
                HomeFragment.money_home = Integer.parseInt(money);
                HomeFragment.id_home = Integer.parseInt(posi);
                HomeFragment.mobile_home = Integer.parseInt(mobi);



                startActivity(new Intent(getActivity(), ActivityDetails.class));
                getActivity().finish();
            });


            return view1;
        }
}
}