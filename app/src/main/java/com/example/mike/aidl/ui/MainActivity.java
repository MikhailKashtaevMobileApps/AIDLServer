package com.example.mike.aidl.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mike.aidl.R;
import com.example.mike.aidl.repository.PersonDBService;
import com.example.mike.aidl.repository.local.LocalDataStore;
import com.example.mike.aidl.repository.model.Person;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    IBinder mBinder;
    private MainViewModel mvm;
    private EditText personName;
    private EditText personAge;
    private EditText personGender;
    private TextView personDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        personName = findViewById( R.id.etPersonName );
        personAge = findViewById( R.id.etPersonAge );
        personGender = findViewById( R.id.etPersonGender );
        personDisplay = findViewById( R.id.tvDisplay );

        mvm = ViewModelProviders.of(this).get(MainViewModel.class);

        // Provide service to mvm
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent( getApplicationContext(), PersonDBService.class );
                startService(intent);

                bindService( intent, mvm.mConn, BIND_AUTO_CREATE );
            }
        });
        thread.start();

    }

    public void addPerson(View view) {
        mvm.addPerson(
                new Person(
                        personName.getText().toString(),
                        personAge.getText().toString(),
                        personGender.getText().toString()
                ),
                new LocalDataStore.Callback() {
                    @Override
                    public void onSuccess(List<Person> people) {
                        Toast.makeText(getApplicationContext(), "Added person", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getApplicationContext(), "Error adding person "+error, Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void getPeople(View view) {
        mvm.getPeople(new LocalDataStore.Callback() {
            @Override
            public void onSuccess(List<Person> people) {
                String s = "";
                for (Person person : people) {
                    s += person.toString();
                }
                personDisplay.setText( s );
            }

            @Override
            public void onError(String error) {
                Toast.makeText( getApplicationContext(), "Error getting people "+error, Toast.LENGTH_LONG ).show();
            }
        });
    }
}


