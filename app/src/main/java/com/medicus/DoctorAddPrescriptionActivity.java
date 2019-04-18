package com.medicus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoctorAddPrescriptionActivity extends AppCompatActivity {

    @BindView(R.id.input_patient_id) EditText _patientId;
    @BindView(R.id.input_medicine_name) EditText _medicineName;
    @BindView(R.id.input_duration) EditText _duration;
    @BindView(R.id.checkBoxMonday) CheckBox _mondayCheck;
    @BindView(R.id.checkBoxTuesday) CheckBox _tuesdayCheck;
    @BindView(R.id.checkBoxWednesday) CheckBox _weddayCheck;
    @BindView(R.id.checkBoxThursday) CheckBox _thursdayCheck;
    @BindView(R.id.checkBoxFriday) CheckBox _fridayCheck;
    @BindView(R.id.checkBoxSaturday) CheckBox _satdayCheck;
    @BindView(R.id.checkBoxSunday) CheckBox _sundayCheck;
    @BindView(R.id.btn_addPre) Button _addButton;
    @BindView(R.id.timePicker) TimePicker _tp;
    public static SQLiteHelper sqLiteHelper;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //UserSessionManager obj = new UserSessionManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_add_prescription);
        ButterKnife.bind(this);

        _addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PutDatatoFirestore();
                login();
            }
        });
    }

    private void PutDatatoFirestore() {
        if (!validate()) {
            onPrescibeFailed();
            return;
        }
        _addButton.setEnabled(false);
        int pid = Integer.parseInt(_patientId.getText().toString());
        int did = UserSessionManager.userid;
        String medicineName = _medicineName.getText().toString();
        String duration = _duration.getText().toString();
        int hour = _tp.getHour();
        int minute = _tp.getMinute();
        Map<String, Object> data=  new HashMap<>();
        data.put("pid", pid);
        data.put("did", did);
        data.put("medicine name", medicineName);
        data.put("Duration", duration);
        data.put("hour", hour);
        data.put("minute", minute);
        if(_mondayCheck.isChecked()){
            data.put("day", 1);
            db.collection("Prescriptions").document().set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("***********LOGS*****","data has been saved");
                }
            });

        }
        if(_tuesdayCheck.isChecked()){
            data.put("day", 2);
            db.collection("Prescriptions").document().set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("***********LOGS*****","data has been saved");
                }
            });
        }
        if(_weddayCheck.isChecked()){
            data.put("day", 3);
            db.collection("Prescriptions").document().set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("***********LOGS*****","data has been saved");
                }
            });
        }
        if(_thursdayCheck.isChecked()){
            data.put("day", 4);
            db.collection("Prescriptions").document().set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("***********LOGS*****","data has been saved");
                }
            });
        }
        if(_fridayCheck.isChecked()){
            data.put("day", 5);
            db.collection("Prescriptions").document().set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("***********LOGS*****","data has been saved");
                }
            });
        }
        if(_satdayCheck.isChecked()){
            data.put("day", 6);
            db.collection("Prescriptions").document().set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("***********LOGS*****","data has been saved");
                }
            });
        }
        if(_sundayCheck.isChecked()) {
            data.put("day", 7);
            db.collection("Prescriptions").document().set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("***********LOGS*****", "data has been saved");
                }
            });
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void login() {

        if (!validate()) {
            onPrescibeFailed();
            return;
        }

        Log.i("Result","atleast reaching the login");

        _addButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(DoctorAddPrescriptionActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding Prescription...");
        progressDialog.show();

        int pid = Integer.parseInt(_patientId.getText().toString());

        int did=UserSessionManager.userid;
        String medicineName = _medicineName.getText().toString();
        String duration = _duration.getText().toString();
        int hour = _tp.getHour();
        int minute = _tp.getMinute();

//        sqLiteHelper = new SQLiteHelper(getApplicationContext(),"UserDB.sqlite",null,1);
//        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS PRESCRIPTION(presId INTEGER PRIMARY KEY AUTOINCREMENT, patientID INTEGER,doctorID INTEGER, day INTEGER,duration VARCHAR,hour INTEGER,minute INTEGER,medname VARCHAR)");
//
//        Log.i("Result","atleast database is being created");
//        if(_mondayCheck.isChecked()){
//            Log.i("Result","atleast monday is working");
//            sqLiteHelper.insertPrescriptionData(pid,did,1,duration,hour,minute,medicineName);
//            Log.i("Result","atleast monday is working");
//
//        }
//        if(_tuesdayCheck.isChecked()){
//            sqLiteHelper.insertPrescriptionData(pid,did,2,duration,hour,minute,medicineName);
//        }
//        if(_weddayCheck.isChecked()){
//            sqLiteHelper.insertPrescriptionData(pid,did,3,duration,hour,minute,medicineName);
//        }
//        if(_thursdayCheck.isChecked()){
//            sqLiteHelper.insertPrescriptionData(pid,did,4,duration,hour,minute,medicineName);
//        }
//        if(_fridayCheck.isChecked()){
//            sqLiteHelper.insertPrescriptionData(pid,did,5,duration,hour,minute,medicineName);
//        }
//        if(_satdayCheck.isChecked()){
//            sqLiteHelper.insertPrescriptionData(pid,did,6,duration,hour,minute,medicineName);
//        }
//        if(_sundayCheck.isChecked()){
//            sqLiteHelper.insertPrescriptionData(pid,did,7,duration,hour,minute,medicineName);
//        }


        Toast.makeText(getApplicationContext(),"The Prescription has successfully added",Toast.LENGTH_LONG).show();



            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            onPrescibeSuccess();
                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);

    }

    public void onPrescibeFailed() {
        Toast.makeText(getBaseContext(), "Couldn't add the prescription", Toast.LENGTH_LONG).show();

        _addButton.setEnabled(true);
    }

    public void onPrescibeSuccess() {
        _addButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), DoctorDashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public boolean validate() {
        boolean valid = true;

        String pid = _patientId.getText().toString();
        String medicineName = _medicineName.getText().toString();
        String duration = _duration.getText().toString();

        if (medicineName.isEmpty() || medicineName.length() < 3) {
            _medicineName.setError("at least 3 characters");
            valid = false;
        } else {
            _medicineName.setError(null);
        }

        if (pid.isEmpty()) {
            _patientId.setError("Enter Valid Patient ID");
            valid = false;
        } else {
            _patientId.setError(null);
        }

        if (duration.isEmpty()) {
            _duration.setError("Enter Valid Duration");
            valid = false;
        } else {
            _duration.setError(null);
        }

        return valid;
    }
}
