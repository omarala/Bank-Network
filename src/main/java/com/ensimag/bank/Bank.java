package com.ensimag.bank;

import com.ensimag.api.bank.IAccount;
import com.ensimag.api.bank.IBank;
import com.ensimag.api.bank.IUser;

import javax.security.auth.login.AccountNotFoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cadicn on 11/8/18.
 */
public class Bank implements IBank {
    // Variable statique incrementee a chaque nouvelle banque
    private long bankId;
    private List<IAccount> accounts = new ArrayList<IAccount>();

    public Bank() {
        try{
            IDManager idManager = (IDManager) Naming.lookup("rmi://localhost:" + 1099 + "/service");
            this.bankId = idManager.nextBankId();

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public Bank(long bankId) {
        this.bankId = bankId;
    }

    public long getBankId() {
        return this.bankId;
    }

    public List<IAccount> getAccounts() throws RemoteException {
        return this.accounts;
    }

    public IAccount getAccount(long l) throws AccountNotFoundException, RemoteException {
        for (IAccount account : this.accounts) {
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
        for (IAccount account : this.accounts) {
            if (account.getAccountNumber() == l) {
                this.accounts.remove(account);
                return true;
            }
        }
        return false;
    }
}
