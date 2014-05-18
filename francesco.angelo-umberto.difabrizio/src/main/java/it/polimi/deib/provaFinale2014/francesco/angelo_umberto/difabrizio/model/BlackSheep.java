package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;


public class BlackSheep extends SpecialAnimal {
    @Override
    public void moveTo(Region region){
        //se non c'è un recinto e se esiste la strada uscita col dado
        //muoviti
        super.setMyRegion(region);
    }
}
