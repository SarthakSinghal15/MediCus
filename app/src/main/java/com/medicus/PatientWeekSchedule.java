package com.medicus;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PatientWeekSchedule extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner dayofweek;
    private ArrayList<String> medNames,medTimes;
    RecyclerView recyclerView;
    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_week_schedule);

        dayofweek = (Spinner) findViewById(R.id.spn_dayofweek);

        // Spinner click listener
        dayofweek.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> weekdays = new ArrayList<String>();

        weekdays.add("Monday");
        weekdays.add("Tuesday");
        weekdays.add("Wednesday");
        weekdays.add("Thursday");
        weekdays.add("Friday");
        weekdays.add("Saturday");
        weekdays.add("Sunday");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, weekdays);

        // Drop down layout style – list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        dayofweek.setAdapter(dataAdapter);

        medNames = new ArrayList<>();
        medTimes = new ArrayList<>();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        // On selecting a spinner item
        String day = parent.getItemAtPosition(position).toString();

        getDaysMedicines(position+1);
        setRecyclerView();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Showing medicines schedule for " + day, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0)
    {
        // TODO Auto-generated method stub
    }

    private void getDaysMedicines(int day)
    {
        // code to get today's medicines
        medNames.clear();
        medTimes.clear();
        //Calendar calendar = Calendar.getInstance();
        sqLiteHelper = new SQLiteHelper(getApplicationContext(),"UserDB.sqlite",null,1);
        Log.i("resultforcursor","executed     :    "+day);
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
        recyclerView = (RecyclerView) findViewById(R.id.rv_daymeds);
        MedicineRecyclerViewAdapter recyclerViewAdapter = new MedicineRecyclerViewAdapter(this,medNames,medTimes);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
