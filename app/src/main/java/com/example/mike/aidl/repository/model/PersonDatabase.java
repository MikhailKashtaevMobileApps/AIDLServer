package com.example.mike.aidl.repository.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Person.class}, version = 1)
public abstract class PersonDatabase extends RoomDatabase {
    public static final String DB_NAME = "person_db";
    public abstract PersonDAO personDAO();
}
