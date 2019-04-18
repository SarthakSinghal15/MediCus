package com.medicus;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class PatientTodaySchedule extends AppCompatActivity {

    private ArrayList<String> medNames,medTimes;
    RecyclerView recyclerView;
    public static SQLiteHelper sqLiteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_today_schedule);

        medNames = new ArrayList<>();
        medTimes = new ArrayList<>();

        getTodaysMedicines();
        setRecyclerView();
    }

    private void getTodaysMedicines()
    {
        // code to get today's medicines
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        sqLiteHelper = new SQLiteHelper(getApplicationContext(),"UserDB.sqlite",null,1);
        Log.i("resultforcursor","executed ddfdf");
        String sql = "SELECT medname,hour,minute FROM PRESCRIPTION WHERE day ="+day;
        Cursor c = sqLiteHelper.getData(sql);
        Log.i("resultforcursor","executed" + c.getCount());
        if(c.moveToFirst())
        {
            do {
                medNames.add(c.getString(0));
                medTimes.add(c.getInt(1)+":"+c.getInt(2));


            }
            while (c.moveToNext());
        }
        else
        {
            medNames.add("No Medicines for today");
            medTimes.add("--:--");
        }

//        medNames.add("Crocine");
//        medTimes.add("9:00 AM");
//
//        medNames.add("Paracetamol");
//        medTimes.add("12:00 PM");
//
//        medNames.add("Combiflame");
//        medTimes.add("3:00 PM");
//
//        medNames.add("Oncet CF");
//        medTimes.add("6:00 PM");
//
//        medNames.add("B-Capsule");
//        medTimes.add("9:00 PM");

    }

    private void setRecyclerView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.rv_todaymeds);
        MedicineRecyclerViewAdapter recyclerViewAdapter = new MedicineRecyclerViewAdapter(this,medNames,medTimes);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
