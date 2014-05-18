package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class BuyLand extends Action{

    private BuyLand() {
    }
    
    public static BuyLand getInstance(){
        return new BuyLand();
    }
    @Override
    public void execute() {
        //chiedi quale comprare
        //verifica se è possibile (deve esistere un terreno di quel tipo e il giocatore deve avere soldi)
        //ritorna il terreno o l'avviso di impossibilità
    }

}
