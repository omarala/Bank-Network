package com.ensimag.bank;

import com.ensimag.api.bank.IAccount;
import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by cadicn on 11/9/18.
 */
public class BankClassTest {
    private static Bank bank1;
    private static Bank bank2;

    @BeforeClass()
    public static void initParameters() {
        bank1 = new Bank();
        bank2 = new Bank();
    }

    @Test
    public void BankInitTest() {
        assertEquals(1, bank1.getBankId());
        try {
            assertEquals(new ArrayList<IAccount>(), bank1.getAccounts());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void BankIdIncreases() {
        assertEquals(bank1.getBankId()+1, bank2.getBankId());
    }

    @Test
    public void openAccountTest() {
        User user = new User("toto", "dede", "21");
        try {
            bank2.openAccount(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            assertEquals(bank2.getAccounts().get(0).getAccountUser(), user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
