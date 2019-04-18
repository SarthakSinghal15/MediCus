package com.medicus;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
    SQLiteDatabase database = getWritableDatabase();
    database.execSQL(sql);
    }

    public void insertData(String name, String pasword){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO USERS VALUES (NULL, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,name);
        statement.bindString(2,pasword);
        statement.executeInsert();
    }

    public void insertPrescriptionData(int patientID, int doctorID, String day, String duration, int hour, int minute, String medname){
        Log.i("Result","atleast query is about to be executed");
        SQLiteDatabase database = getWritableDatabase();
        Log.i("Result","1");
        String sql = "INSERT INTO PRESCRIPTION VALUES (NULL, ?, ?, ?,?,?,?,?)";
        Log.i("Result","2");
        SQLiteStatement statement = database.compileStatement(sql);
        Log.i("Result","3");
        statement.clearBindings();
        Log.i("Result","4");
        statement.bindLong(1,patientID);
        statement.bindLong(2,doctorID);
        statement.bindString(3, day);
        statement.bindString(4, duration);
        statement.bindLong(5,hour);
        statement.bindLong(6,minute);
        statement.bindString(7, medname);
        statement.executeInsert();
        Log.i("Result","atleast quesry is being executed");
    }

    public  void deleteData() {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM PRESCRIPTION";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.execute();
        database.close();
    }



    public ArrayList getPrescription(){
        ArrayList<Integer> list_of_ids = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT presid FROM PRESCRIPTION";
        Cursor cursor = database.rawQuery(query,null);
        int a;
        //String b;
        //b="not found";
        if(cursor.moveToFirst())
        {
            do {
                a=cursor.getInt(0);
                list_of_ids.add(a);

            }
            while (cursor.moveToNext());
        }
        return list_of_ids;

    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
