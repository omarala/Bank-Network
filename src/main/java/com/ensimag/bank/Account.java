package com.ensimag.bank;

import com.ensimag.api.bank.IAccount;
import com.ensimag.api.bank.IUser;
import com.ensimag.api.bank.NotEnoughMoneyException;
import java.util.Random;

/**
 * Created by cadicn on 11/8/18.
 */
public class Account implements IAccount {
    // Variable statique incrementee a chaque nouveau compte
    private static long nextAccountNumber = 1;
    private IUser user;
    private long accountNumber;
    private int total;
    private int allowedOverdraw;

    public Account(IUser iUser) {
        this.user = iUser;
        this.total = 0;
        this.allowedOverdraw = 0;
        this.accountNumber = nextAccountNumber;
        nextAccountNumber++;

    }
    public Account(IUser iUser, long accountNumber, int allowedOverdraw) {
        this.accountNumber = accountNumber;
        this.user = iUser;
        this.total = 0;
        this.allowedOverdraw = allowedOverdraw;
    }
    public IUser getAccountUser() {
        return this.user;
    }

    public long getAccountNumber() {
        return this.accountNumber;
    }

    public int add(int amount) {
        this.total += amount;
        return this.total;
    }

    public int remove(int amount) throws NotEnoughMoneyException {
        if (this.total + this.allowedOverdraw - amount >= 0) {
            this.total -= amount;
        }
        else {
            throw new NotEnoughMoneyException(this);
        }
        return this.total;
    }

    public int getTotal() {
        return this.total;
    }

    public int setAllowedOverdraw(int overdraw) {
        this.allowedOverdraw = overdraw;
        return this.allowedOverdraw;
    }

    @Override
    public String toString() {
        return "\n -------- Account ------\n User : " + this.user.toString() + "\n account number : "
                + this.accountNumber + "\n total : " + this.total + "\n allowed Overdrawn: " + this.allowedOverdraw
                + "\n ---------------------------";
    }

    /**
     * New equal to compare account
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Account)) {
            return false;
        }
        Account account = (Account) o;
        return account.accountNumber == this.accountNumber;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(this.accountNumber).hashCode();
    }
}
