package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandLineView implements TypeOfView {

    private final PrintWriter stdOut = new PrintWriter(System.out);
    private final Scanner stdIn = new Scanner(System.in);

    public CommandLineView() {

    }

    public void refreshRegion(int regionIndex, int numbOfSheep, int numbOfRam,
                              int numbOfLamb) {
        showInfo("La regione " + regionIndex + " ora ha " + numbOfSheep
                + " pecore, " + numbOfLamb + " agnelli, " + numbOfRam + " montoni.");

    }

    public void refreshStreet(int streetIndex, boolean fence,
                              String nickShepherd) {
        stdOut.print("La strada " + streetIndex + " è ");

        if (fence == true) {
            stdOut.println(" recintata");
            stdOut.flush();

        } else if (nickShepherd != null && !"null".equals(nickShepherd)) {
            stdOut.println(" occupata da " + nickShepherd);
            stdOut.flush();

        } else {
            stdOut.println(" libera");
            stdOut.flush();

        }
    }

    public void refereshGameParameters(int numbOfPlayers, String firstPlayer,
                                       int shepherd4player) {
        showInfo(
                "La partita ha " + numbOfPlayers + ", il primo giocatore è "
                + firstPlayer + ", ogni giocatore ha " + shepherd4player);

    }

    public void refereshCurrentPlayer(String currentPlayer) {
        showInfo("E' il turno di " + currentPlayer);

    }

    public void refreshBlackSheep(String result) {

        //splitta il risultato e raccogli l'outcome
        String[] token = result.split(",");
        String outcome = token[0];

        String diceValue = token[1];
        String startRegion = token[2];
        if ("ok".equalsIgnoreCase(outcome)) {
            String endRegion = token[3];
            showInfo(
                    "La pecora nera si è spostata dalla regione " + startRegion
                    + " alla regione " + endRegion + " passando per la strada di valore " + diceValue);
        } else if ("nok".equalsIgnoreCase(outcome)) {
            showInfo(
                    "La pecora nera non può muoversi, la strada di valore "
                    + diceValue + " è bloccata o non esiste nella regione " + startRegion);
        }

    }

    public void refreshWolf(String result) {
        //splitta il risultato e raccogli l'outcome
        String[] token = result.split(",");
        String outcome = token[0];

        if ("ok".equalsIgnoreCase(outcome)) {
            String fence = token[1];
            String ovine = token[2];
            String diceValue = token[3];
            String startRegion = token[4];
            String endRegion = token[5];
            if ("ok".equalsIgnoreCase(fence)) {
                showInfo(
                        "Il lupo si è mosso dalla regione " + startRegion + " alla regione " + endRegion
                        + " passando per la strada di valore " + diceValue + " e saltando la recinzione!");
            } else {
                showInfo(
                        "Il lupo si è mosso dalla regione " + startRegion + " alla regione " + endRegion
                        + " passando per la strada di valore " + diceValue);
            }
            if (!ovine.equalsIgnoreCase("nok")) {
                showInfo("Il lupo ha mangiato una " + ovine);
            }
        } else {
            String diceValue = token[1];
            String startRegion = token[2];
            showInfo(
                    "Il lupo non è riuscito a passare attraverso la strada di valore "
                    + diceValue + " nella regione " + startRegion);
        }
    }

    public String setUpShepherd(int shepherdIndex) {
        showInfo("Inserisci una strada per il pastore " + shepherdIndex);
        return stdIn.nextLine();
    }

    public String moveOvine() {
        showInfo("Inserisci il tipo di ovino da spostare:");
        String type = stdIn.nextLine();

        showInfo("Inserisci la regione di partenza:");
        String startRegion = stdIn.nextLine();

        showInfo("Inserisci la regione d'arrivo:");
        String endRegion = stdIn.nextLine();

        return startRegion + "," + endRegion + "," + type;
    }

    public void refreshMoveShepherd(String nickName, String shepherdIndex,
                                    String newStreet) {
        showInfo(
                "Il giocatore " + nickName + " ha posizionato il pastore " + shepherdIndex + " nella strada " + newStreet);

    }

    public void showSetShepherd(String shepherdIndex, String streetIndex) {
        showInfo("Pastore " + shepherdIndex + " posizionato in " + streetIndex);
    }

    public void refreshBuyLand(String buyer, String land, String price) {
        showInfo(
                "Il giocatore " + buyer + " ha acquistato un territorio " + land + " per " + price + " danari");

    }

    public void refereshCard(String type, int value) {
        showInfo("Hai una carta " + type + " di valore " + value);

    }

    public int chooseAction(int[] availableActions,
                            String[] availableStringedActions) {
        String stringToPrint = "";
        for (int i = 0; i < availableActions.length; i++) {
            stringToPrint += String.valueOf(availableActions[i]) + "- " + availableStringedActions[i] + " \n";
        }
        String choice;
        int action = -1;
        boolean correct = false;
        boolean actionFound;

        do {

            showInfo("Scegli un azione tra:\n" + stringToPrint);
            choice = stdIn.nextLine();
            try {
                action = Integer.parseInt(choice);
                actionFound = false;
                for (int i = 0; i < availableActions.length && !actionFound; i++) {
                    if (availableActions[i] == action) {
                        correct = true;
                        actionFound = true;
                    }
                }
                if (!actionFound) {
                    showInfo("Azione non valida.\nPrego riprovare.");
                }
            } catch (NumberFormatException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);
                showInfo("Azione non valida.\nPrego riprovare.");

            }
        } while (!correct);
        return action;
    }

    public void showInfo(String info) {
        stdOut.println();
        stdOut.println(info);
        stdOut.flush();
    }

    public void refreshMoveOvine(String nickName, String type,
                                 String startRegion,
                                 String endRegion) {
        showInfo(
                "Il giocatore " + nickName + " ha spostato un " + type + " da " + startRegion + " a " + endRegion);
    }

    public void moveShepherd(String startRegion, String endRegion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String askMoveShepherd() {
        showInfo("Scegliere quale pastore muovere:");
        String idShepherd = stdIn.nextLine();
        showInfo(
                "Scegliere in quale strada spostare il pastore " + idShepherd + ":");
        String stringedStreet = stdIn.nextLine();
        return idShepherd + "," + stringedStreet;
    }

    public String askMateSheepWith() {
        showInfo("Scegli il pastore vicino la regione dell'accoppiamento:");
        String idShepherd = stdIn.nextLine();

        showInfo("Scegli la regione dell'accoppiamento:");
        String region = stdIn.nextLine();
       
        return idShepherd + "," + region;
    }

    public void showWelcome() {
        this.showInfo("Benvenuto, il gioco sta per iniziare!");
        //per ora la welcome da tastiera è una sringa normale
    }

    public void showEndGame() {
        this.showInfo("Il gioco è terminato arrivederci!");
    }

    public String askBuyLand() {
        showInfo("Che territorio vuoi comprare?");
        return stdIn.nextLine();
    }

    public void showBoughtLand(String boughLand, String price) {
        showInfo(
                "Hai acquistato la carta " + boughLand + " per " + price + " danari");
    }

    public void showMoveShepherd(String priceToMove) {
        showInfo("Pastore spostato pagando " + priceToMove + " denari");
    }

    public void showMoveOvine(String startRegion, String endRegion, String type) {
        showInfo(
                "Hai spostato un " + type + " da " + startRegion + " a " + endRegion);
    }

    public void refreshMateSheepWith(String nickName, String region,
                                     String otherType, String newType) {
        showInfo(
                "Il giocatore " + nickName + " ha accoppiato una pecora con un "
                + otherType + " nella regione " + region + " ed è nato un " + newType);
    }

    public void showMateSheepWith(String region, String otherType,
                                  String newType) {
        showInfo(
                "Hai fatto accoppiare una pecora con un " + otherType
                + " nella regione " + region + " ed è nato un " + newType);
    }

}
