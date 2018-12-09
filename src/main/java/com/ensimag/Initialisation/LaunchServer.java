package com.ensimag.Initialisation;

import com.ensimag.bank.IDManager;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LaunchServer {
    public static void main(String[] args) {
        try {
            // Create RMI registry
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(1099);
            System.out.println("-----Server Launched on 1099 port of ENSIPC560----");

            while (true) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}