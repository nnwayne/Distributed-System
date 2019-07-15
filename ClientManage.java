/**
 * Comp90015-Assignment 2
 * @author: Kung Pao Chicken
 * Ming Tang 902089
 * Yajing Huang 896243
 * Wai Yan Wong 892083
 *
 */


import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ClientManage {
    private static ClientManage instance;
    private List<ClientThread> connectedClient;
    private List<String> ClientOnline;
    private List<String> ValidClient;
    private IRemoteScrabble game;
    private HashMap<String, ClientThread> connectedDic;
    private boolean gameRunning;
    private int gamerReady;
    private int gamerNotReady;
    private String[] inviteList;
    private ClientThread gameHost;
    private ArrayList<String> ReadyList;
    private int port;

    private ClientManage() {
        connectedClient = new ArrayList<ClientThread> ();
        ClientOnline = new ArrayList<String>();
        ValidClient = new ArrayList<String>();
        connectedDic = new HashMap<String, ClientThread>();
        gameRunning = false;
        gamerReady = 0;
        ReadyList = new ArrayList<String>();
        port = 3333;
    }

    public boolean getGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean run) {
        this.gameRunning = run;
        if(!run) {
            this.SendOnlineClient();
            gamerReady = 0;
        }
    }


    public int Online() {
        return ClientOnline.size();
    }

    public void setGame(IRemoteScrabble game) {
        this.game = game;
    }

    public void setGamerReady(String Name) {
        this.gamerReady++;
        ReadyList.add(Name);
        this.gameStart();
    }


    public void setGamerNotReady() {
        this.gamerNotReady++;
        this.gameStart();
    }


    public void gameStart() {
        if(gamerNotReady + gamerReady == inviteList.length + 1) {
            System.out.println("all players ready");
            if(gamerReady < 2) {
                System.out.println("No player");
                JSONObject msg = new JSONObject();
                msg = new JSONObject();
                msg.put("Not Enough Player", "No player");
                gameHost.write(msg);
                this.gamerReady = 0;
                this.gamerNotReady = 0;
                this.ReadyList = new ArrayList<>();
            }
            else {
                JSONObject msg = new JSONObject();
                msg = new JSONObject();
                //msg.put("GameStart", "Ready");
                //gameHost.write(msg);
                if(!ClientManage.getInstance().getGameRunning()) {
                    try {
                        IRemoteScrabble newGame = new RemoteScrabbleImpl();
                        ((RemoteScrabbleImpl) newGame).StartGame(port);
                        ClientManage.getInstance().setGame(newGame);
                        msg = new JSONObject();
                        msg.put("ReplyStartGame", port);
                        this.setGameRunning(true);
                        //ClientManage.getInstance().processMessage("Invitation");
                        port++;

                    }
                    catch (Exception e) {
                        System.out.println("Can't begin new game.");
                        msg = new JSONObject();
                        msg.put("Error","Error: can't create game");
                    }
                }
                else {
                    this.gamerReady = 0;
                    this.gamerNotReady = 0;

                    System.out.println("Can't begin new game.");
                    msg = new JSONObject();
                    this.setGameRunning(false);
                    msg.put("Error","Error: can't create game, game running currently");
                }
                for (int i = 0; i < ReadyList.size(); i++) {
                    if(connectedDic.containsKey(ReadyList.get(i))) {
                        connectedDic.get(ReadyList.get(i)).write(msg);
                    }
                }
                this.ReadyList = new ArrayList<>();
            }
        }
    }


    public void setValidClient(String file) {
        ValidClient = new ArrayList<>();
        File fileObject = new File(file);
        if(fileObject.exists()) {//tell whether the file exists
            Scanner inputStream = null;
            try {
                inputStream = new Scanner(fileObject);
                String line;
                while(inputStream.hasNextLine()) {
                    line = inputStream.nextLine();

                    ValidClient.add(line);
                }
                inputStream.close();
            }
            catch (FileNotFoundException e) {
                System.out.println("Error opening the ClientList file: " + file);
            }
        }
    }




    public boolean isLoged(String ID) {
        if(ClientOnline.contains(ID)) {
            return true;
        }
        else return false;
    }

    public boolean isValid(String ID) {
        if(ValidClient.contains(ID)) {
            return true;
        }
        else return false;
    }


    public static synchronized ClientManage getInstance() {
        if(instance == null) {
            instance = new ClientManage();
        }
        return instance;
    }


    public synchronized void processMessage(String value) {
        // Broadcast the client message to all the clients connected
        // to the server
        JSONObject msg = new JSONObject();
        if(value.equals("Invitation")) {
            msg.put("ReplyStartGame", value);
        }
        else {
            msg.put("Reply", value);
        }
        for(ClientThread clientConnection : connectedClient) {
            clientConnection.write(msg);
        }
    }

    public synchronized void sendInvitation(String[] players, ClientThread client) {
        this.gameHost = client;
        JSONObject msg = new JSONObject();
        msg.put("ReplyStartGame", "Invitation");
        inviteList = players;
        for (int i = 0; i < players.length; i++) {
            if(connectedDic.containsKey(players[i])) {
                connectedDic.get(players[i]).write(msg);
                System.out.print(players[i]);
            }
        }
    }



    public synchronized void SendOnlineClient() {
        JSONObject msg = new JSONObject();
        String list = "";
        for(int i = 0; i < ClientOnline.size(); i++ ) {
            list = list + ":" + ClientOnline.get(i);
        }
        msg.put("ReplyClientList", list);
        for(ClientThread clientConnection : connectedClient) {
            clientConnection.write(msg);
        }
    }


    public synchronized void clientConnected(ClientThread clientConnection) {
        connectedClient.add(clientConnection);
        ClientOnline.add(clientConnection.getClientInfor());
        connectedDic.put(clientConnection.getClientInfor(), clientConnection);
        this.SendOnlineClient();
    }


    public synchronized void clientDisconnected(ClientThread clientConnection) {
        connectedClient.remove(clientConnection);
        ClientOnline.remove(clientConnection.getClientInfor());
        this.SendOnlineClient();
    }

    public synchronized List<ClientThread> getConnectedClients() {
        return connectedClient;
    }

}
