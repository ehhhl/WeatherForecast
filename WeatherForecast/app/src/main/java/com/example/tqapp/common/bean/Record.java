package com.example.tqapp.common.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Record implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id; // 记录id
    public String userAccount;
    public String title;
    public String content;
    public String time;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(time, record.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time);
    }
}