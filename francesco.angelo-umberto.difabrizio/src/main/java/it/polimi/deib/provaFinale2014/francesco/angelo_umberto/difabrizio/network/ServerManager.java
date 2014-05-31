package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class is a manager that collects connections from clients and starts
 * games when there are enough cilents. It starts a thread any time it needs to
 * start a new game but it allows only a maximum of games.
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class ServerManager {

    /**
     * Main method, creates a serverMangager and starts it
     *
     * @param args
     */
    public static void main(String[] args) {
        //creo un server su una certa porta

        String answer;
        int choice;
        int port = 5050;
        String serverName = "sheepland";

        Scanner stdIn = new Scanner(System.in);
        PrintWriter stdOut = new PrintWriter(System.out);

        boolean stringValid = false;

        //DEBUG ON/OFF
        DebugLogger.turnOffExceptionLog();

        while (!stringValid) {
            try {
                stdOut.println(
                        "Scegli connessione:\n1- Socket\n2- RMI");
                stdOut.flush();

                answer = stdIn.nextLine();
                choice = Integer.parseInt(answer);

                if (choice == 1) {
                    stringValid = true;
                    ServerSockets server = new ServerSockets(port);
                    server.start();
                } else if (choice == 2) {
                    stringValid = true;
                    ServerRmiImpl server = new ServerRmiImpl(serverName,
                            "localhost", port);
                    server.start();
                } else {
                    stdOut.println("La scelta inserita non è valida\n");
                    stdOut.flush();

                }
            } catch (NumberFormatException e) {
                stdOut.println("Scelta non valida\n");
                stdOut.flush();

            } catch (RemoteException ex) {
                //TODO
                Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        stdOut.println("Thread server attivato.");
        stdOut.flush();

    }

}
