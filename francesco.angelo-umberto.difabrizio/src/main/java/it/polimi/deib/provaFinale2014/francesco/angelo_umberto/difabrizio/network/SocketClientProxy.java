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

    /**
     * Create a socket client proxy that deals with the communication with the
     * given socket
     *
     * @param socket The socket representing the client
     */
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

    protected void checkConnection() {
        if (socket.isClosed() || socket.isInputShutdown() || socket.isOutputShutdown()) {
            DebugLogger.println(
                    "Il socket è closed lo status è posto su offline");
            super.setStatus(NetworkConstants.OFFLINE);
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
     * Manda un messaggio al client attraverso il proprio socket
     *
     * @param info
     */
    protected void send(int info) {
        toClient.println(info);
        //flusha lo stream e controlla eventuali errori
        if (toClient.checkError()) {
            DebugLogger.println(
                    "C'è stato un errore inviando al client lo status è posto su offline");
            super.setStatus(NetworkConstants.OFFLINE);
        }
    }

    /**
     * Manda un messaggio al client attraverso il proprio socket
     *
     * @param message boolean
     */
    protected void send(boolean message) {
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
    protected String receiveString() throws PlayerDisconnectedException {
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

    /**
     * Riceve un messaggio dal client
     *
     * @return il boolean ricevuta dal client
     *
     * @throws PlayerDisconnectedException if the player disconnects during is
     *                                     own shift
     */
    protected boolean receiveBoolean() throws PlayerDisconnectedException {
        try {
            boolean answer = fromClient.nextBoolean();

            //skip new line
            receiveString();

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

    /**
     * Riceve un int dal client
     *
     * @return il int ricevuta dal client
     *
     * @throws PlayerDisconnectedException if the player disconnects during is
     *                                     own shift
     */
    protected int receiveInt() throws PlayerDisconnectedException {
        try {
            int answer = fromClient.nextInt();

            //skip new line
            receiveString();

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
