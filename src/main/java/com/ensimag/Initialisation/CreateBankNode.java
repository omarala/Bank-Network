package com.ensimag.Initialisation;

import com.ensimag.api.bank.IBankNode;
import com.ensimag.api.message.EnumMessageType;
import com.ensimag.bank.*;

import com.sun.xml.internal.bind.v2.model.core.ID;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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

            // Connect the node to the network, if it's not the first node
            if (newBankId > 1) {
                long prevBankNodeId = newBankId - 1;
                IBankNode prevBankNode = (IBankNode) r.lookup("BankNode" + prevBankNodeId);
                bankNode.addNeighboor(prevBankNode);
                prevBankNode.addNeighboor(bankNode);
                System.out.println("----- BankNode" + newBankId + " has BankNode" + prevBankNodeId + " as neighboor-----");
            }
            if(newBankId == 2){
                User user = new User("Cadic", "Nolwenn", "21");
                BankMessage bankMessage = new BankMessage(newBankId, idManager.nextMessageId(), newBankId, 1, new OpenAccountAction(user), EnumMessageType.SINGLE_DEST);
                //IBankNode prevBankNode = (IBankNode) r.lookup("BankNode" + 1);
                bankNode.onMessage(bankMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
