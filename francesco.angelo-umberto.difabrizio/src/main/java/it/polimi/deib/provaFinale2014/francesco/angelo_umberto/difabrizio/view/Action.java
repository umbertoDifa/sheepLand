package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

/**
 *
 * @author Francesco
 */
public class Action extends BackgroundAndTextJPanel {

    public Action() {
        setOpacity(true);
    }
    
    /**
     * se ok l'img viene visualizzata non opaca
     * @param ok 
     */
    protected void setAvailableView(boolean ok) {
        super.setOpacity(false);
    }

}
