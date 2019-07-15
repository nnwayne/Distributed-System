/**
 * Comp90015-Assignment 2
 * @author: Kung Pao Chicken
 * Ming Tang 902089
 * Yajing Huang 896243
 * Wai Yan Wong 892083
 *
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import org.json.simple.JSONObject;

public class Client {

	private static Socket socket;
	private static DataInputStream reader;
    private static DataOutputStream writer;

	public static void main(String args[]) {
		Port.createGUI();
	}
	
	public static void ConnectServer(String IP, String port) throws Exception{
		
			int PortNum = Integer.parseInt(port);
			socket = new Socket(IP, PortNum);
			System.out.println("Connection with server established.");

			// Get the input/output streams for reading/writing data from/to the socket
			reader = new DataInputStream(socket.getInputStream());
            writer = new DataOutputStream(socket.getOutputStream());

			// Launch a new thread in charge of listening for any messages
			// that arrive through the socket's input stream (any data sent by the server)
			ServerListener ml = new ServerListener(reader);
			ml.start();
	}


	
	public static void sendMessage(JSONObject message) {
		try {
			// Send a message to server and print the message
			writer.writeUTF(message.toJSONString());
			System.out.print("Send to server: ");
			System.out.println(message.toJSONString());
		} catch (Exception e) {
			System.out.println("Connection is interrupted.");
			System.out.println("System exit.");
			System.exit(0);
		}
	}

	public static void closeSocket() {
		try {
			JSONObject jsonDisconnect = new JSONObject();
			jsonDisconnect.put("Command", "Disconnect");
			sendMessage(jsonDisconnect);
			socket.close();
		} catch (Exception e) {
			System.out.println("closeSocket Wrong");

		}
	}
}
