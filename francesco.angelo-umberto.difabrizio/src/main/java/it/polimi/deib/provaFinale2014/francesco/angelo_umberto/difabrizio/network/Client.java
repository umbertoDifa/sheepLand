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
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class Client {

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
        String nickName;
        do {
            stdOut.println("Inserisci il tuo nickName:");
            stdOut.flush();

            nickName = stdIn.nextLine();
        } while ("".equals(nickName) || nickName.contains(",") || nickName.contains(
                ":"));
        //da evitare come la peste la stringa vuota come nickname
        //esplodono i satelliti della nasa

        boolean valid = false;

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
                    ClientSocket client = new ClientSocket(ip, socketPort,
                            new CommandLineView(), nickName);
                    client.startClient();
                    valid = true;
                } else if ("2".equals(typeOfInterface)) {
                    //Socket - GUI
                    ClientSocket client = new ClientSocket(ip, socketPort,
                            new GuiView(), nickName);
                    client.startClient();
                    valid = true;
                }
            } else if ("2".equals(typeOfConnection)) {
                if ("1".equals(typeOfInterface)) {
                    //Rmi - CLC
                    ClientRmi client;
                    try {
                        client = new ClientRmi(ip, rmiPort, nameServer,
                                new CommandLineView(),
                                nickName);
                        client.startClient();
                        DebugLogger.println("Client remoto attivo");
                    } catch (RemoteException ex) {
                        Logger.getLogger(DebugLogger.class.getName()).log(
                                Level.SEVERE,
                                ex.getMessage(), ex);
                    }

                    valid = true;
                } else if ("2".equals(typeOfInterface)) {
                    //rmi -GUI
                    ClientRmi client;
                    try {
                        client = new ClientRmi(ip, rmiPort, nameServer,
                                new GuiView(),
                                nickName);
                        client.startClient();
                        DebugLogger.println("Client remoto attivo");
                    } catch (RemoteException ex) {
                        Logger.getLogger(DebugLogger.class.getName()).log(
                                Level.SEVERE,
                                ex.getMessage(), ex);
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

    }
}
