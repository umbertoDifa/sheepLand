package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.io.PrintWriter;
import java.util.Scanner;

public class CommandLineView implements TypeOfView {

    private final PrintWriter stdOut = new PrintWriter(System.out);
    private final Scanner stdIn = new Scanner(System.in);

    public CommandLineView() {

    }

    public void refreshRegion(int regionIndex, int numbOfSheep, int numbOfRam,
                              int numbOfLamb) {
        stdOut.println("La regione " + regionIndex + " ora ha " + numbOfSheep
                + " pecore, " + numbOfLamb + " agnelli, " + numbOfRam + " montoni.");
    }

    public void refreshStreet(int streetIndex, boolean fence,
                              String nickShepherd) {
        stdOut.println("La strada " + streetIndex + "è ");
        if (fence == true) {
            stdOut.println(" recintata");
        } else if (nickShepherd != null) {
            stdOut.println(" occupata da " + nickShepherd);
        } else {
            stdOut.println("libera");
        }
    }

    public void refereshGameParameters(int numbOfPlayers, String firstPlayer,
                                       int shepherd4player) {
        stdOut.println(
                "La partita ha " + numbOfPlayers + ", il primo giocatore è "
                + firstPlayer + ", ogni giocatore ha " + shepherd4player);
    }

    public void refereshCurrentPlayer(String currenPlayer) {
        stdOut.println("é il turno di " + currenPlayer);
    }

    public void refreshBlackSheep(int regionIndex) {
        stdOut.println("La pecora nera è nella regione " + regionIndex);
    }

    public void refreshWolf(int regionIndex) {
        stdOut.println("Il lupo è nella regione" + regionIndex);
    }

    public String setUpShepherd(int shepherdIndex) {
        stdOut.println("Inserisci una strada per il pastore " + shepherdIndex);
        return stdIn.nextLine();
    }

    public String moveOvine() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshMoveOvine(int startRegionIndex, int endRegionIndex,
                                 String type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void moveShepherd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshMoveShepherd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void buyLand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mateSheepWith() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshMateSheepWith(int regionIndex, String ovineType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void killOvine() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshKillOvine(int regionIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String askNickName() {
        return null; //TODO
    }

    public void refereshCard(String type, int value) {
        stdOut.println("Hai una carta " + type + "di valore " + value);
    }

    public int chooseAction(int[] availableActions,
                            String[] availableStringedActions) {
        String stringToPrint = "";
        for (int i : availableActions) {
            stringToPrint += String.valueOf(availableActions[i]) + "- " + availableActions[i] + " ";
        }
        String choice;
        int action = -1;
        boolean correct = false;

        do {
            stdOut.println("Inserire azione tra " + stringToPrint);
            choice = stdIn.nextLine();
            try {
                action = Integer.parseInt(choice);
                for (int i = 0; i < availableActions.length; i++) {
                    if (availableActions[i] == action) {
                        correct = true;
                        break;
                    }
                }
            } catch (NumberFormatException ex) {
                stdOut.println("Azione non valida.\nPrego riprovare:");
            }
        } while (!correct);
        return action;
    }

    public int askIdShepherd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String askStreet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void showInfo(String info) {
        stdOut.println(info);
    }

    public void moveOvine(String type, String startRegion, String endRegion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
