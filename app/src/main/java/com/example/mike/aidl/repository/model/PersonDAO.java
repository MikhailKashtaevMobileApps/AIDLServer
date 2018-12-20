package com.example.mike.aidl.repository.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PersonDAO {

    @Query("SELECT * FROM person")
    List<Person> getAll();

    @Insert
    void insert(Person person);

    @Delete
    void delete(Person person);

}
