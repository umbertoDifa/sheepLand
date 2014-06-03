package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.MissingCardException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Contiene le carte che non sono state ancora vendute e i recinti non ancora
 * usati. Se chiesto può tornare sia una carta che un recinto.
 *
 * @author Francesco
 */
public class Bank {

    private Card[] unusedCards;
    private List<Card> initialCards = new ArrayList<Card>();
    private Fence[] unusedFences;

    /**
     * Costruisce un banco settando il numero di carte da vendere, il numero di
     * carte da distribuire ai giocatori all'inizio del gioco e il numero di
     * recinti totali disponibili
     *
     * @param numCards       Carte del banco da vendere
     * @param numInitialCard Carte da distribuire all'inizio del gioco
     * @param numeFences     Numero di recinti totali(finali + non finali)
     */
    public Bank(int numCards, int numInitialCard, int numeFences) {
        this.unusedCards = new Card[numCards];
        this.unusedFences = new Fence[numeFences];
        //creo le carte iniziali
        //per tutte le carte iniziali da dare
        for (int i = 0; i < numInitialCard; i++) {

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
     *
     * @return Una carta il cui tipo è casuale ma unico nella lista
     */
   
    public Card getInitialCard() {
        //crea oggetto random
        Random random = new Random();

        //trova index casuale tra quelli disponibili
        int index = random.nextInt(this.initialCards.size());

        //prendi la carta 
        Card returnableCard = this.initialCards.get(index);

        //rimuovila dalla lista
        this.initialCards.remove(index);
        return returnableCard; //ritornala
    }

    /**
     * Cerca una carta del tipo specificato nell'array delle carte disponibili e
     * la ritorna se esiste eliminandola da quelle disponibili altrimenti
     * solleva un eccezione se le carte (di quel tipo) sono finite
     *
     * @param type Tipo di carta voluto
     *
     * @return una Card del tipo chiesto
     *
     * @throws MissingCardException Se la carta non c'è
     */
    public Card getCard(RegionType type) throws MissingCardException {
        //salvo la carta da restituire
        Card cardToReturn = this.findCard(type);

        //elimino il suo riferimento nell'array
        removeCard(cardToReturn);

        //ritorno la carta
        return cardToReturn;
    }

    /**
     * Il metodo elimina una carta dall'array delle carte della banca ponendo il
     * suo riferimento a null. Non gestisce casi di ArrayOutOfBounds perche
     * viene chiamata quando il controllo è stato già fatto da un'altra funzione
     * tipicamente findCard
     *
     * @param deleteMe Carta da eliminare
     */
    private void removeCard(Card deleteMe) {
        //per ogni carta
        for (int i = 0; i < unusedCards.length; i++) {

            //se la carta è quella da eliminare
            if (unusedCards[i] == deleteMe) {

                //impostala a null
                unusedCards[i] = null;
                return;
            }
        }
    }
    /**
     * Given a type of card it returns its price, which means the price of 
     * the first available card of that type in the bank
     * @param type Type of card
     * @return The price
     * @throws MissingCardException If no card of that type is found
     */
    public int getPriceOfCard(RegionType type) throws MissingCardException {
        //chiamo la find card e ritorno il prezzo
        if (type != RegionType.SHEEPSBURG) {
            return this.findCard(type).getValue();
        }
        else return GameConstants.PRICE_FOR_SHEEPSBURG.getValue();
    }

    private Card findCard(RegionType type) throws MissingCardException {
        //l'algoritmo che segue a come idea di indicizzare l'array
        //le carte vengono caricate nell'array di quelle a disposizione della 
        //banca in maniera ordinata

        for (int i = type.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue();
                i < (type.getIndex() + 1) * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); i++) {
            if (this.unusedCards[i] != null) {
                return this.unusedCards[i];
            }
        }
        throw new MissingCardException(
                "Non ci sono più carte per il tipo " + type.toString());
    }
    /**
     * It returns a fence taking it from tha unused ones
     * @return A fence
     * @throws FinishedFencesException If there are no more fences
     */
    public Fence getFence() throws FinishedFencesException {
        //questo valore avrà sempre senso per come è implementata la numberOfUsedFence
        int position = this.numberOfUsedFence();

        //salva il recinto
        Fence returnableFence = unusedFences[this.numberOfUsedFence()];

        //eliminalo dall'array
        unusedFences[this.numberOfUsedFence()] = null;
        return returnableFence;

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
     * Riceve una card e la posiziona nel primo posto vuoto delle carte non
     * usate(del banco). Il metodo se necessario inserisce anche un carta con
     * tipo shepsburg. Se si cerca di inserire più carte di quelle che l'array
     * può contenere si verificherà un errore. Il metodo non mi permettere di
     * caricare in una specifica posizione, nel caso questo sia voluto è
     * necessario riempire nell'ordine voluto l'array
     *
     * @param card Carta da caricare
     */
    public void loadCard(Card card) {
        for (int i = 0; i < GameConstants.NUM_CARDS.getValue(); i++)
            if (unusedCards[i] == null) {
                unusedCards[i] = card;
                return;
            }
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
        int i;
        //scorri l'array
        for (i = 0; i < unusedFences.length; i++) {
            //al primo recinto disponibile               
            if (unusedFences[i] != null) {
                //restituisci quanti ne sono stati dati
                return i;
            }
        }
        throw new FinishedFencesException("I recinti sono terminati");
    }

}
