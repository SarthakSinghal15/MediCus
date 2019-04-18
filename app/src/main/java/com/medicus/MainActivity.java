package com.medicus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new UserSessionManager(getApplicationContext());

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(!session.isUserLoggedIn()) {
                            //navigate to login activity

                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            //HashMap<String, String> user = session.getUserDetails();

                            //String userType = session.getUserType();

                            if (session.getUserType().equals("Doctor")) {
                                //navigate to doctor dashboard

                                Intent intent = new Intent(MainActivity.this, DoctorDashboardActivity.class);
                                //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                                finish();
                            } else {
                                //navigate to patient dashboard

                                Intent intent = new Intent(MainActivity.this, PatientDashboardActivity.class);
                                //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }, 3000);

        /*

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
                */
    }

}
