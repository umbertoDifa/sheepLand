//
//********************Prova di implementazione diversa di alcune funzioni del gameManager
//
//package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;
//
//import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionNotFoundException;
//import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.CannotMoveBlackSheepException;
//import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.CannotMoveWolfException;
//import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException;
//import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Bank;
//import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Card;
//import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Dice;
//import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.GameConstants;
//import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Map;
//import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Ovine;
//import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Region;
//import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.RegionType;
//import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
//import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.BusyStreetException;
//import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
//import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.ServerThread;
//import java.util.ArrayList;
//import java.util.Random;
//
///**
// * E' il controllo della partita. Si occupa di crearne una a seconda del numero
// * dei giocatori.
// *
// * @author francesco.angelo-umberto.difabrizio
// */
//public class GameManager2 {//TODO: pattern memento per ripristini?
////TODO: dividere la classe fra GameSetupper e GameManager? è grossa...
//
//    private final ServerThread server;
//    
//    private final Map map;
//    private ArrayList<Player> players = new ArrayList<Player>();
//    private final int playersNumber;
//    private int firstPlayer; //rappresenterà il segnalino indicante il primo giocatore del giro
//    private final int[] playersHashCode; //valore cached degli hash dei giocatori
//
//    private final Bank bank;
//
//    /**
//     * Crea un GameManager
//     *
//     * @param playersNumber Numero dei giocatori di una partita
//     * @param server        Thread che gestisce la partita
//     */
//    public GameManager2(int playersNumber, ServerThread server) {
//        this.playersNumber = playersNumber;
//        this.playersHashCode = new int[playersNumber]; //creo un array della dimensione del numero dei player
//        this.map = new Map(); //creo la mappa univoca del gioco
//        this.server = server; //creo il collegamento all'univoco serverThread
//        this.setUpPlayers(playersNumber); //setto arraylist giocatori e array hashcode giocatori
//        this.bank = new Bank(GameConstants.NUM_CARDS.getValue(),
//                GameConstants.NUM_FENCES.getValue());
//    }
//
//    /**
//     * Dato il numero di player, riempie l'arraylist dei player e riempie
//     * l'array dei rispettivi hashcode
//     *
//     * @param numbPlayer
//     */
//    //FIXME: questo è commentato perchè da un errore da aggiustare dopo
//    private void setUpPlayers(int numbPlayer) {
//        for (int i = 0; i < playersNumber; i++) { //per ogni giocatore
//            players.add(new Player(this)); //FIXME dovuto al nome della classe
//            //lo aggiungo alla lista dei giocatori
//            playersHashCode[i] = players.get(i).hashCode();//salvo il suo hashcode
//        }
//    }
//
//    /**
//     * Metodo principale che viene invocato dal server thread per creare tutti
//     * gli oggetti di una partita e avviarla
//     */
//    private void SetUpGame() {
//        this.setUpMap();
//        this.setUpSocketPlayerMap();
//        this.setUpAnimals();
//        this.setUpShepherds();
//        this.setUpCards();
//        this.setUpFences();
//        this.setUpShift();
//    }
//
//    /**
//     * Per ogni terreno regione della mappa aggiungi un collegamento ad un
//     * animale il cui tipo è scelto in maniera randomica
//     */
//    private void setUpAnimals() {
//        int SHEEPSBURG_ID = 18;
//        Region[] region = this.map.getRegions();//recupera l'array delle regioni
//        for (Region reg : region) { //per ogni regione
//            reg.addOvine(new Ovine());//aggiungi un ovino (a caso)          
//        }
//        //posiziono lupo e pecora nera a sheepsburg
//        map.getBlackSheep().setAt(map.getRegions()[SHEEPSBURG_ID]);
//        map.getWolf().setAt(map.getRegions()[SHEEPSBURG_ID]);
//    }
//
//    /**
//     * Chiede ad ogni giocatore dove posizionare il proprio pastore
//     */
//    private void setUpShepherds() {
//        Street chosenStreet = new Street(0); //HACK: creo cmq una strada con valore 0 così non ho errori 
//        //perchè la variabile potrebbe essere null fuori dal while infatti tutti i controlli necessari li faccio
//        //in askStreet e nelle funzioni da lei chiamate che mi ridanno una delle eccezioni che gestisco
//        for (int i = 0; i < this.playersNumber; i++) {  //ciclo che chiede la strada ad ogni pastore
//            //chiedo la strada finchè non è valida
//            chosenStreet = (Street) this.askUntilRightAnswer(new StreetAsker(i));
//            //se arrivo qua ho  gestito i casi di strada non esistente o occupata 
//            this.players.get(i).getShepherd().moveTo(chosenStreet); //sposta il pastore 
//            //creo una carta con valore 0 e di tipo casuale e l'aggiungo a 
//            //quelle del pastore corrispondente al mio player
//            RegionType randomType = RegionType.getRandomRegionType();
//            this.players.get(i).getShepherd().addCard(new Card(0, randomType
//            )); //aggiungi la carta
//            //invia conferma riepilogativa
//            this.server.sendTo(this.playersHashCode[i],
//                    "Pastore posizionato. Hai una carta terreno di tipo: " + randomType.toString());
//        }
//        
//    }
//    
//    private void setUpShift() {
//        //creo oggetto random
//        Random random = new Random();
//        //imposto il primo giocatore a caso tra quelli presenti
//        this.firstPlayer = random.nextInt(this.playersNumber);
//    }
//
//    /**
//     * chiama l'omonimo metodo della Map
//     */
//    private void setUpMap() {
//        this.map.setUp();
//    }
//
//    /**
//     * chiama l'omonimo metodo del serverThread
//     */
//    private void setUpSocketPlayerMap() {
//        this.server.setUpSocketPlayerMap(playersHashCode);
//    }
//
//    /**
//     * Carica i recinti finali e non nel banco
//     */
//    private void setUpFences() {
//        int i; // counter
//        //salvo il numero di recinti non finali
//        int numbOfNonFinalFences = GameConstants.NUM_FENCES.getValue() - GameConstants.NUM_FINAL_FENCES.
//                getValue();
//        //carico il numero di recinti non finali in bank
//        for (i = 0; i < numbOfNonFinalFences; i++) {
//            bank.loadFence(i);
//        }
//        //carico i recinti finali in bank. 'i' parte da dove l'ha lasciato il 
//        //ciclo sopra è arriva alla fine dell'array
//        for (; i < GameConstants.NUM_FENCES.getValue(); i++) {
//            bank.loadFinalFence(i);
//        }
//    }
//
//    /**
//     * Crea le carte di tutti i tipi necessari da caricare nel banco
//     */
//    private void setUpCards() {
//        for (int i = 0; i < RegionType.values().length - 1; i++)            //per ogni tipo di regione - sheepsburg  
//            for (int j = 0; j < GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); j++) { //per tante quante sono le carte di ogni tipo
//                //crea una carta col valore giusto( j crescente da 0 al max) e tipo giusto(dipendente da i) 
//                Card cardToAdd = new Card(j, RegionType.values()[i]);
//                //calcola la posizione a cui aggiungerla(fondamentale per la getCard che
//                //usa una politica basata sugli indici)
//                int position = (i * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue()) + j;
//                //caricala
//                bank.loadCard(cardToAdd, position);
//            }
//        
//    }
//    
//    private void playTheGame() {
//        this.executeRounds();
//        //this.calculatePoints(); //TODO
//        //this.broadcastWinner(); //TODO
//    }
//    
//    public void startGame() {
//        this.SetUpGame();
//        this.playTheGame();
//        //gameFinished
//    }
//    
//    private void executeRounds() {
//        int currentPlayer = this.firstPlayer;
//        boolean lastRound = false;
//        
//        while (!(lastRound && currentPlayer == this.firstPlayer)) {//se non è l'ultimo giro o il giocatore non è l'ultimo del giro
//            try { //prova a fare un turno
//                this.executeShift(currentPlayer);
//            } catch (FinishedFencesException e) { //se finiscono i recinti
//                //i recinti sono finiti chiama l'ultimo giro
//                lastRound = true;
//            } finally {//comunque vada
//                //aggiorno il player che gioca in modulo playersNumber
//                currentPlayer++;
//                currentPlayer %= this.playersNumber;
//                //controllo se ho il fine giro per muovere il lupo
//                if (currentPlayer == this.firstPlayer) {//se il prossimo a giocare è il primo del giro
//                    //calcolo su quale strada mi vorrebbe far passare il dado sapendo di partire dalla regione del lupo
//                    Region actualWolfRegion = this.map.getWolf().getMyRegion();
//                    Street potentialWalkthroughStreet = this.map.getStreetByValue(
//                            actualWolfRegion,
//                            Dice.getRandomValue());
//                    try {
//                        if (potentialWalkthroughStreet != null) {
//                            Region endRegion = this.map.getEndRegion(
//                                    actualWolfRegion,
//                                    potentialWalkthroughStreet);
//                            //cerco di farcelo passare
//                            this.map.getWolf().moveThrough(
//                                    potentialWalkthroughStreet, endRegion);
//                            //se tutto ok
//                            //TODO:broadcast lupo mosso
//                        } else {
//                            throw new CannotMoveWolfException(
//                                    "La strada non esiste.");
//                        }
//                    } catch (CannotMoveWolfException ex) {//se non può avviso e lo lascio dov'è
//                        //TODO: brodcast lupo can't move
//                    }
//                }
//            }
//        }//while
//        //fine round
//    }
//    
//    private void executeShift(int player) throws FinishedFencesException {
//        String noMoreFenceMessage = "Recinti Finiti!";
//        //TODO SUPERIMPORTANTE: il tentativo di muovere la pecora nera qua sotto
//        //è praticamente identico a quelle del lupo....metodino?
//        //cerca la strada che potrebbe attraversare la pecora nera
//        try {
//            //salvo la regione in cui si trova la pecora
//            Region actualBlacksheepRegion = this.map.getBlackSheep().getMyRegion();
//            //cerco la strada che dovrebbe attraversare
//            Street potentialWalkthroughStreet = this.map.getStreetByValue(
//                    actualBlacksheepRegion,
//                    Dice.getRandomValue());
//            
//            if (potentialWalkthroughStreet != null) {//se esiste
//                Region endRegion = this.map.getEndRegion(actualBlacksheepRegion,
//                        potentialWalkthroughStreet);
//                //cerco di far passare la pecora nera
//                this.map.getBlackSheep().moveThrough(potentialWalkthroughStreet,
//                        endRegion);
//                //tutto ok
//                //TODO:broadcast pecora mossa
//            } else {
//                throw new CannotMoveBlackSheepException("La strada non esiste");
//            }
//        } catch (CannotMoveBlackSheepException ex) {
//            //TODO: broadcast sheep stays in place
//        }
//        for (int i = 0; i < GameConstants.NUM_ACTIONS.getValue(); i++) {//per il numero di azioni possibili per un turno
//
//            while (true) {
//                try {
//                    this.players.get(i).chooseAndMakeAction(); //scegli l'azione e falla
//                    break; //se non arriva l'eccezione
//                } catch (ActionNotFoundException ex) {
//                    //avvisa e riavvia la procedura di scelta dell'i-esima azione
//                    this.server.sendTo(playersHashCode[i], ex.getMessage());
//                }
//            }
//        }
//        if (this.bank.numberOfUsedFence() >= GameConstants.NUM_FENCES.getValue() - GameConstants.NUM_FINAL_FENCES.getValue()) {
//            throw new FinishedFencesException(noMoreFenceMessage); //occhio che questo è lanciato per ogni turno dell'ultimo giro
//        }
//    }
//    
//    private Object askUntilRightAnswer(Query query) {
//        while (true) {
//            try {
//                return query.execute();//se tutto va secondo i piani ritorno l'oggetto
//            } catch (Exception e) {//altrimenti avviso dell'errore e ripeto la richiesta
//                //TODO check
//                this.server.sendTo(query.player, "Errore: " + e.getMessage());
//            }
//        }//while
//    }
//    
//    private abstract class Query {
//        
//        private int player;
//        
//        public abstract Object execute() throws Exception;
//        
//        public void setPlayer(int player) {
//            this.player = player;
//        }
//        
//        public int getPlayer() {
//            return player;
//        }
//        
//    }
//    
//    private class StreetAsker extends Query {
//        
//        public StreetAsker(int player) {
//            super.setPlayer(player);
//        }
//
//        //funziona tutto, testato hardodando il valore della strada
//        //se c'è un errore controllare la connessione
//        @Override
//        public Object execute() throws StreetNotFoundException,
//                                       BusyStreetException {
//            String errorString = "Strada già occupata, prego riprovare:";
//            
//            String stringedStreet = GameManager2.this.server.talkTo(
//                    GameManager2.this.playersHashCode[super.getPlayer()],
//                    "In quale strada vuoi posizionare il pastore?"); //raccogli decisione
//            //traducila in oggetto steet 
//            Street chosenStreet = map.convertStringToStreet(stringedStreet);
//            if (!chosenStreet.isFree()) { //se la strada è occuapata
//                throw new BusyStreetException(errorString); //solleva eccezione
//            }
//            return chosenStreet; //altrimenti ritorna la strada
//        }
//        
//    }
//    
//    private class ActionAsker extends Query {
//
//        public ActionAsker(int player) {
//            super.setPlayer(player);
//        }
//        
//        @Override
//        public Object execute() throws Exception {
//            return  GameManager2.this.players.get(super.getPlayer()).chooseAndMakeAction(); //scegli l'azione e falla
//        }
//    }
//    
//    public ServerThread getServer() {
//        return server;
//    }
//    
//    public Map getMap() {
//        return map;
//    }
//    
//}
