package com.example.mike.aidl.ui;

import android.arch.lifecycle.ViewModel;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.mike.aidl.repository.PersonDBService;
import com.example.mike.aidl.repository.local.LocalDataStore;
import com.example.mike.aidl.repository.model.Person;

import java.util.List;

public class MainViewModel extends ViewModel {

    PersonDBService.MyBinder mBinder;
    Connection mConn;

    public MainViewModel(){
        this.mConn = new Connection();
    }

    public class Connection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MainViewModel.this.mBinder = (PersonDBService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    public void getPeople(LocalDataStore.Callback callback){
        mBinder.getPeople(callback);
    }

    public void addPerson(Person person, LocalDataStore.Callback callback){ mBinder.insert(person, callback); }
}
