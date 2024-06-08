package com.example.tqapp.common.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String account;
    public String password;
    public String name;
}
