package com.example.ocbcpayment.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static com.example.ocbcpayment.base.Constants.DB_TABLE;

@Entity(tableName = DB_TABLE)
public class User implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String phone;
    private String password;
    private String amount;


    public User(String name, String phone, String password, String amount) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}