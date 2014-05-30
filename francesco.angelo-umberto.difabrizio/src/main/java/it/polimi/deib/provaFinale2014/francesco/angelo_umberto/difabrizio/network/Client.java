package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view.GuiView;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view.CommandLineView;
import java.util.Scanner;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class Client {

    public static void main(String[] args) {

        boolean rmi = false;
        final String ip = "127.0.0.1";
        final int port = 5050;
        final String nameServer = "sheepland";

//        Client client = new Client(ip, port);
        Scanner stdIn = new Scanner(System.in);
        boolean stringValid = false;
        String answer;
        int choice;

        while (!stringValid) {
            // try {
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
                    ClientRmi clientRmi = new ClientRmi(ip, port, new CommandLineView(), String nickName);
                    clientRmi.start();
                } else if (choice == 2) {
                    ClientRmi clientRmi = new ClientRmi(ip, port, nameServer, new GuiView(), String nickName);
                    clientRmi.start();
                }

            } else {
                System.out.println("La scelta inserita non è valida\n");
            }
        }catch (NumberFormatException e) {
            System.out.println("Scelta non valida\n");

        }
    }

    System.out.println("Server spento.");
    }
}
