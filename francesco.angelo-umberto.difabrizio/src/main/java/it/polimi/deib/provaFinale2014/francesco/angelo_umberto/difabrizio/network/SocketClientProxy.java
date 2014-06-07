package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe del client visto dal server, contiene il socket e gli stream per
 * parlarci
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class SocketClientProxy extends ClientProxy {

    private final Socket socket;
    private Scanner fromClient;
    private PrintWriter toClient;

    public SocketClientProxy(Socket socket) {
        super();

        this.socket = socket;
        try {
            //inizializzo stream out
            this.toClient = new PrintWriter(this.socket.getOutputStream());

            //inizializzo streamo in
            this.fromClient = new Scanner(this.socket.getInputStream());
        } catch (IOException ex) {
            //se fallisce la creazione di un canale di scambio dati
            Logger.getLogger(DebugLogger.class.getName()).log(
                    Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Returns the socket
     *
     * @return the socket of this client
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Manda un messaggio al client attraverso il proprio socket
     *
     * @param message
     */
    protected void send(String message) {
        toClient.println(message);
        //flusha lo stream e controlla eventuali errori
        if (toClient.checkError()) {
            DebugLogger.println(
                    "C'è stato un errore inviando al client lo status è posto su offline");
            super.setStatus(NetworkConstants.OFFLINE);
        }
    }

    /**
     * Riceve un messaggio dal client
     *
     * @return la stringa ricevuta dal client
     *
     * @throws PlayerDisconnectedException if the player disconnects during is
     *                                     own shift
     */
    protected String receive() throws PlayerDisconnectedException {
        //TODO:gestisci queste eccez
        try {
            String answer = fromClient.nextLine();
            return answer;
        } catch (NoSuchElementException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            DebugLogger.println(
                    "C'è stato un errore ricevendo dal client lo status è posto su offline");
            super.setStatus(NetworkConstants.OFFLINE);
            throw new PlayerDisconnectedException("Il player si è disconnesso");
        }
    }

}
