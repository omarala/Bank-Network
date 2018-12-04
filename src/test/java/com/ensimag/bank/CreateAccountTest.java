package com.ensimag.bank;

import static org.junit.Assert.*;

import com.ensimag.api.bank.NotEnoughMoneyException;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by cadicn on 11/8/18.
 */
public class CreateAccountTest {
    private static User user;
    private static Account account1;
    private static Account account2;


    @BeforeClass
    public static void setUpParameters() {
        user = new User("Nolwenn", "Cadic", "22");
        account1 = new Account(user);
        account2 = new Account(user);
    }


    @Test
    public void AccountNumberIncreases() {
        assertEquals(account1.getAccountNumber() + 1, account2.getAccountNumber());
    }

    @Test
    public void AccountNumberCreated() {
        Account account = new Account(user);
        System.out.println(account.getTotal());
        assertEquals(0, account.getTotal());
        assertEquals(user, account.getAccountUser());
    }

    @Test
    public void addTest() {
        account1.add(5);
        assertEquals(5, account1.getTotal());
    }

    @Test
    public void removeTest() {
        try {
            account1.remove(3);
        } catch (NotEnoughMoneyException e) {
            e.printStackTrace();
        }
        assertEquals(2, account1.getTotal());
    }

    @Test
    public void removeThrowsExceptionTest() {
        try {
            account2.remove(3);
            fail("Exception not thrown");
        } catch (NotEnoughMoneyException e) {
            e.printStackTrace();
        }
    }


}
