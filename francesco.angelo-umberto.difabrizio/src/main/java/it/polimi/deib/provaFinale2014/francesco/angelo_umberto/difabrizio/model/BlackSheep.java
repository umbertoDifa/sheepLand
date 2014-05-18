package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.CannotMoveBlackSheepException;

public class BlackSheep extends SpecialAnimal {

    @Override
    public void setAt(Region region) {
        super.setMyRegion(region);
    }

    @Override
    public void moveThrough(Street street) throws CannotMoveBlackSheepException {//TODO: da fare
        //se non c'è un recinto e se esiste la strada uscita col dado
        //e se la strada e la regione in cui la pecora sta attualmente confinano
        //e se la regione d'arrivo confina con la strada

    }

}
