package com.tajim.hisabkorun;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class SeeDetailsActivity extends AppCompatActivity {
    SQLiteDataBaseHisab sqLiteDataBaseHisab;
    RecyclerView recycler_details;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String,String> hashMap;
    Toolbar tool_see_Details;
    TextView name_See_Details;
    TextView name_lyy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_details);
        recycler_details = findViewById(R.id.recycler_see_details);
        tool_see_Details = findViewById(R.id.tool_see_Details);
        name_See_Details = findViewById(R.id.name_See_details);
        name_lyy = findViewById(R.id.name_lyy);
        sqLiteDataBaseHisab= new SQLiteDataBaseHisab(this);



        setSupportActionBar(tool_see_Details);
        tool_see_Details.setNavigationOnClickListener(v->{
            onBackPressed();
        });

        Cursor cursor;
        if(HomeActivity.allHistory == false){
            name_See_Details.setText("All transactions with "+HomeFragment.name_home);
             cursor = sqLiteDataBaseHisab.getFullHistoryBYId(true,ActivityDetails.id_dtl);
        }else {
            name_See_Details.setText("All transactions");
             cursor = sqLiteDataBaseHisab.getFullHistoryBYId(false,ActivityDetails.id_dtl);
        }



        while (cursor.moveToNext()){
            int pelam  = cursor.getInt(2);
            int dilam = cursor.getInt(3);
            int result = cursor.getInt(4);
            String date = cursor.getString(5);
            String biboron = cursor.getString(6);
            String name = cursor.getString(7);
            if (biboron.length()<1) biboron = "";

            hashMap = new HashMap<>();
            hashMap.put("pelam",""+pelam);
            hashMap.put("dilam",""+dilam);
            hashMap.put("result",""+result);
            hashMap.put("date",""+date);
            hashMap.put("biboron",""+biboron);
            hashMap.put("name",""+name);
            arrayList.add(hashMap);

        }

        MyAdapter myAdapter = new MyAdapter();
        recycler_details.setAdapter(myAdapter);
        recycler_details.setLayoutManager(new LinearLayoutManager(this));





    }
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewholder>{
        TextView date_ly, pelam_ly, dilam_ly, pabo_ly, dibo_ly, details_ly, name_ly;
        private class myViewholder extends RecyclerView.ViewHolder{

            public myViewholder(@NonNull View itemView) {
                super(itemView);
                date_ly = itemView.findViewById(R.id.date_ly);
                pelam_ly = itemView.findViewById(R.id.pelam_ly);
                dilam_ly = itemView.findViewById(R.id.dilam_ly);
                pabo_ly = itemView.findViewById(R.id.pabo_ly);
                dibo_ly = itemView.findViewById(R.id.dibo_ly);
                details_ly = itemView.findViewById(R.id.details_ly);
                name_ly = itemView.findViewById(R.id.name_ly);

            }
        }
        @NonNull
        @Override
        public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View myview = getLayoutInflater().inflate(R.layout.layout_see_details,parent,false);
            return new myViewholder(myview);
        }

        @Override
        public void onBindViewHolder(@NonNull myViewholder holder, int position) {

             HashMap<String,String> hashMapp = arrayList.get(position);
            String datee = hashMapp.get("date");
            String pelamm = hashMapp.get("pelam");
            String dilamm = hashMapp.get("dilam");
            String resultt = hashMapp.get("result");
            String biboronn = hashMapp.get("biboron");
            String namee = hashMapp.get("name");
            date_ly.setText(datee);
            if (HomeActivity.allHistory == true){

                name_ly.setVisibility(View.VISIBLE);
                name_lyy.setVisibility(View.VISIBLE);
                name_ly.setText(namee);
            }else {
                name_ly.setVisibility(View.GONE);
                name_lyy.setVisibility(View.GONE);
            }
            pelam_ly.setText(pelamm);
            dilam_ly.setText(dilamm);
            details_ly.setText(biboronn);
            if (Integer.parseInt(resultt) <0){
                dibo_ly.setText(""+resultt);
                pabo_ly.setText("0");



            }else {

                pabo_ly.setText(""+resultt);
                dibo_ly.setText("0");

            }
            Log.d("RecyclerView", "ArrayList size: " + arrayList.size());




        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }


    }

    @Override
    public void onBackPressed() {
        if (HomeActivity.allHistory==true){
            startActivity(new Intent(this, HomeActivity.class));
            finish();


        }else {
            super.onBackPressed();

        }
    }
}