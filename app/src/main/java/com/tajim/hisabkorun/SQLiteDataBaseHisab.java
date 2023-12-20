package com.tajim.hisabkorun;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class SQLiteDataBaseHisab extends SQLiteOpenHelper {


    public static final String dataBaseName = "Hisab_korun_DB";
    public static final int dataBaseVersion = 1;


    public SQLiteDataBaseHisab(Context context) {
        super(context, dataBaseName, null, dataBaseVersion);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE hisab_table (ID INTEGER primary key autoincrement, name_db Text, paona_db INTEGER , dena_db INTEGER, mobile_db INTEGER, color_db INTEGER )");

        sqLiteDatabase.execSQL("CREATE TABLE details_table (ID INTEGER primary key autoincrement, serial INTEGER, pelam INTEGER, dilam INTEGER, result INTEGER, date TEXT, biboron TEXT, name_dl TEXT)");


    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists hisab_table");
        sqLiteDatabase.execSQL("drop table if exists details_table");

    }


    public int insertDataSQL(String name, int paona, int dena, int mobile, int color) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name_db",name);
        contentValues.put("paona_db",paona);
        contentValues.put("dena_db",dena);
        contentValues.put("mobile_db",mobile);
        contentValues.put("color_db",color);
        long pos = database.insert("hisab_table",null, contentValues);
        int rowIdAsInt = (int) pos;


        return rowIdAsInt;


    }

    public Cursor getDataSQL (){

        SQLiteDatabase database = this.getReadableDatabase();


        Cursor cursor = database.rawQuery("SELECT * FROM hisab_table",null);


        return cursor;
    }

    public Cursor srcDataSQLbyName(String name){
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM hisab_table where name_db like '"+name+"'",null);

        return cursor;

    }

    public Cursor srcDataSQLbyNameApproximate(String name){
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM hisab_table where name_db like '%"+name+"%'",null);

        return cursor;

    }
    public void editDataSQLbyID(int id , int paona, int dena ){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("UPDATE hisab_table SET paona_db= paona_db +'" + paona + "', dena_db= dena_db+ '" + dena + "' WHERE ID like '" + id + "'");

        database.close();

    }

    public void updateDataSQl(int id, String name,int mobile){

        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("UPDATE hisab_table SET name_db= '" + name + "', mobile_db='" + mobile + "' WHERE ID like '" + id + "'");

        database.close();


    }
    public void makeToast (Context context,String body ){
        Toast.makeText(context, body,Toast.LENGTH_SHORT).show();
    }

    public void deleteDataSQLbyID(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM hisab_table where ID like '"+id+"'");


    }

    public void updateHistory(int serial, int result, int pelam, int dilam, String date, String biboron, String name ){

        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

            contentValues.put("pelam", pelam);
            contentValues.put("dilam", dilam);
            contentValues.put("date", date);
            contentValues.put("result", result);
            contentValues.put("serial", serial);
            contentValues.put("biboron", biboron);
            contentValues.put("name_dl", name);

            database.insert("details_table",null, contentValues);



    }

    public Cursor getResultSQL (int serial){
        SQLiteDatabase database = this.getReadableDatabase();


        Cursor cursor = database.rawQuery("SELECT * FROM details_table where serial like '"+serial+"' ",null);






        return cursor;
    }

    public Cursor getFullHistoryBYId (Boolean byid, int id ){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor;
        if (byid == true){


         cursor = database.rawQuery("SELECT * FROM details_table where serial like '"+id+"' ORDER BY id DESC",null);
        }else {
             cursor = database.rawQuery("SELECT * FROM details_table ORDER BY id DESC",null);

        }


        return cursor;


    }

    public Cursor getFullHistoryBYIdASC (Boolean byid, int id ){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor;
        if (byid == true){


            cursor = database.rawQuery("SELECT * FROM details_table where serial like '"+id+"' ",null);
        }else {
            cursor = database.rawQuery("SELECT * FROM details_table ",null);

        }


        return cursor;


    }

    public void clearAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("details_table", null, null); // replace with your table name
        db.delete("hisab_table", null, null); // replace with your table name
        db.close();
    }









}
