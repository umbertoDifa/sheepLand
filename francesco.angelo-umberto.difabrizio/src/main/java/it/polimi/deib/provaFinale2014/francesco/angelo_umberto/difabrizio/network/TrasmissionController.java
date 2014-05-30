package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.Player;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author
 */
public abstract class TrasmissionController {

    private HashMap<String,Player> nick2PlayerMap = new HashMap<String, Player>();

    public void setNick2PlayerMap(String[] nickNames, List<Player> players) {
        for(int i=0; i<nickNames.length; i++){
            nick2PlayerMap.put(nickNames[i], players.get(i));
        }
    }

    public abstract void refreshRegion(String nickName, int regionIndex,
            int numbOfSheep, int numbOfRam,
            int numbOfLamb);

    public abstract void refreshStreet(String nickName, int streetIndex, boolean fence,
            String nickNameOfShepherdPlayer);

    public abstract void refreshGameParameters(String nickName);

    public abstract void refreshCurrentPlayer(String nickName);

    public abstract void refreshCard(String nickName, String card, int value);

    public abstract void refreshBlackSheep(String message);

    public abstract void refreshWolf(String nickName);

    public abstract void refreshMoveOvine(String nickName, String startRegion, String endRegion, String ovineType);

    public abstract void refreshMoveShepherd(String nickNameMover,
            String newStreet);

    public abstract void refreshKillOvine(String nickName);

    //ritorna una stringa corripondente o a una strada o il risultato della chiamata RMI
    public abstract boolean askSetUpShepherd(String nickName, int shepherdIndex);

    //ritorna una stringa corrispondente all'azione scelta, sia per socket che rmi
    public abstract boolean askChooseAction(String nickName, String possibleActions);

    //ritorna una stringa con regione di partenza e di arrivo o risultato RMI !!!!TORNA DUE
    public abstract boolean askMoveOvine(String nickName);

    //ritorna una stringa corrispondente a una strada o risultato RMI
    public abstract boolean askMoveSheperd(String nickName);

    //ritorna una stringa corrispondente al tipo di regione
    public abstract String buyLand(String nickName);

    //ritorna stringa corrispondente a Regione e Tipo di ovino
    public abstract String askKillOvine(String nickName);

    //ritorna una stringa corrispondente alla regione e Tipo di ovino
    public abstract String askMateSheepWith(String nickName);

    public abstract void askThrowDice(String nickName);

    public abstract void refreshInfo(String nickName, String info);

    public HashMap<String, Player> getNick2PlayerMap() {
        return nick2PlayerMap;
    }

}
