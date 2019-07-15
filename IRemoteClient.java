/**
 * Comp90015-Assignment 2
 * @author: Kung Pao Chicken
 * Ming Tang 902089
 * Yajing Huang 896243
 * Wai Yan Wong 892083
 *
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteClient extends Remote {
    public String getGamerID() throws RemoteException;
    public void sendMessage(String mag) throws RemoteException;
    public void addScore(int newWordScore) throws RemoteException;
    public int getScore() throws  RemoteException;
}


