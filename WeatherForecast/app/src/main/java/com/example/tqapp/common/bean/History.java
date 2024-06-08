package com.example.tqapp.common.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class History implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String city;
    public String provinces;
    public String account;
}
