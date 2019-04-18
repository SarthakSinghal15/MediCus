package com.medicus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoctorDashboardActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    TextView did, dname, daddr, dcontact;
    UserSessionManager session;


    @BindView(R.id.txt_doctorName) TextView _dnameText;
    @BindView(R.id.txt_phoneNo) TextView _dphoneText;
    @BindView(R.id.txt_hospitalAddr) TextView _daddressText;
    @BindView(R.id.btn_addPatient) Button _addPButton;
    @BindView(R.id.btn_viewPatient) Button _viewPButton;
    @BindView(R.id.btn_approvePatient) Button _approvePButton;
    @BindView(R.id.btn_addMedicine) Button _addMedicineButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);
        ButterKnife.bind(this);

        session = new UserSessionManager(getApplicationContext());

        did = (TextView) findViewById(R.id.txt_doctorID);
        dname = (TextView) findViewById(R.id.txt_doctorName);
        daddr = (TextView) findViewById(R.id.txt_hospitalAddr);
        dcontact= (TextView) findViewById(R.id.txt_phoneNo);

        loadProfile();

        _addPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),DoctorAddPatientActivity.class);
                startActivity(intent);
                //finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _addMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),DoctorAddPrescriptionActivity.class);
                startActivity(intent);
                //finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _viewPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),DoctorViewPatient.class);
                startActivity(intent);
                //finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");


//        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
//                R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();

//        String name = _nameText.getText().toString();
//        String address = _addressText.getText().toString();
//        String email = _emailText.getText().toString();
//        String mobile = _mobileText.getText().toString();
//        String password = _passwordText.getText().toString();
//        String reEnterPassword = _reEnterPasswordText.getText().toString();
//        String emergencyContact = _emergencyText.getText().toString();
//        String emergencyContactMobile = _emergencyMobileText.getText().toString();

        // TODO: Implement your own signup logic here.

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onSignupSuccess or onSignupFailed
//                        // depending on success
//                        //onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }

    private void loadProfile()
    {
        HashMap<String, String> user = session.getUserDetails();

        did.setText(user.get(UserSessionManager.U_ID));
        dname.setText(user.get(UserSessionManager.U_NAME));
        dcontact.setText(user.get(UserSessionManager.U_ECONTACT));
        daddr.setText(user.get(UserSessionManager.U_ADDR));
    }



}