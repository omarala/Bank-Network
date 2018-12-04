package com.ensimag.bank;

import com.ensimag.api.bank.IUser;

/**
 * Created by cadicn on 11/8/18.
 */
public class User implements IUser{

    private String name;
    private String firstName;
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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof User))
            return false;
        User comparedUser = (User) o;
        return this.name.equals(comparedUser.getName()) &&  this.firstName.equals(comparedUser.getFirstName())
                && this.age.equals(comparedUser.getAge());
    }
}
