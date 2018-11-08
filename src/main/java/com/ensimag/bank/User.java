package com.ensimag.bank;

import com.ensimag.api.bank.IUser;

/**
 * Created by cadicn on 11/8/18.
 */
public class User implements IUser{

    private String name;
    private String firstName;
    // TODO Changer age en date de naissance et changer le getter
    private String age;

    public User(String name, String firstName, String age) {
        this.name = name;
        this.firstName = firstName;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getAge() {
        return age;
    }
}
