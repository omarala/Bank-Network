package com.ensimag.Initialisation;

import com.ensimag.api.bank.IBankNode;
import com.ensimag.api.message.EnumMessageType;
import com.ensimag.bank.*;

import com.sun.xml.internal.bind.v2.model.core.ID;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by cadicn on 11/22/18.
 */
public class CreateBankNode {
    public static void main(String[] args) {
        try {
            Registry r = LocateRegistry.getRegistry("localhost", 1099);
            IIDManager idManager = (IIDManager) Naming.lookup("rmi://localhost:" + 1099 + "/IDManager");
            System.out.println(idManager);
            long newBankId = idManager.nextBankId();
            IBankNode bankNode = new BankNode(new Bank(newBankId), idManager);
            r.bind("BankNode" + newBankId, bankNode);
            System.out.println("----- BankNode" + newBankId + " is available on RMI registry-----");


            if(newBankId > 1) {
                Random random = new Random();
                int nbNeighboors = random.nextInt((int) newBankId - 1) + 1;
                ArrayList<Integer> list = new ArrayList<Integer>();
                for (int i = 1; i < newBankId; i++) {
                    list.add(i);
                }

                for (int i = 0; i < nbNeighboors; i++) {
                    int rd = random.nextInt(list.size());
                    int idVoisin = list.get(rd);
                    list.remove(rd);
                    IBankNode voisin = (IBankNode) r.lookup("BankNode" + idVoisin);
                    bankNode.addNeighboor(voisin);
                    voisin.addNeighboor(bankNode);
                    System.out.println("youhouuu j'ai rajoute des voisins");

                }
            }
            if(newBankId == 5){
                User user = new User("Cadic", "Nolwenn", "21");
                BankMessage bankMessage = new BankMessage(newBankId, idManager.nextMessageId(), newBankId, 1, new OpenAccountAction(user), EnumMessageType.SINGLE_DEST);
                bankNode.onMessage(bankMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
