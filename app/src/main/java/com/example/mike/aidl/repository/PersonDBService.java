package com.example.mike.aidl.repository;

import android.app.Service;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.telecom.Call;

import com.example.mike.aidl.IMyAidlInterface;
import com.example.mike.aidl.repository.local.LocalDataStore;
import com.example.mike.aidl.repository.model.Person;
import com.example.mike.aidl.repository.model.PersonDAO;
import com.example.mike.aidl.repository.model.PersonDatabase;

import java.util.List;

public class PersonDBService extends Service {

    MyBinder mBinder = new MyBinder();
    LocalDataStore lds;

    public PersonDBService() {
        lds = new LocalDataStore(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private PersonDAO getDAO(){
        return Room.databaseBuilder(getApplicationContext(), PersonDatabase.class, PersonDatabase.DB_NAME).build().personDAO();
    }

    public class MyBinder extends IMyAidlInterface.Stub {

        public void getPeople(LocalDataStore.Callback callback){
            lds.getAll(callback);
        }

        public void insert(Person p, LocalDataStore.Callback callback){
            lds.insert(p, callback);
        }

        public void delete(Person p, LocalDataStore.Callback callback){
            lds.delete(p, callback);
        }

        // Reserved for AIDL
        @Override
        public String aidl_getPersons() throws RemoteException {
            return "  ";
        }
    }

}
