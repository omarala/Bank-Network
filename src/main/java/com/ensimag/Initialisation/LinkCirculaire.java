package com.ensimag.Initialisation;

import com.ensimag.api.bank.IBankNode;

import java.rmi.Naming;

public class LinkCirculaire {
    public static void main(String[] args){
        try{
            IBankNode bNode1 = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + 1);
            IBankNode bNode2 = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + 2);
            IBankNode bNode3 = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + 3);
            IBankNode bNode4 = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + 4);

            bNode2.addNeighboor(bNode1);
            bNode1.addNeighboor(bNode2);

            bNode3.addNeighboor(bNode2);
            bNode2.addNeighboor(bNode3);

            bNode3.addNeighboor(bNode4);
            bNode4.addNeighboor(bNode3);

            bNode4.addNeighboor(bNode1);
            bNode1.addNeighboor(bNode4);

            System.out.println("network : 1 <-> 2 <-> 3 <-> 4 <-> 1");

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
