package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.MissingCardException;
import java.util.ArrayList;

/**
 * Contiene le carte che non sono state ancora vendute e i recinti non ancora
 * usati. Se chiesto può tornare sia una carta che un recinto.
 *
 * @author Francesco
 */
public class Bank {

    private Card[] unusedCards;
    private ArrayList<Card> initialCards = new ArrayList<Card>();
    private Fence[] unusedFences; //TODO: magari inseriamo un margine di sicurezza? se mi arriva una FinishedFenceException 
    //mi si sputtana tutto

    public Bank(int numCards, int numInitialCard, int numeFences) {
        this.unusedCards = new Card[numCards];
        this.unusedFences = new Fence[numeFences];
        //creo le carte iniziali
        for (int i = 0; i < numInitialCard; i++) {//per tutte le carte iniziali da dare
            
            //creo una carta con valore 0
            //e con tipo RegionType ciclicamente ovvero partendo dal primo tipo
            //fino all'ultimo, se le carte da distribuire sono più dei terreni
            //allora ricomincio dalla prima
            this.initialCards.add(new Card(0,
                    RegionType.values()[i % numInitialCard]));
        }
    }
    /**
     * Ritorna una carta tra quelle iniziali e la rimuove da quelle disponibili
     * @return Una carta il cui tipo è casuale ma unico nella lista 
     */
    //TODO: guarda che sta roba manda un ArrayOutOfBound se non ha carte quindi boh...
    public Card getInitialCard() {
       Card returnableCard = this.initialCards.get(0);//prendi la prima carta della lista
       this.initialCards.remove(0); //rimuovila dalla lista
       return returnableCard; //ritornala
    }
    
    /**
     * Cerca una carta del tipo specificato nell'array delle carte disponibili e
     * la ritorna se esiste, altrimenti solleva un eccezione se le carte (di
     * quel tipo) sono finite
     *
     * @param type Tipo di carta voluto
     *
     * @return una Card del tipo chiesto
     *
     * @throws MissingCardException Se la carta non c'è
     */
    public Card getCard(RegionType type) throws MissingCardException {
        String missingCardMessage = "Non ci sono più carte per il tipo " + type.toString(); //preparo un messaggio di errore
        //l'algoritmo che segue a come idea di indicizzare l'array
        //le carte vengono caricate nell'array di quelle a disposizione della 
        //banca in maniera ordinata
        for (int i = type.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue();
                i < (type.getIndex() + 1) * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); i++) {
            if (this.unusedCards[i] != null) {
                Card foundedCard = this.unusedCards[i];
                this.unusedCards[i] = null;
                return foundedCard;
            }
        }
        throw new MissingCardException(missingCardMessage);
    }

    public Fence getFence() throws FinishedFencesException {
        int position = this.numberOfUsedFence(); //questo valore avrà sempre senso per come è implementata la numberOfUsedFence

        Fence returnableFence = unusedFences[this.numberOfUsedFence()]; //salva il recinto

        unusedFences[this.numberOfUsedFence()] = null; //eliminalo dall'array
        return returnableFence;//ritorna il recinto

    }

    /**
     * Crea un recinto non finale e lo aggiunge all'array dei recinti non usati
     * nella posizione specificata
     *
     * @param position Posizione in cui inserire il recinto
     */
    public void loadFence(int position) {
        unusedFences[position] = new Fence(false);
    }

    /**
     * Crea un recinto finale e lo aggiunge all'array dei recinti non usati
     * nella posizione specificata
     *
     * @param position Posizione in cui inserire il recinto
     */
    public void loadFinalFence(int position) {
        unusedFences[position] = new Fence(true);
    }

    /**
     * Riceve una card e la posiziona dove richiesto nell'array delle carte non
     * usate(del banco)
     *
     * @param card     Carta da caricare
     * @param position Posizione in cui caricarla
     */
    public void loadCard(Card card, int position) {
        unusedCards[position] = card;
    }

    /**
     * Restitutisce il numero di recinti ceduti dalla banca lancia l'eccezione
     * FinishedFencesException
     *
     * @return Il numero di recinti usati
     *
     * @throws
     * it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException
     */
    public int numberOfUsedFence() throws FinishedFencesException {
        int i; //serve a contare quanti recinti sono stati dati
        for (i = 0; i < unusedFences.length; i++) { //scorri l'array
            if (unusedFences[i] != null) //al primo recinto disponibile               
            {
                return i;         //restituisci quanti ne sono stati dati        }            
            }
        }
        throw new FinishedFencesException("I recinti sono terminati");
    }

}
