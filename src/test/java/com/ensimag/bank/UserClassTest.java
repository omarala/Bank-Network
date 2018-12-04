package com.ensimag.bank;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by cadicn on 12/4/18.
 */
public class UserClassTest {
    @Test
    public void equalsUser() {
        User user1 = new User("Toto", "tutu", "23");
        User user2 = new User("Toto", "tutu", "23");
        assertTrue(user1.equals(user2));
    }
}
