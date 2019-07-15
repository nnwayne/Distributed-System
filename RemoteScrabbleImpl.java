/**
 * Comp90015-Assignment 2
 * @author: Kung Pao Chicken
 * Ming Tang 902089
 * Yajing Huang 896243
 * Wai Yan Wong 892083
 *
 */

import javax.swing.*;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class RemoteScrabbleImpl extends UnicastRemoteObject implements IRemoteScrabble, Serializable {
    private List<IRemoteClient> gamers;

    private char[][] scrabble;
    private IRemoteClient currentGamer;
    private int inRoundCount;
    private int RowAgree;
    private int ColumnAgree;
    private int RowDisagree;
    private int ColumnDisagree;
    private int newWordScoreRow;
    private int newWordScoreColumn;
    private boolean newCharAdded;
    private int[] position;
    private int gamerReady;
    private boolean firstTime;
    private boolean gameRunning;
    private  int port;
    // private RemoteClient RC = new RemoteClient();

    public RemoteScrabbleImpl() throws Exception {
        super();
        gamerReady = 0;
        position = new int[2];
        this.gamers = new ArrayList<IRemoteClient>();
        this.scrabble = new char[20][20];
        for(int i = 0; i < 19; i++) {
            for(int j = 0; j < 19; j++) {
                scrabble[i][j] = '.';
            }
        }
        gameRunning = false;
        inRoundCount = 0;
        RowAgree = 0;
        RowDisagree = 0;
        ColumnDisagree = 0;
        firstTime = true;
    }


    public synchronized void StartGame(int port ) {
        try {
            //System.setSecurityManager(new java.rmi.RMISecurityManager());
            LocateRegistry.createRegistry(port);
            this.port = port;
            System.out.println(this.port);
            Naming.bind("rmi://localhost:" + this.port + "/NewScrabbleGame", this);
            System.out.println("New game Started...");
        }
        catch (Exception e) {
            System.out.println("Wrong on starting game...");
        }
        firstTime = true;
    }


    /***
     * other gamer ready
     * @throws RemoteException
     */
    public synchronized void ready() throws RemoteException{
        gamerReady++;
        BroadCasting("Warning:New player ready");
    }

    /***
     * unready
     * @throws RemoteException
     */
    public synchronized void notReady() throws RemoteException{
        gamerReady--;
        BroadCasting("Warning:New player not ready");
    }

    public synchronized void start() throws RemoteException{
        gamerReady ++;
        if(gamerReady < gamers.size() || gamers.size() < 2 ) {
            BroadCasting("Warning:New player ready, Waiting for all players ready");
            gamerReady --;
        }
        else {
            this.currentGamer = gamers.get(0);
            sendMessage(currentGamer, "Pass:");
            BroadCasting("GameStart:");
            gameRunning = true;
        }
    }

    public synchronized void setCharacter(int i, int j, char c) throws RemoteException {
        if (gamerReady < gamers.size() || !gameRunning) {
            BroadCasting("Warning: waiting for all players get ready.");
        }
        else {
                this.scrabble[i][j] = c;
                System.out.println("New character:" + c + " at " + i + " , " + j);
                BroadCasting("New character:" + c + ":" + i + ":" + j);
                newCharAdded = true;
                inRoundCount = 0;
                position[0] = i;
                position[1] = j;


        }
        //System.out.println("BroFinish");
    }


    @Override
    public synchronized void newWord(int i, int j, boolean row) throws RemoteException {
        if (!gameRunning) {
            BroadCasting("Warning:game not started yet.");
            return;
        }
        int wordScore = 0;
        String newWord = "";
        if (row) {
            countloop1:
            for (int k = j; k < 20; k ++) {
                if(scrabble[i][k] != '.') {
                    System.out.println(scrabble[i][k]);
                    newWord = newWord + String.valueOf(scrabble[i][k]);
                    wordScore++;
                }
                else {
                    break countloop1;
                }
            }
            countloop2:
            for (int k = j - 1; k >= 0; k --) {
                if(scrabble[i][k] != '.') {
                    System.out.println(scrabble[i][j]);
                    newWord = String.valueOf(scrabble[i][k]) + newWord;
                    wordScore++;
                }
                else {
                    break countloop2;
                }
            }
        }
        else {
            wordScore =0;
            countloop1:
            for (int k = i; k < 20; k ++) {
                if(scrabble[k][j] != '.') {
                    System.out.println(scrabble[k][j]);
                    newWord = newWord + String.valueOf(scrabble[k][j]);
                    wordScore++;
                }
                else {
                    break countloop1;
                }
            }
            countloop2:
            for (int k = i - 1; k >= 0; k --) {
                if(scrabble[k][j] != '.') {
                    System.out.println(scrabble[k][j]);
                    newWord = String.valueOf(scrabble[k][j]) + newWord;
                    wordScore++;
                }
                else {
                    break countloop2;
                }
            }
        }


        if(row) {
            newWordScoreRow = wordScore;
            System.out.println("New word at row:" + newWord + " at row " + i);
            BroadCasting("New word at row:" + newWord + " at row " + i);
        }
        else {
            newWordScoreColumn = wordScore;
            System.out.println("New word at column:" + newWord + " at column " + j);
            BroadCasting("New word at column:" + newWord + " at column " + j);
        }
    }


    public synchronized void setAgree(Boolean Row) throws RemoteException {
        if(Row) {
            this.RowAgree++;
            System.out.println("one player agreed at R");
            if (RowAgree + RowDisagree == gamers.size()) {
                System.out.println("all players replied");
                if (RowAgree == gamers.size()) {
                    currentGamer.addScore(newWordScoreRow);
                    String ScoreBoard = "";

                    System.out.println("Scored:" + currentGamer.getGamerID() + " gets " + newWordScoreRow + " current score" + currentGamer.getScore());
                    for (int z = 0; z < gamers.size(); z++) {
                        System.out.println(gamers.get(z).getGamerID() + "-" + gamers.get(z).getScore());
                        ScoreBoard = ScoreBoard + ":" + gamers.get(z).getGamerID() + "-" + gamers.get(z).getScore();
                    }
                    System.out.println("New scoreboard sending");
                    BroadCasting("ScoreBoard" + ScoreBoard);
                }
                else {
                    currentGamer.addScore(0);
                    String ScoreBoard = "";

                    System.out.println("Scored:" + currentGamer.getGamerID() + " gets " + newWordScoreRow + " current score" + currentGamer.getScore());
                    for (int z = 0; z < gamers.size(); z++) {
                        System.out.println(gamers.get(z).getGamerID() + "-" + gamers.get(z).getScore());
                        ScoreBoard = ScoreBoard + ":" + gamers.get(z).getGamerID() + "-" + gamers.get(z).getScore();
                    }
                    System.out.println("New scoreboard sending");
                    BroadCasting("ScoreBoard" + ScoreBoard);

                }
                inRoundCount = 0;
                RowDisagree = 0;
                RowAgree = 0;
                //pass(true);

            }
        }
        else{
            this.ColumnAgree++;
            System.out.println("one player agreed at C");
            if (ColumnAgree + ColumnDisagree == gamers.size()) {
                if (ColumnAgree == gamers.size()) {
                    currentGamer.addScore(newWordScoreRow);
                    String ScoreBoard = "";

                    System.out.println("Scored:" + currentGamer.getGamerID() + " gets " + newWordScoreRow + " current score" + currentGamer.getScore());
                    for (int z = 0; z < gamers.size(); z++) {
                        System.out.println(gamers.get(z).getGamerID() + "-" + gamers.get(z).getScore());
                        ScoreBoard = ScoreBoard + ":" + gamers.get(z).getGamerID() + "-" + gamers.get(z).getScore();
                    }
                    System.out.println("New scoreboard sending");
                    BroadCasting("ScoreBoard" + ScoreBoard);
                    inRoundCount = 0;
                    ColumnAgree = 0;
                    ColumnDisagree = 0;
                    pass(true);
                    System.out.println("Token passed.");
                }
                else {
                    currentGamer.addScore(0);
                    String ScoreBoard = "";

                    System.out.println("Scored:" + currentGamer.getGamerID() + " gets " + newWordScoreRow + " current score" + currentGamer.getScore());
                    for (int z = 0; z < gamers.size(); z++) {
                        System.out.println(gamers.get(z).getGamerID() + "-" + gamers.get(z).getScore());
                        ScoreBoard = ScoreBoard + ":" + gamers.get(z).getGamerID() + "-" + gamers.get(z).getScore();
                    }
                    System.out.println("New scoreboard sending");
                    BroadCasting("ScoreBoard" + ScoreBoard);
                    inRoundCount = 0;
                    ColumnDisagree = 0;
                    ColumnAgree = 0;
                    pass(true);
                    System.out.println("Token passed.");
                }
            }
        }
    }


    public synchronized void setDisagree(Boolean Row) throws RemoteException {
        if(Row) {
            this.RowDisagree++;
            if(RowDisagree + RowAgree == gamers.size()) {
                currentGamer.addScore(0);
                String ScoreBoard = "";

                System.out.println("Scored:" + currentGamer.getGamerID() + " gets " + newWordScoreRow + " current score" + currentGamer.getScore());
                for (int z = 0; z < gamers.size(); z++) {
                    System.out.println(gamers.get(z).getGamerID() + "-" + gamers.get(z).getScore());
                    ScoreBoard = ScoreBoard + ":" + gamers.get(z).getGamerID() + "-" + gamers.get(z).getScore();
                }
                System.out.println("New scoreboard sending");
                BroadCasting("ScoreBoard" + ScoreBoard);
                inRoundCount = 0;
                ColumnAgree = 0;
                ColumnDisagree = 0;
            }
        }
        else {
            this.ColumnDisagree++;
            if(ColumnAgree + ColumnDisagree == gamers.size()) {
                currentGamer.addScore(0);
                String ScoreBoard = "";

                System.out.println("Scored:" + currentGamer.getGamerID() + " gets " + newWordScoreRow + " current score" + currentGamer.getScore());
                for (int z = 0; z < gamers.size(); z++) {
                    System.out.println(gamers.get(z).getGamerID() + "-" + gamers.get(z).getScore());
                    ScoreBoard = ScoreBoard + ":" + gamers.get(z).getGamerID() + "-" + gamers.get(z).getScore();
                }
                System.out.println("New scoreboard sending");
                BroadCasting("ScoreBoard" + ScoreBoard);
                inRoundCount = 0;
                ColumnAgree = 0;
                ColumnDisagree = 0;
                pass(true);
                System.out.println("Token passed.");
            }
        }

    }


    public synchronized void pass(boolean isNew) throws RemoteException {
        int token = gamers.indexOf(currentGamer);
        if(!isNew && !newCharAdded) {
            inRoundCount ++;
            System.out.println("RoundCount: "+inRoundCount);
        }


        if (inRoundCount < gamers.size()) {
            if(token < gamers.size() - 1) {
                token ++;
                currentGamer = (IRemoteClient) gamers.get(token);
                sendMessage(currentGamer, "Pass:");
                BroadCasting("Token passed to:" + currentGamer.getGamerID());
                newCharAdded = false;
                System.out.println("token at: "+token);
            }
            else {
                token = 0;
                currentGamer = (IRemoteClient) gamers.get(token);
                sendMessage(currentGamer, "Pass:");
                BroadCasting("Token passed to:" + currentGamer.getGamerID());
                newCharAdded = false;
                System.out.println("token at: 0");
            }
        }

        else if (inRoundCount == gamers.size()) {
            this.getWinner();
            this.exit();
            //gamers.clear();
        }
    }


    public synchronized void getWinner() throws RemoteException {
        int max = 0;
        ArrayList<Integer> winners = new ArrayList<>();
        for(int i = 0; i < gamers.size(); i++) {
            if(max < ((IRemoteClient)gamers.get(i)).getScore()) {
                max = ((IRemoteClient)gamers.get(i)).getScore();
                if(winners.size() == 0) {
                    System.out.println(i);
                    winners.add(i);
                }
                else {
                    winners.clear();
                    winners.add(i);
                }
            }
            else if(max == ((IRemoteClient)gamers.get(i)).getScore()) {

                winners.add(i);
            }
        }
        String news = "";
        for(int i = 0; i < winners.size(); i++) {
            news = news + ((IRemoteClient)gamers.get(i)).getGamerID() + ", ";
        }
        System.out.println("Game over:" + news + " wins.");
        BroadCasting("Game over: " + news + " wins.");
    }


    @Override
    public synchronized void BroadCasting(String msg) throws RemoteException {
        for (int i = 0; i < gamers.size(); i++) {
            ((IRemoteClient)gamers.get(i)).sendMessage(msg);
            //RC.sendMessage(msg);
        }
    }


    @Override
    public synchronized void sendMessage(IRemoteClient gamer, String msg) throws RemoteException {
        if(!gamers.contains(gamer)) {
            return;
        }
        else {
            gamer.sendMessage(msg);
        }
    }

    @Override
    public synchronized void addClient(IRemoteClient gamer) throws RemoteException {
        if(!gamers.contains(gamer)) {
            if(gamers.size() < 4) {
                gamers.add(gamer);
                System.out.println("New gamer enter this room: " + gamer.getGamerID());
                System.out.println("Online gamer:" + gamers.size());
                BroadCasting("New gamer:" + gamer.getGamerID());
            }
            else {
                sendMessage(gamer,"Reject:");
            }
        }
    }


    @Override
    public synchronized void removeClient(IRemoteClient gamer, boolean isHost) throws RemoteException {
        //System.out.println("gamer log off");
        if (gameRunning) {
            if (gamers.contains(gamer)) {
                System.out.println("gamer log off");
                gamers.remove(gamer);
                BroadCasting("Offline Gamer:" + gamer.getGamerID());

                System.out.println("Gamer left this room: " + gamer.getGamerID());
                System.out.println("Online gamer:" + gamers.size());
                BroadCasting("Warning:Unexpected log off, game over");
                System.out.println("Warning:Unexpected log off, game over");
                this.exit();
                //this.gamers.clear();
            }
            else {
                System.out.println("no such player");
            }
        }
        else {

            if (!isHost) {
                if (gamers.contains(gamer)) {
                    System.out.println("gamer log off");
                    BroadCasting("Gamer leaving:" + gamer.getGamerID());
                    gamers.remove(gamer);
                    System.out.println("Gamer left this room: " + gamer.getGamerID());
                    System.out.println("Online gamer:" + gamers.size());
                    if (gamers.size() < 2 && gamers.size() > 0) {
                        BroadCasting("Offline Gamer:game over");
                        System.out.println("Warning:Unexpected log off, game over");
                        this.exit();
                    }
                    else if (gamers.size() == 0) {
                        System.out.println("Warning:game over");
                        this.exit();
                    }
                    else {
                        System.out.println("no such player");
                    }
                }
            }

            else {
                BroadCasting("Offline Gamer:game over");
                System.out.println("Warning:Unexpected log off, game over");
                this.exit();
            }
        }
    }



    public void exit() throws RemoteException {
        try {
            Naming.unbind("rmi://localhost:" + port + "/NewScrabbleGame");
            UnicastRemoteObject.unexportObject(this, true);
            System.out.print("Game ended.");
            ClientManage.getInstance().setGameRunning(false);
        }
        catch (Exception e) {
            System.out.print("Game ending failed.");
        }
    }

    public static void main(String[] args) throws Exception {
        IRemoteScrabble newGame = new RemoteScrabbleImpl();
        int port = 3333;
        ((RemoteScrabbleImpl) newGame).StartGame(port);
        //((RemoteScrabbleImpl) newGame).start();
    }

}