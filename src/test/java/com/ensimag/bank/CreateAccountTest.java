package com.ensimag.bank;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runners.Parameterized;

/**
 * Created by cadicn on 11/8/18.
 */
public class CreateAccountTest {

    private User user = new User("Nolwenn", "Cadic", "22");
    private Account account1 = new Account(user);
    private Account account2 = new Account(user);

    @Test
    public void AccoundNumberIncreases() {
        assertEquals(1, account1.getAccountNumber());
        assertEquals(2, account2.getAccountNumber());
    }
}
