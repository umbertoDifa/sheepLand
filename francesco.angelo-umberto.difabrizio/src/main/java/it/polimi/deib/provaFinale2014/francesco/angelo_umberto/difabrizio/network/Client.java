package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view.CommandLineView;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view.GuiView;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * It's the client manager, it ask the user which kind of connections it prefers
 * and which kind of guy then starts the righ client
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class Client {

    /**
     * The lock is used so that after a player finishes a game the lock is
     * released and the client manager asks to the player if he wants to play
     * again.
     */
    protected static final Object LOCK = new Object();

    /**
     * Starts the client asking the nickName, the type of connection and the
     * type of view preferred
     *
     * @param args
     */
    public static void main(String[] args) {

        final String ip = "localhost";

        final int rmiPort = NetworkConstants.PORT_RMI.getValue();
        final int socketPort = NetworkConstants.PORT_SOCKET.getValue();

        final String nameServer = "sheepland";

        Scanner stdIn = new Scanner(System.in);
        PrintWriter stdOut = new PrintWriter(System.out);

        String answer;

        boolean valid = false;
        do {

            while (!valid) {
                stdOut.println("Scegli connessione:\n1- Socket\n2- RMI");
                stdOut.flush();
                String typeOfConnection = stdIn.nextLine();

                stdOut.println("Scegli interfaccia:\n1- CLC\n2- GUI");
                stdOut.flush();
                String typeOfInterface = stdIn.nextLine();

                if ("1".equals(typeOfConnection)) {
                    if ("1".equals(typeOfInterface)) {
                        //Socket - CLC
                        ClientSocket client = new ClientSocket(ip,
                                socketPort,
                                new CommandLineView());
                        client.startClient();
                        valid = true;
                    } else if ("2".equals(typeOfInterface)) {
                        //Socket - GUI
                        ClientSocket client = new ClientSocket(ip,
                                socketPort,
                                new GuiView());
                        client.startClient();
                        valid = true;
                    }
                } else if ("2".equals(typeOfConnection)) {
                    if ("1".equals(typeOfInterface)) {
                        //Rmi - CLC
                        ClientRmi client;
                        try {
                            client = new ClientRmi(ip, rmiPort, nameServer,
                                    new CommandLineView());
                            client.startClient();
                            DebugLogger.println("Client remoto attivo");
                            synchronized (LOCK) {
                                LOCK.wait();
                            }
                        } catch (InterruptedException ex) {
                            Logger.getLogger(DebugLogger.class.getName()).log(
                                    Level.SEVERE,
                                    null, ex);
                        }

                        valid = true;
                    } else if ("2".equals(typeOfInterface)) {
                        //rmi -GUI
                        ClientRmi client;
                        try {
                            client = new ClientRmi(ip, rmiPort, nameServer,
                                    new GuiView());
                            client.startClient();
                            DebugLogger.println("Client remoto attivo");
                            synchronized (LOCK) {
                                LOCK.wait();
                            }
                        } catch (InterruptedException ex) {
                            Logger.getLogger(DebugLogger.class.getName()).log(
                                    Level.SEVERE,
                                    null, ex);
                        }
                        valid = true;
                    }
                }
                if (!valid) {
                    stdOut.println(
                            "\nI dati inseriti non sono validi prego riprovare.\n");
                    stdOut.flush();
                }
            }

            valid = false;
            do {
                stdOut.println("Iniziare una nuova partita?(S/N)");
                stdOut.flush();
                answer = stdIn.nextLine();
            } while (!"S".equalsIgnoreCase(answer) && !"N".equalsIgnoreCase(
                    answer));

        } while ("S".equalsIgnoreCase(answer));
    }

    private Client() {
    }

}
