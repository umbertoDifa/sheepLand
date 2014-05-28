package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class Client {

    public static void main(String[] args) {
        //server info
    private boolean rmi = false;
    private final String ip = "127.0.0.1";
    private final int port = 5050;
    private final String nameServer = "sheepland";

//        Client client = new Client(ip, port);
    Scanner stdIn = new Scanner(System.in);
    boolean stringValid = false;
    String answer;
    int choice;

    while (stringValid== false) {
            try {
            System.out.println(
                    "Scegli connessione:\n1- Socket\n2- RMI");
            answer = stdIn.nextLine();
            choice = Integer.parseInt(answer);

            if (choice == 1) {
                stringValid = true;
                rmi = false;
                System.out.println(
                        "Scegli interfaccia:\n1- CLC\n2- GUI");
                answer = stdIn.nextLine();
                choice = Integer.parseInt(answer);
                if (choice == 1) {
                    ClientSocket clientSocket = new ClientSocket(ip,
                            port, new CommandLineView());
                } else if (choice == 2) {
                    ClientSocket clientSocket = new ClientSocket(ip,
                            port, new GuiView());
                }
            } else if (choice == 2) {
                stringValid = true;
                rmi = true;
                System.out.println(
                        "Scegli interfaccia:\n1- CLC\n2- GUI");
                answer = stdIn.nextLine();
                choice = Integer.parseInt(answer);
                if (choice == 1) {
                    ClientRmi clientRmi = new ClientRmi(ip,
                            port, new CommandLineView(), String nickName
                    );
                        clientRmi.start();
                } else if (choice == 2) {
                    ClientRmi clientRmi = new ClientRmi(ip,
                            port, nameServer, new GuiView(), String nickName
                    );
                        clientRmi.start();
                }

            } else {
                System.out.println("La scelta inserita non è valida\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("Scelta non valida\n");

        }
    }
    System.out.println ("Server spento.");
    }
}
