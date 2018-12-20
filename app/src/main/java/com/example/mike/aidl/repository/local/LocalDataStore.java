package com.example.mike.aidl.repository.local;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.mike.aidl.repository.model.Person;
import com.example.mike.aidl.repository.model.PersonDAO;
import com.example.mike.aidl.repository.model.PersonDatabase;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class LocalDataStore {
    private static final String TAG = "__TAG__";

    private Context context;

    public LocalDataStore( Context context ) {
        this.context = context;
    }

    private PersonDAO getDAO(){
        return Room.databaseBuilder( this.context, PersonDatabase.class, "person_db" ).build().personDAO();
    }

    public void getAll(final Callback callback ){
        Single.fromCallable(new Callable<List<Person>>() {
            @Override
            public List<Person> call() throws Exception {
                return getDAO().getAll();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Person>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: started getAll retrieve");
                    }

                    @Override
                    public void onSuccess(List<Person> people) {
                        Log.d(TAG, "onSuccess: "+people.toString());
                        callback.onSuccess(people);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: "+e.getMessage());
                        callback.onError(e.getMessage());
                    }
                });
    }

    public void insert(final Person person, final Callback callback){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                getDAO().insert(person);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: insert");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                        callback.onSuccess(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: "+e.getMessage());
                        callback.onError(e.getMessage());
                    }
                });
    }

    public void delete(final Person person, final Callback callback){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                getDAO().delete(person);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        callback.onSuccess(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError( e.getMessage() );
                    }
                });
    }

    public interface Callback{
        void onSuccess( List<Person> people );
        void onError( String error );
    }

}
