package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandLineView implements TypeOfViewController {

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

        if (fence) {
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
                    "La pecora nera si è spostata da la regione " + startRegion
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
                        + " passando sulla strada di valore " + diceValue + " e saltando la recinzione!");
            } else {
                showInfo(
                        "Il lupo si è mosso dalla regione " + startRegion + " alla regione " + endRegion
                        + " passando per la strada di valore " + diceValue);
            }
            if (!"nok".equalsIgnoreCase(ovine)) {
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

    public String askMoveOvine() {
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
        boolean rightFormat;

        do {
            showInfo("Scegli un azione tra:\n" + stringToPrint);
            choice = stdIn.nextLine();
            try {
                action = Integer.parseInt(choice);
                rightFormat = true;
            } catch (NumberFormatException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);
                showInfo("Azione non valida.\nPrego riprovare.");
                rightFormat = false;
            }
        } while (!rightFormat || !actionExists(availableActions, action));
        return action;
    }

    private boolean actionExists(int[] availableActions, int action) {
        for (int i = 0; i < availableActions.length; i++) {
            if (availableActions[i] == action) {
                return true;
            }
        }
        showInfo("Azione non esistente.\nPrego riporvare:");
        return false;
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

    public String askKillOvine() {
        showInfo("Scegliere quale pastore ucciderà l'ovino:");
        String idShepherd = stdIn.nextLine();

        showInfo("Scegliere in quale regione uccidere:");
        String stringedRegion = stdIn.nextLine();

        showInfo("Scegliere che tipo di ovino uccidere:\n-Lamb\n-Ram\n-Sheep");
        String type = stdIn.nextLine();

        return idShepherd + "," + stringedRegion + "," + type;
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
        showInfo(
                "Che territorio vuoi comprare?\n-Plain\n-Countryside\n-Hill\n-Mountain\n-Desert\n-Lake");
        return stdIn.nextLine();
    }

    public void showBoughtLand(String boughLand, String price) {
        showInfo(
                "Hai acquistato la carta " + boughLand + " per " + price + " danari");
    }

    public void showKillOvine(String region, String type, String shepherdPayed) {
        showInfo(
                "Hai ucciso un " + type + " nella regione " + region + " pagando " + shepherdPayed + " pastori per il silenzio");
    }

    public void showMoveShepherd(String priceToMove) {
        showInfo("Pastore spostato pagando " + priceToMove + " denari");
    }

    public void showMoveOvine(String startRegion, String endRegion, String type) {
        showInfo(
                "Hai spostato un " + type + " da " + startRegion + " a " + endRegion);
    }

    public void refreshMateSheepWith(String nickName, String region,
                                     String otherType, String newType,
                                     String outcome) {
        if ("ok".equals(outcome)) {
            showInfo(
                    "Il giocatore " + nickName + " ha accoppiato una pecora con un "
                    + otherType + " nella regione " + region + " ed è nato un " + newType + "!");
        } else {
            showInfo(
                    "Il giocatore " + nickName + " ha tentato di accoppiare una pecora con un " + otherType + " ma ha fallito!");
        }

    }

    public void refreshKillOvine(String killer, String region, String type,
                                 String outcome) {
        if ("ok".equals(outcome)) {
            showInfo(
                    "Il giocatore " + killer + " ha ucciso un ovino " + type + " nella regione " + region);
        } else {
            showInfo(
                    "Il giocatore " + killer + " ha tentato di uccidere un " + type + " nella regione " + region);
        }
    }

    public void showMateSheepWith(String region, String otherType,
                                  String newType) {
        showInfo(
                "Hai fatto accoppiare una pecora con un " + otherType
                + " nella regione " + region + " ed è nato un " + newType);
    }

    public void refreshMoney(String money) {
        showInfo("Ora hai " + money + " denari");
    }

    public void showMyRank(Boolean winner, String rank) {
        if (winner) {
            showInfo("Hai vinto con " + rank + " punti!");
        } else {
            showInfo("Hai perso con " + rank + " punti!");
        }
    }

    public void showClassification(String classification) {
        showInfo("Classifica: ");
        String[] token = classification.split(",");
        int i = 0;
        while (i < token.length - 1) {
            showInfo("Giocatore :" + token[i] + " punteggio: " + token[i + 1]);
            i += 2;
        }
    }

    public void specialAnimalInitialCondition(String region) {
        String[] token = region.split(",");
        if ("Wolf".equals(token[0])) {
            showInfo("Il lupo si trova nella regione " + token[1]);
        } else if ("BlackSheep".equals(token[0])) {
            showInfo("La pecora nera si trova nella regione " + token[1]);
        }
    }

    public void refreshPlayerDisconnected(String player) {
        showInfo("Il giocatore " + player + " si è disconnesso");
    }

    public void showUnexpectedEndOfGame() {
        showInfo("Il gioco è terminato per mancanza di giocatori, si scusiamo.");
    }

}
