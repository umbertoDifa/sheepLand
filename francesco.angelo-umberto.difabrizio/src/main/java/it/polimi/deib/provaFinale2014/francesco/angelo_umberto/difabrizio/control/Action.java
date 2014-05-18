package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

/**
 * Contiene un tipo di azione fra quelle possibili Crea azioni secondo il
 * pattern abstract factory
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public abstract class Action {//TODO: interface?

    public abstract void execute();

    public static Action make(int scelta) {
        switch (scelta) {
            case 1:
                return  MoveSheep.getInstance();
            case 2:
                return MoveShepherd.getInstance();
            case 3:
                return  BuyLand.getInstance();
            case 4:
                return  MateSheeps.getInstance();
            case 5:
                return MateSheepAndRam.getInstance();
            case 6:
                return KillSheep.getInstance();
        }
        return null; //TODO: perche devo mette sta roba?
    }
}
