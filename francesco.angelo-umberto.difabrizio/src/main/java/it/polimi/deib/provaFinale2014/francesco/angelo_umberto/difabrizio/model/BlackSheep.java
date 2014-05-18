package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

public class BlackSheep extends SpecialAnimal {

    @Override
    public void setAt(Region region) {
        super.setMyRegion(region);
    }

    @Override
    public void moveThroughTo(Street street, Region endRegion) {
        //se non c'è un recinto e se esiste la strada uscita col dado
        //e se la strada e la regione in cui la pecora sta attualmente confinano
        //e se la regione d'arrivo confina con la strada
        if (street != null && 
                !street.hasFence() && 
                this.getMyRegion().isNeighbour(street) &&
                street.isNeighbour(endRegion)) {
            //trova regione d'arrivo
            this.setAt(endRegion);
        }
    }

}
