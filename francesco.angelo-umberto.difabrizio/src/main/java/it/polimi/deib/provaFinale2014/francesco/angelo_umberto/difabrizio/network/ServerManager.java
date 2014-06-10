package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
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
     * it's the map that links the client nickName to its proxy, which is the
     * interface to comunicate with the client and can be a socket proxy or an
     * rmi proxy
     */
    public static final Map<String, ClientProxy> Nick2ClientProxyMap = new HashMap<String, ClientProxy>();
    /**
     * It represents the number of active games. Since it's static it can be
     * modified by any thread which decrements it before dying
     */
    public static int activatedGames = 0;

    /**
     * Main method, creates a serverMangager and starts it
     *
     * @param args
     */
    public static void main(String[] args) {
        String serverName = "sheepland";

        int socketPort = NetworkConstants.PORT_SOCKET.getValue();
        int rmiPort = NetworkConstants.PORT_RMI.getValue();

        Scanner stdIn = new Scanner(System.in);
        PrintWriter stdOut = new PrintWriter(System.out);

        String answer;
        int choice;
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
                    ServerSockets server = new ServerSockets(socketPort);
                    server.start();
                } else if (choice == 2) {
                    stringValid = true;
                    ServerRmiImpl server = new ServerRmiImpl(serverName,
                            rmiPort);
                    server.start();
                } else {
                    stdOut.println("La scelta inserita non è valida\n");
                    stdOut.flush();

                }
            } catch (NumberFormatException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);
                stdOut.println("Scelta non valida\n");
                stdOut.flush();

            } catch (RemoteException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);
                stdOut.println("Error catched by server manager.");
                stdOut.flush();

            }
        }
        stdOut.println("Thread server attivato.");
        stdOut.flush();

    }

    private ServerManager() {
    }

}
