/**
 * Comp90015-Assignment 2
 * @author: Kung Pao Chicken
 * Ming Tang 902089
 * Yajing Huang 896243
 * Wai Yan Wong 892083
 *
 */

import java.io.IOException;
import java.net.*;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Date;
import java.util.ArrayList;
import java.util.Scanner;


public class authorizationServer {
    private int port;
    private Scanner managerInput;
    private ServerSocket serverSocket;
    private String clientList;


    public authorizationServer(int port, Scanner managerInput, String clientList) {
        this.port = port;
        this.managerInput = managerInput;
        this.clientList = clientList;
    }


    public void startServer() {
        int port = this.port;
        boolean done = false;
        ClientManage.getInstance().setValidClient(clientList);

        while (!done) {
            try {
                this.serverSocket = new ServerSocket(port);
                //clientOnline = new ArrayList<String[]>();
                System.out.println("Server is running on port: " + port);
                int count = 0;
                while (true) {
                    Socket ThreadSocket = serverSocket.accept();
                    count++;

                    //ThreadSocket.setSoTimeout(12000000);
                    ClientThread client = new ClientThread(ThreadSocket, count);
                    new Thread(client).start();
                    done = true;
                }

            }
            catch (IOException e) {
                System.out.println("Cannot start server: please check port or network connection");
                System.out.println("Please input new port: ");
                String inputPort = managerInput.nextLine();
                boolean finishp = false;
                while (!finishp) {
                    try {
                        port = Integer.parseInt(inputPort);
                        finishp = true;
                    } catch (Exception invalidPort) {
                        System.out.println("Invalid, please input again:");
                        inputPort = managerInput.nextLine();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        int Port = 1268;
        String fileLocation = "validUser.txt";
        boolean done = false;

        Scanner userInput = new Scanner(System.in);

        //follow section is the valid check
        while (!done) {
            System.out.println("Please input the port number:");
            boolean finishp = false;
            String inport = userInput.nextLine();
            while (!finishp) {
                try {
                    Port = Integer.parseInt(inport);
                    finishp = true;
                } catch (Exception e) {
                    System.out.println("Please input valid number:");
                    inport = userInput.nextLine();
                }
            }
            done = true;
        }


        authorizationServer runningServer = new authorizationServer(Port, userInput, fileLocation);

        runningServer.startServer();
    }
}
