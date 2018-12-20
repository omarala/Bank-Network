package com.ensimag.Initialisation;

import com.ensimag.api.bank.IBankNode;

import java.rmi.Naming;

public class LinkLineaire {
    public static void main(String[] args){
        try{
            IBankNode bNode1 = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + 1);
            IBankNode bNode2 = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + 2);
            IBankNode bNode3 = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + 3);

            bNode2.addNeighboor(bNode1);
            bNode3.addNeighboor(bNode2);

            System.out.println("network : 1 <-> 2 <-> 3 ");

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
