package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionCancelledException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.OvineType;
import java.rmi.RemoteException;

/**
 *
 * @author Umberto
 */
public interface Player {

    public void moveOvine(OvineType type) throws RemoteException,
                                                 ActionCancelledException;

    public void moveShepherd() throws RemoteException, ActionCancelledException,
                                      FinishedFencesException;

    public void buyLand() throws RemoteException, ActionCancelledException;

    public void mateSheepWith(OvineType otherOvineType) throws RemoteException,
                                                               ActionCancelledException;

    public void killOvine() throws RemoteException;
}
