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

public interface IRemoteScrabble extends Remote {
    public void sendMessage(IRemoteClient client, String msg) throws RemoteException;
    public void addClient(IRemoteClient client) throws RemoteException;
    public void removeClient(IRemoteClient client, boolean isHost) throws RemoteException;
    public void BroadCasting(String msg) throws RemoteException;
    public void setCharacter( int i, int j, char c) throws RemoteException;
    public void newWord(int i, int j, boolean row) throws RemoteException;
    public void setAgree(Boolean Row) throws RemoteException;
    public void setDisagree(Boolean Row) throws RemoteException;
    public void start() throws RemoteException;
    public void pass(boolean isNew) throws RemoteException;
    public void ready() throws RemoteException;
    public void notReady() throws RemoteException;
    public void getWinner() throws RemoteException;
}
