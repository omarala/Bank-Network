package com.ensimag.Initialisation;

import com.ensimag.api.bank.IBankNode;
import com.ensimag.bank.Bank;
import com.ensimag.bank.BankNode;
import com.ensimag.bank.IDManager;

import com.sun.xml.internal.bind.v2.model.core.ID;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by cadicn on 11/22/18.
 */
public class CreateBankNode {
    public static void main(String[] args) {
        try {
            Registry r = LocateRegistry.getRegistry("ENSIPC560", 1099);
            long newBankId = IDManager.nextBankId();

            IBankNode bankNode = new BankNode(new Bank(newBankId));
            r.bind("BankNode" + newBankId, bankNode);
            System.out.println("----- BankNode" + newBankId + " is available on RMI registry-----");

            // Connect the node to the network, if it's not the first node
            if (newBankId > 1) {
                long prevBankNodeId = newBankId - 1;
                IBankNode prevBankNode = (IBankNode) r.lookup("BankNode" + prevBankNodeId);
                prevBankNode.addNeighboor(bankNode);
                bankNode.addNeighboor(prevBankNode);
                System.out.println(
                        "----- BankNode" + newBankId + " has BankNode" + prevBankNodeId + " as neighboor-----");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
