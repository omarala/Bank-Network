package com.ensimag.bank;

import com.ensimag.api.bank.IAccount;
import com.ensimag.api.bank.IBank;
import com.ensimag.api.bank.IUser;

import javax.security.auth.login.AccountNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cadicn on 11/8/18.
 */
public class Bank implements IBank {
    // Variable statique incrementee a chaque nouvelle banque
    private static long nextBankId = 1;
    private long bankId;
    private List<IAccount> accounts;

    public Bank() {
        this.bankId = this.nextBankId;
        this.accounts = new ArrayList<IAccount>();
        nextBankId++;
    }

    public long getBankId() {
        return this.bankId;
    }

    public List<IAccount> getAccounts() throws RemoteException {
        return this.accounts;
    }

    public IAccount getAccount(long l) throws AccountNotFoundException, RemoteException {
        for (IAccount account: this.accounts) {
            if (account.getAccountNumber() == l) {
                return account;
            }
        }
        System.out.println("Account not found");
        return null;
    }

    public IAccount openAccount(IUser iUser) throws RemoteException {
        Account account = new Account(iUser);
        this.accounts.add(account);
        return account;
    }

    public boolean closeAccount(long l) throws AccountNotFoundException, RemoteException {
        for (IAccount account: this.accounts) {
            if (account.getAccountNumber() == l) {
                this.accounts.remove(account);
                return true;
            }
        }
        return false;
    }
}
