package com.client;

/**
 * Client Main
 */
public class ClientLauncher {

    public static void main(String[] args) {
        try {
            IrcClient launcher = new IrcClient();
            launcher.launch();
        } catch (Exception e) {
            System.err.println("Something bad happened when trying to start client");
            e.printStackTrace();
        }
    }

}
