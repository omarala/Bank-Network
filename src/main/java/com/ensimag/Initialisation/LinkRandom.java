package com.ensimag.Initialisation;

import com.ensimag.api.bank.IBankNode;

import java.rmi.Naming;

public class LinkRandom {
    public static void main(){
        try{
            IBankNode bNode1 = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + 1);
            IBankNode bNode2 = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + 2);
            IBankNode bNode3 = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + 3);
            IBankNode bNode4 = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + 4);
            IBankNode bNode5 = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + 5);
            IBankNode bNode6 = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + 6);
            IBankNode bNode7 = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + 7);


            bNode2.addNeighboor(bNode1);
            bNode3.addNeighboor(bNode1);
            bNode4.addNeighboor(bNode1);
            bNode5.addNeighboor(bNode4);
            bNode6.addNeighboor(bNode5);
            bNode7.addNeighboor(bNode5);

            System.out.println("1 -> 2, 3, 4 ");
            System.out.println("2 -> 1");
            System.out.println("3 -> 1");
            System.out.println("4 -> 5, 1");
            System.out.println("5 -> 4, 6, 7");
            System.out.println("6 -> 5");
            System.out.println("7 -> 5");



        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
