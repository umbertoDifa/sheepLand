package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;


public class KillSheep extends Action {

    private KillSheep() {
    }
    
    public static KillSheep getInstance(){
        return new KillSheep();
    }
    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
