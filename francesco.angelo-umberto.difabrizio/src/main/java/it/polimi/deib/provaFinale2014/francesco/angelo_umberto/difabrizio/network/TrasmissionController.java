package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import java.util.ArrayList;

/**
 *
 * @author
 */
public interface TrasmissionController {
   
    public String refreshRegion(String nickName);
    
    public String refreshStreet(String nickName);
    
    public String refreshGameParameters(String nickName);
    
    public String refreshCurrentPlayer(String nickName);
    
    public String refreshCards(String nickName);
    
    public String refreshBlackSheep(String nickName);
    
    public String refreshWolf(String nickName);
    
    public String refreshMoveOvine(String nickName);
    
    public String refreshMoveShepherd(String nickName);
    
    public String refreshKillOvine(String nickName);
        
    //ritorna una stringa corripondente o a una strada o il risultato della chiamata RMI
    public String askSetUpShepherd(String nickName);
    
    //ritorna una stringa corrispondente all'azione scelta, sia per socket che rmi
    public String askChooseAction(String nickName);
    
    //ritorna una stringa con regione di partenza e di arrivo o risultato RMI !!!!TORNA DUE
    public String askMoveOvine(String nickName);
    
    //ritorna una stringa corrispondente a una strada o risultato RMI
    public String askMoveSheperd(String nickName);
    
    //ritorna una stringa corrispondente al tipo di regione
    public String buyLand(String nickName);
    
    //ritorna stringa corrispondente a Regione e Tipo di ovino
    public String askKillOvine(String nickName);
    
    //ritorna una stringa corrispondente alla regione e Tipo di ovino
    public String askMateSheepWith(String nickName);
    
    public void askThrowDice(String nickName);
    
            
    public String 
//
//    public void broadcastRegion();
//
//    public void refreshAll();
//
//    public int askRegion();
//
//    public String askStreet(String nickName, int idShepherd);
//    
//    public void sendTo(String nickName, String message);
    
}
