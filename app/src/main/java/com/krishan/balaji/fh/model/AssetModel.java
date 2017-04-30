package com.krishan.balaji.fh.model;

/**
 * Created by balaji142857 on 5/8/16.
 */
public class AssetModel {

    private String name;
    private float balance;
    private long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}