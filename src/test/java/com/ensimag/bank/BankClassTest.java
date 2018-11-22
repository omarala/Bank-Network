package com.ensimag.bank;

import com.ensimag.api.bank.IAccount;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.security.auth.login.AccountNotFoundException;
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

    @Test
    public void closeAccountTest() {
        long accountId = 0;
        boolean bool = true;
        User user = new User("nono", "ko", "33");
        try {
            bank2.openAccount(user);
            for (IAccount account: bank2.getAccounts()) {
                if (account.getAccountUser().equals(user)) {
                    accountId = account.getAccountNumber();
                }
            }
            bank2.closeAccount(accountId);
            for (IAccount account: bank2.getAccounts()) {
                if (account.getAccountNumber() == accountId) {
                    bool = false;
                }
            }
            assertEquals(true, bool);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AccountNotFoundException e) {
            e.printStackTrace();
        }

    }
// TODO tester lever exception quand exception sera implementee
//    @Test
//    public void closeAccountThrowException() {
//        try {
//            bank1.closeAccount(-3);
//            fail("Exception not thrown for closeAccount");
//        } catch (AccountNotFoundException e) {
//            e.printStackTrace();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
}
