package com.ensimag.Initialisation;

import com.ensimag.api.bank.IBankNode;
import com.ensimag.api.message.EnumMessageType;
import com.ensimag.bank.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by cadicn on 12/11/18.
 */
public class createNetwork {
    public static long openAccountAction(Scanner scanner, long nb) {
        System.out.println("Enter the bank in which you would like to open the account (between 1 and " + nb + ") : ");
        return scanner.nextLong();
    }

    public static long[] closeAccountAction(Scanner scanner, long nb) {
        long[] params = new long[2];
        System.out.println("Enter the bank in which you would like to close the account (between 1 and " + nb + ") : ");
        params[0] = scanner.nextLong();
        System.out.println("Enter the id of the account you would like to close : ");
        params[1] = scanner.nextInt();
        return params;
    }

    public static long[] addAmountAction(Scanner scanner, long nb) {
        long[] params = new long[3];
        System.out.println("Enter the bank concerned (between 1 and " + nb + ") : ");
        params[0] = scanner.nextLong();
        System.out.println("Enter the id of the concerned account : ");
        params[1] = scanner.nextLong();
        System.out.println("Enter the amount you would like to add : ");
        params[2] = scanner.nextLong();
        return params;
    }

    public static long[] withDrawAmountAction(Scanner scanner, long nb) {
        long[] params = new long[3];
        System.out.println("Enter the bank concerned (between 1 and " + nb + ") : ");
        params[0] = scanner.nextLong();
        System.out.println("Enter the id of the concerned account : ");
        params[1] = scanner.nextLong();
        System.out.println("Enter the amount you would like to withdraw : ");
        params[2] = scanner.nextLong();
        return params;
    }

    public static long[] consultAccountAction(Scanner scanner, long nb) {
        long[] params = new long[2];
        System.out.println("Enter the bank concerned (between 1 and " + nb + ") : ");
        params[0] = scanner.nextLong();
        System.out.println("Enter the id of the concerned account : ");
        params[1] = scanner.nextLong();
        return params;
    }


    public static void displayChoices() {
        System.out.println("Here are the actions you can perform, select the number of the corresponding action ");
        System.out.println("1 - Consult an account");
        System.out.println("2 - Open an account");
        System.out.println("3 - Close an account");
        System.out.println("4 - Add money to an account");
        System.out.println("5 - Withdraw money of an account");
        System.out.println("6 - Exit");
    }

    private static void handleOpenAccount(Scanner scanner, long nb, User user, IBankNode connectedBN) {
        long param = openAccountAction(scanner, nb);
        try {
            System.out.println("I am in bankNode"+ connectedBN.getId() + " , opening account in bankNode"+param);
            IIDManager idManager = (IIDManager) Naming.lookup("rmi://localhost:" + 1099 + "/IDManager");
            BankMessage msg = new BankMessage(connectedBN.getId(), idManager.nextMessageId(), connectedBN.getId(), param,
                    new OpenAccountAction(user), EnumMessageType.SINGLE_DEST);
            connectedBN.onMessage(msg);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void handleCloseAccount(Scanner scanner, long nb, User user, IBankNode connectedBN) {
        long[] params = closeAccountAction(scanner, nb);
        try {
            IIDManager idManager = (IIDManager) Naming.lookup("rmi://localhost:" + 1099 + "/IDManager");
            BankMessage msg = new BankMessage(connectedBN.getId(), idManager.nextMessageId(), connectedBN.getId(), params[0],
                    new CloseAccountAction(params[1], user), EnumMessageType.SINGLE_DEST);
            connectedBN.onMessage(msg);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void handleAddAmount(Scanner scanner, long nb, IBankNode connectedBN) {
        long[] params = addAmountAction(scanner, nb);
        try {
            IIDManager idManager = (IIDManager) Naming.lookup("rmi://localhost:" + 1099 + "/IDManager");
            BankMessage msg = new BankMessage(connectedBN.getId(), idManager.nextMessageId(), connectedBN.getId(), params[0],
                    new AddAmoutAction(params[1], (int)params[2]), EnumMessageType.SINGLE_DEST);
            connectedBN.onMessage(msg);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void handleWithdrawAmount(Scanner scanner, long nb, IBankNode connectedBN) {
        long[] params = withDrawAmountAction(scanner, nb);
        try {
            //the destination
            IIDManager idManager = (IIDManager) Naming.lookup("rmi://localhost:" + 1099 + "/IDManager");
            BankMessage msg = new BankMessage(connectedBN.getId(), idManager.nextMessageId(), connectedBN.getId(), params[0],
                    new WithdrawAccountAction(params[1], (int)params[2]), EnumMessageType.SINGLE_DEST);
            connectedBN.onMessage(msg);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    private static void handleConsult(Scanner scanner, long nb, IBankNode connectedBN) {
        long[] params = consultAccountAction(scanner, nb);
        try {
            IBankNode bn = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + params[0]);
            IIDManager idManager = (IIDManager) Naming.lookup("rmi://localhost:" + 1099 + "/IDManager");
            BankMessage msg = new BankMessage(connectedBN.getId(), idManager.nextMessageId(), connectedBN.getId(), params[0],
                    new ConsultAccountAction(params[1]), EnumMessageType.SINGLE_DEST);
            connectedBN.onMessage(msg);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Get the IDManager
        try {
            IIDManager idManager = (IIDManager) Naming.lookup("rmi://localhost:" + 1099 + "/IDManager");
            long numberOfBanks = idManager.getNextBankId() - 1;
            for (int i = 1 ; i<numberOfBanks + 1; i++){
                IBankNode bNode = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + i);
                bNode.addNeighboor(null);
            }
            System.out.println("Let's identify you \n ");
            System.out.println("Enter you name : ");
            String name = scanner.nextLine();
            System.out.println("Enter you family name : ");
            String famileName = scanner.nextLine();
            System.out.println("Enter your age : ");
            String age = scanner.nextLine();
            User user = new User(name, famileName, age);
            System.out.print("Enter the Id of the bank you would like to connect, between 1 and " + numberOfBanks + " : ");
            int nb = scanner.nextInt();
            IBankNode bankNode = (IBankNode) Naming.lookup("rmi://localhost:" + 1099 + "/BankNode" + nb);
            System.out.println("Connected to BankNode" + nb);
            int actionNb = 0;
            while(actionNb != 6) {
                displayChoices();
                actionNb = scanner.nextInt();
                switch (actionNb) {
                    case 1:
                        handleConsult(scanner, numberOfBanks, bankNode);
                        break;
                    case 2:
                        handleOpenAccount(scanner, numberOfBanks, user, bankNode);
                        break;
                    case 3:
                        handleCloseAccount(scanner, numberOfBanks, user, bankNode);
                        break;
                    case 4:
                        handleAddAmount(scanner, numberOfBanks, bankNode);
                        break;
                    case 5:
                        handleWithdrawAmount(scanner, numberOfBanks, bankNode);
                        break;
                    case 6:
                        break;
                    default:
                        break;
                }
            }

        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

}
