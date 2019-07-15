/**
 * Comp90015-Assignment 2
 * @author: Kung Pao Chicken
 * Ming Tang 902089
 * Yajing Huang 896243
 * Wai Yan Wong 892083
 *
 */

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.Date;


public class ClientThread implements Runnable {
    private Socket socket;
    private int ClientID;
    private String Name;
    private String ClientInfor;
    private DataInputStream input;
    private DataOutputStream output;


    public ClientThread (Socket socket, int ClientID) {
        this.socket = socket;
        this.ClientID = ClientID;
    }

    public String getClientInfor() {
        return Name;
    }

    @Override
    public void run(){
        try {
            // The JSON Parser
            JSONParser parser = new JSONParser();
            // Input stream
            DataInputStream input = new DataInputStream(socket.getInputStream());
            this.input = input;
            // Output Stream
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            this.output = output;

            JSONObject UserName = (JSONObject) parser.parse(input.readUTF());
            if(UserName.containsKey("username")) {
                String userName = (String) UserName.get("username");
                /*
                if(!ClientManage.getInstance().isValid(userName)) {
                    JSONObject msg = new JSONObject();
                    msg.put("ReplyLogin", "Invalid User");
                    this.write(msg);
                    socket.close();
                    System.out.println("Invalid user" + ClientID);
                }
                else
                 */
                if(ClientManage.getInstance().isLoged(userName)) {
                    JSONObject msg = new JSONObject();
                    msg.put("ReplyLogin", "Already Logged in");
                    this.write(msg);
                    socket.close();
                    System.out.println("Duplicated log in denied:" + ClientID);
                }
                else{
                    JSONObject msg = new JSONObject();
                    Name = userName;
                    String NewC = userName + ":" + Integer.toString(ClientID) + ":" + new String(socket.getInetAddress().getAddress());
                    ClientInfor = NewC;
                    String ServerInfor = "Connected on server: " + socket.getLocalAddress() + " on port: " + socket.getLocalPort();
                    msg.put("ReplyLogin", "Connection Infor:" + ServerInfor);
                    msg.put("ClientInfor", ClientInfor);
                    this.write(msg);

                    System.out.println("Client: " + userName + " ID: " + this.ClientID + " connected");
                    ClientManage.getInstance().clientConnected(this);
                    System.out.println(ClientManage.getInstance().Online() + " clients online");

                    while (true) {
                        if (input.available() > 0) {
                            // Attempt to convert read data to JSON
                            JSONObject command = (JSONObject) parser.parse(input.readUTF());
                            String Command = (String) command.get("Command");
                            System.out.println("COMMAND RECEIVED: " + Command);
                            //Integer result = parseCommand(command);

                            if(Command.contains("invite")) {
                                if(!ClientManage.getInstance().getGameRunning()) {
                                    //ClientManage.getInstance().setGameRunning(true);
                                    String[] invite = Command.split(":");
                                    //send invitation
                                    String[] inviteList = new String[invite.length -1];
                                    for(int i = 1; i < invite.length; i++) {
                                        inviteList[i - 1] = invite[i];
                                    }
                                    ClientManage.getInstance().sendInvitation(inviteList, this);
                                    ClientManage.getInstance().setGamerReady(this.Name);
                                }
                                else {
                                    System.out.println("Can't begin new game.");
                                    msg = new JSONObject();
                                    msg.put("Error","Error: can't create game, game running currently");
                                    this.write(msg);
                                }

                            }

                            else if(Command.contains("accept")) {
                                ClientManage.getInstance().setGamerReady(this.Name);
                            }

                            else if(Command.contains("decline")) {
                                ClientManage.getInstance().setGamerNotReady();

                            }

                            else if(Command.equals("Join")) {
                                msg = new JSONObject();
                                msg.put("ReplyGameInfor", "rmi://localhost:3333/NewScrabbleGame");
                                this.write(msg);
                            }


                            else if(Command.equals("Disconnect")) {
                                ClientManage.getInstance().clientDisconnected(this);
                                System.out.println("client" + this.ClientID + " log off.");
                            }
                        }
                    }
                }
            }
            else {
                socket.close();
                System.out.println("Unlogged user disconnected");
            }


        }
        catch (IOException e) {
            System.out.println("Client : "+ ClientID + " has disconnected at " + new Date() + ".");
            ClientManage.getInstance().clientDisconnected(this);
            System.out.println("client" + this.ClientID + "log off.");
            System.out.println(ClientManage.getInstance().Online() + " clients online");
            ClientManage.getInstance().clientDisconnected(this);
        }
        catch (ParseException e) {
            System.out.println("Corrupted message received from Client" + ClientID);
        }
    }

    public void write(JSONObject Reply) {

        try {
            output.writeUTF(Reply.toJSONString());
            System.out.println(Thread.currentThread().getName() + " - Message sent to client " + ClientID);
        }
        catch (IOException e) {
            System.out.println("Client : "+ ClientID + " has disconnected at " + new Date() + ".");
            System.out.println(ClientManage.getInstance().Online() + " clients online");
            ClientManage.getInstance().clientDisconnected(this);
        }
    }

}
