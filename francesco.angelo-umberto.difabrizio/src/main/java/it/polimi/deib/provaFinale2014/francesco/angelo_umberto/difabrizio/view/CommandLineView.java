package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * It's the view responsable to deal with the input-output from the console
 *
 * @author Umberto
 */
public class CommandLineView implements TypeOfViewController {

    private final PrintWriter stdOut = new PrintWriter(System.out);
    private final Scanner stdIn = new Scanner(System.in);

    /**
     * {@inheritDoc }
     *
     * @param regionIndex
     * @param numbOfSheep
     * @param numbOfRam
     * @param numbOfLamb
     */
    public void refreshRegion(int regionIndex, int numbOfSheep, int numbOfRam,
            int numbOfLamb) {
        showInfo("La regione " + regionIndex + " ora ha " + numbOfSheep
                + " pecore, " + numbOfLamb + " agnelli, " + numbOfRam + " montoni.");

    }

    /**
     * {@inheritDoc }
     *
     * @param streetIndex
     * @param fence
     * @param nickShepherd
     * @param shepherdIndex
     */
    public void refreshStreet(int streetIndex, boolean fence,
            String nickShepherd, int shepherdIndex) {
        stdOut.print("La strada " + streetIndex + " è ");

        if (fence) {
            stdOut.println(" recintata");
            stdOut.flush();

        } else if (nickShepherd != null && !"null".equals(nickShepherd)) {
            stdOut.println(
                    " occupata dal pastore " + shepherdIndex + " del giocatore " + nickShepherd);
            stdOut.flush();

        } else {
            stdOut.println(" libera");
            stdOut.flush();

        }
    }

    /**
     * {@inheritDoc }
     *
     * @param numbOfPlayers
     * @param firstPlayer
     * @param shepherd4player
     */
    public void refereshGameParameters(int numbOfPlayers, String firstPlayer,
            int shepherd4player) {
        showInfo(
                "La partita ha " + numbOfPlayers + ", il primo giocatore è "
                + firstPlayer + ", ogni giocatore ha " + shepherd4player);

    }

    /**
     * {@inheritDoc }
     *
     * @param currentPlayer
     */
    public void refereshCurrentPlayer(String currentPlayer) {
        showInfo("E' il turno di " + currentPlayer);

    }

    /**
     * {@inheritDoc }
     *
     * @param result
     */
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

    /**
     * {@inheritDoc }
     *
     * @param result
     */
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

    /**
     * {@inheritDoc }
     *
     * @param shepherdIndex
     *
     * @return
     */
    public String setUpShepherd(int shepherdIndex) {
        showInfo("Inserisci una strada per il pastore " + shepherdIndex);
        return stdIn.nextLine();
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    public String askMoveOvine() {
        showInfo("Inserisci il tipo di ovino da spostare:");
        String type = stdIn.nextLine();

        showInfo("Inserisci la regione di partenza:");
        String startRegion = stdIn.nextLine();

        showInfo("Inserisci la regione d'arrivo:");
        String endRegion = stdIn.nextLine();

        return startRegion + "," + endRegion + "," + type;
    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     * @param shepherdIndex
     * @param endStreet
     * @param price
     */
    public void refreshMoveShepherd(String nickName, int shepherdIndex,
            String endStreet, int price) {
        showInfo(
                "Il giocatore " + nickName + " ha posizionato il pastore "
                + shepherdIndex + " nella strada " + endStreet + " pagando "
                + price + " danari.");

    }

    /**
     * {@inheritDoc }
     *
     * @param shepherdIndex
     * @param streetIndex
     */
    public void showSetShepherd(int shepherdIndex, String streetIndex) {
        showInfo("Pastore " + shepherdIndex + " posizionato in " + streetIndex);
    }

    /**
     * {@inheritDoc }
     *
     * @param buyer
     * @param land
     * @param price
     */
    public void refreshBuyLand(String buyer, String land, int price) {
        showInfo(
                "Il giocatore " + buyer + " ha acquistato un territorio " + land + " per " + price + " danari");

    }

    /**
     * {@inheritDoc }
     *
     * @param type
     * @param value
     */
    public void refereshCard(String type, int value) {
        if (value != -2) {
            showInfo("Hai una carta " + type + " di valore " + value);
        }

    }

    /**
     * {@inheritDoc }
     *
     * @param availableActions
     * @param availableStringedActions
     *
     * @return
     */
    public String chooseAction(int[] availableActions,
            String[] availableStringedActions) {
        String stringToPrint = "";
        for (int i = 0; i < availableActions.length; i++) {
            stringToPrint += String.valueOf(availableActions[i]) + "- " + availableStringedActions[i] + " \n";
        }

        showInfo("Scegli un azione tra:\n" + stringToPrint);
        return stdIn.nextLine();

    }

    /**
     * {@inheritDoc }
     *
     * @param info
     */
    public void showInfo(String info) {
        stdOut.println();
        stdOut.println(info);
        stdOut.flush();
    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     * @param type
     * @param startRegion
     * @param endRegion
     */
    public void refreshMoveOvine(String nickName, String type,
            String startRegion,
            String endRegion) {
        showInfo(
                "Il giocatore " + nickName + " ha spostato un " + type + " da " + startRegion + " a " + endRegion);
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    public String askMoveShepherd() {
        showInfo("Scegliere quale pastore muovere:");
        String idShepherd = stdIn.nextLine();

        showInfo(
                "Scegliere in quale strada spostare il pastore " + idShepherd + ":");
        String stringedStreet = stdIn.nextLine();

        return idShepherd + "," + stringedStreet;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    public String askKillOvine() {
        showInfo("Scegliere quale pastore ucciderà l'ovino:");
        String idShepherd = stdIn.nextLine();

        showInfo("Scegliere in quale regione uccidere:");
        String stringedRegion = stdIn.nextLine();

        showInfo("Scegliere che tipo di ovino uccidere:\n-Lamb\n-Ram\n-Sheep");
        String type = stdIn.nextLine();

        return idShepherd + "," + stringedRegion + "," + type;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    public String askMateSheepWith() {
        showInfo("Scegli il pastore vicino la regione dell'accoppiamento:");
        String idShepherd = stdIn.nextLine();

        showInfo("Scegli la regione dell'accoppiamento:");
        String region = stdIn.nextLine();

        return idShepherd + "," + region;
    }

    /**
     * {@inheritDoc }
     */
    public void showWelcome() {
        this.showInfo("Benvenuto, il gioco sta per iniziare!");
        //per ora la welcome da tastiera è una sringa normale
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    public String askNickName() {

        stdOut.println("Inserisci il tuo nickName:");
        stdOut.flush();

        return stdIn.nextLine();
    }

    /**
     * {@inheritDoc }
     */
    public void showEndGame() {
        this.showInfo("Il gioco è terminato arrivederci!");
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    public String askBuyLand() {
        showInfo(
                "Che territorio vuoi comprare?\n-Plain\n-Countryside\n-Hill\n-Mountain\n-Desert\n-Lake");
        return stdIn.nextLine();
    }

    /**
     * {@inheritDoc }
     *
     * @param boughLand
     * @param price
     */
    public void showBoughtLand(String boughLand, String price) {
        showInfo(
                "Hai acquistato la carta " + boughLand + " per " + price + " danari");
    }

    /**
     * {@inheritDoc }
     *
     * @param region
     * @param type
     * @param shepherdPayed
     */
    public void showKillOvine(String region, String type, String shepherdPayed) {
        showInfo(
                "Hai ucciso un " + type + " nella regione " + region + " pagando " + shepherdPayed + " pastori per il silenzio");
    }

    /**
     * {@inheritDoc }
     *
     * @param idShepherd
     * @param priceToMove
     */
    public void showMoveShepherd(String idShepherd, String priceToMove) {
        showInfo(
                "Pastore " + idShepherd + " spostato pagando " + priceToMove + " denari");
    }

    /**
     * {@inheritDoc }
     *
     * @param startRegion
     * @param endRegion
     * @param type
     */
    public void showMoveOvine(String startRegion, String endRegion, String type) {
        showInfo(
                "Hai spostato un " + type + " da " + startRegion + " a " + endRegion);
    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     * @param region
     * @param otherType
     * @param newType
     * @param outcome
     */
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

    /**
     * {@inheritDoc }
     *
     * @param killer
     * @param region
     * @param type
     * @param outcome
     */
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

    /**
     * {@inheritDoc }
     *
     * @param region
     * @param otherType
     * @param newType
     */
    public void showMateSheepWith(String region, String otherType,
            String newType) {
        showInfo(
                "Hai fatto accoppiare una pecora con un " + otherType
                + " nella regione " + region + " ed è nato un " + newType);
    }

    /**
     * {@inheritDoc }
     *
     * @param money
     */
    public void refreshMoney(String money) {
        showInfo("Ora hai " + money + " denari");
    }

    /**
     * {@inheritDoc }
     *
     * @param winner
     * @param rank
     */
    public void showMyRank(Boolean winner, String rank) {
        if (winner) {
            showInfo("Hai vinto con " + rank + " punti!");
        } else {
            showInfo("Hai perso con " + rank + " punti!");
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param classification
     */
    public void showClassification(String classification) {
        showInfo("Classifica: ");
        String[] token = classification.split(",");
        int i = 0;
        while (i < token.length - 1) {
            showInfo("Giocatore " + token[i] + " punteggio: " + token[i + 1]);
            i += 2;
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param region
     */
    public void specialAnimalInitialCondition(String region) {
        String[] token = region.split(",");
        if ("Wolf".equals(token[0])) {
            showInfo("Il lupo si trova nella regione " + token[1]);
        } else if ("BlackSheep".equals(token[0])) {
            showInfo("La pecora nera si trova nella regione " + token[1]);
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param player
     */
    public void refreshPlayerDisconnected(String player) {
        showInfo("Il giocatore " + player + " si è disconnesso");
    }

    /**
     * {@inheritDoc }
     */
    public void showUnexpectedEndOfGame() {
        showInfo("Il gioco è terminato per mancanza di giocatori, si scusiamo.");
    }

    /**
     * {@inheritDoc }
     *
     * @param nickNames
     * @param wallets
     * @param shepherd4player
     */
    public void refreshGameParameters(String[] nickNames, int[] wallets,
            int shepherd4player) {
        showInfo("Giocatori:");
        for (int i = 0; i < nickNames.length; i++) {
            showInfo(nickNames[i] + " denari: " + wallets[i]);
        }
        showInfo("pastori per giocatore: " + shepherd4player);
    }

    /**
     * {@inheritDoc }
     *
     * @param fences
     */
    public void refreshFences(int fences) {
        showInfo("I recinti rimanenti sono: " + fences);
    }

    /**
     * {@inheritDoc }
     *
     * @param otherPlayer
     * @param money
     */
    public void refreshOtherPlayerMoney(String otherPlayer, int money) {
        showInfo("Il player " + otherPlayer + " ha " + money + " danari");
    }

    /**
     * {@inheritDoc }
     *
     * @param regionType
     * @param availableCards
     */
    public void refreshBankCard(String regionType, int availableCards) {
        showInfo(
                "Nella regione " + regionType + " ci sono ancora " + availableCards + " carte.");
    }

    public boolean askWillingTo(String action) {
        String choice;
        do {
            showInfo("Vuoi " + action + " una carta?(S/N)");
            choice = stdIn.nextLine();

            if ("S".equalsIgnoreCase(choice)) {
                return true;
            } else if ("N".equalsIgnoreCase(choice)) {
                return false;
            }
            showInfo("Scelta non valida. Riprovare.");
        } while (true);
    }

    public String askSellCard(String[] availableCards) {
        showInfo("Carte da vendere disponibili:");
        for (int i = 0; i < availableCards.length; i++) {
            showInfo("-" + availableCards[i]);
        }

        showInfo("Scegli una carta da vendere:");
        return stdIn.nextLine();
    }

    public int askPriceCard() {
        int price;
        do {
            try {
                showInfo("A quanto vuoi vendere la carta?");
                price = Integer.parseInt(stdIn.nextLine());
                return price;
            } catch (NumberFormatException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);
            }
            showInfo("Il prezzo inserito non è valido");
        } while (true);
    }

    public String askBuyMarketCard(String[] cards, int[] prices) {
        showInfo("Carte disponibili da acquistare:");

        for (int i = 0; i < cards.length; i++) {
            showInfo("-Carta: " + cards[i] + " prezzo: " + prices[i]);
        }
        showInfo("Scegli una carta da acquistare:");
        return stdIn.nextLine();
    }

}
