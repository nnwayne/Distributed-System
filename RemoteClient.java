/**
 * Comp90015-Assignment 2
 * @author: Kung Pao Chicken
 * Ming Tang 902089
 * Yajing Huang 896243
 * Wai Yan Wong 892083
 *
 */
import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteClient extends UnicastRemoteObject implements IRemoteClient,ActionListener{
	private String ID = "1";
	private IRemoteScrabble game;
	private boolean token;
	private boolean connected;
	private int score = 0;
	private char letter;
	private int mouseCount;
	private String regex;
	private JFrame frame;
	private JButton bt[][] = new JButton[20][20];
	private JLabel lb[][] = new JLabel[1][20];
	private int i, j;
	private JButton btnPass;
	private JButton btnSubmit;
	private JButton btnExitGame;
	private JButton btnA;
	private JButton btnB;
	private JButton btnC;
	private JButton btnD;
	private JButton btnE;
	private JButton btnF;
	private JButton btnG;
	private JButton btnH;
	private JButton btnI;
	private JButton btnJ;
	private JButton btnK;
	private JButton btnL;
	private JButton btnP;
	private JButton btnO;
	private JButton btnN;
	private JButton btnM;
	private JButton btnQ;
	private JButton btnR;
	private JButton btnS;
	private JButton btnT;
	private JButton btnU;
	private JButton btnV;
	private JButton btnW;
	private JButton btnX;
	private JButton btnY;
	private JButton btnZ;
	private JLabel label;
	private JLabel lblNewLabel;
	private JTextArea showScore;
	private JButton Agree;
	private JButton btnDisagree;
	private JButton btnStartReady;
	private JTextArea Score;
	private boolean holder = false;
	private boolean ready = false;
	private boolean row = true;
	private static boolean isHost = false;
	private boolean isStart = false;
	private Scanner scanner;
	private int k;
	private int l;
	private boolean tempPass;
	private static int port;



	/**
	 * Launch the application.
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		if(args.length == 0) {
			try {
                System.out.println("please input a number:");
                Scanner scanner = new Scanner(System.in);
                int host =  scanner.nextInt();
                System.out.print(host);
                if(host == 1){
                    isHost = true;
                }else{
                    isHost = false;
                }
                port = Integer.parseInt(args[1]);
				RemoteClient window = new RemoteClient();
				window.frame.setVisible(true);
				window.connect( "1", "3333");

			} catch (Exception e) {
				System.out.println("main method wrong in RemoteClient");
			}
		}
		else {
			try {
                if (args[1].equals("1")){
                    isHost = true;
                }else {
                    isHost = false;
                }

				RemoteClient window = new RemoteClient();
				window.frame.setVisible(true);
				window.connect(args[0], args[2]);




			} catch (Exception e) {
				System.out.println("main method wrong in RemoteClient");

			}
		}


	}


	private void connect(String ID, String port) {
		try {
			this.ID = ID;
			System.out.println(port);

			game = (IRemoteScrabble) Naming.lookup("rmi://localhost:" + port + "/NewScrabbleGame");
			connected = true;
//
			game.addClient(this);
//			game.start();
		} catch (Exception e) {
			System.out.println("error connection.\n");
			connected = false;
		}
	}


	private void disconnect() {

		try {

			if (game == null) {
				return;
			}
			game.removeClient(this,isHost);
			System.out.println("diconnected");
			//game = null;

		} catch (Exception e) {
			token = false;
			showScore.append("Please exit the game"+"\n");
			showAll(false);
			btnStartReady.setVisible(false);
			btnExitGame.setVisible(true);
			btnDisagree.setVisible(false);
			Agree.setVisible(false);
//			btnExitGame.setVisible(true);
			System.out.println("error disconnection.");
		} finally {
			connected = false;

		}
	}

	@Override
	public String getGamerID() throws RemoteException {
		return this.ID;
	}

	@Override
	public synchronized int getScore() throws RemoteException {
		return score;
	}

	@Override
	public synchronized void addScore(int newWordScore) throws RemoteException {
		if (newWordScore == 0) {
			showScore.append("sorry, you score failed~"+"\n");
//			JOptionPane.showMessageDialog(null, "sorry, you score failed~", null, JOptionPane.PLAIN_MESSAGE);
		} else {
			showScore.append("congratulations! you score" + newWordScore+"\n");
//			JOptionPane.showMessageDialog(null, "congratulations! you score" + newWordScore, null,
//					JOptionPane.PLAIN_MESSAGE);
		}
		 score = score + newWordScore;
		System.out.println(score);
	}

	@Override
	public void sendMessage(String msg) throws RemoteException {
		String[] info = msg.split(":");
		if (info != null) {
			if (info[0].equals("New character")) {
				k = Integer.parseInt(info[2]);
				l = Integer.parseInt(info[3]);
				bt[k][l].setText(info[1]);
				bt[k][l].setForeground(Color.BLACK);
				System.out.println(msg);
			} else if (info[0].equals("New gamer")) {

				JOptionPane.showMessageDialog(null, this.getGamerID() + " is online", null, JOptionPane.PLAIN_MESSAGE);
			} else if (info[0].equals("New word at column")) {
				Agree.setVisible(true);
				btnDisagree.setVisible(true);
				row = false;
				showScore.append(info[1]+" Please choose agree or disagree button to vote"+"\n");
			} else if (info[0].equals("New word at row")) {
				Agree.setVisible(true);
				btnDisagree.setVisible(true);
				row = true;
				showScore.append(info[1]+" Please choose agree or disagree button to vote"+"\n");
			}else if (info[0].equals("Pass")) {
			    tempPass = true;
				token = true;
                showScore.append("It's your turn"+"\n");
			}  else if (info[0].equals("Token passed to")) {
//				tempPass = true;
				showScore.append(msg+"\n");
			} else if (info[0].equals("Game over")) {
				token = false;
				showScore.append(msg+"Please exit the game"+"\n");
				showAll(false);
				btnStartReady.setVisible(false);
				btnExitGame.setVisible(true);
				btnDisagree.setVisible(false);
				Agree.setVisible(false);


			} else if (info[0].equals("New gamer")) {
				showScore.append(msg+"\n");
//				JOptionPane.showMessageDialog(null,msg, null, JOptionPane.PLAIN_MESSAGE);
			} else if (info[0].equals("Warning")){
				showScore.append(info[1]+"\n");
			}else if (info[0].equals("Offline Gamer")) {
				token = false;
				showScore.append(msg+"Please exit the game"+"\n");
				showAll(false);
				btnStartReady.setVisible(false);
				btnExitGame.setVisible(true);
				btnDisagree.setVisible(false);
				Agree.setVisible(false);
			}else if(info[0].equals("Gamer leaving")){

				showScore.append(msg+"\n");

			}else if (info[0].equals("GameStart")) {

				isStart = true;
				showAll(isStart);
				btnStartReady.setVisible(false);


			}else if(info[0].equals("ScoreBoard")){

				Agree.setVisible(false);
				btnDisagree.setVisible(false);

				String m = "\n";
				for(int c = 1; c < info.length; c++){
					m = m +"\n"+info[c];
				}
				Score.setText(m);
				if(tempPass) {
					showScore.append("Submit row successful! Please click submit again to submit colomn!"+"\n");
				}
				if(row){
					row = false;
				}else{
					row = true;
				}



			}

		}
		// System.out.println(msg);
	}

	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public RemoteClient(String ID) throws RemoteException {
		this.ID = ID;
		this.score = 0;
	}

	public RemoteClient() throws RemoteException {
		// this.connect("aaa");
		initialize();
	}


	private void showAll(boolean istrue){


		btnPass.setVisible(istrue);
		btnSubmit.setVisible(istrue);

		btnA.setVisible(istrue);
		btnB.setVisible(istrue);
		btnC.setVisible(istrue);
		btnD.setVisible(istrue);
		btnE.setVisible(istrue);
		btnF.setVisible(istrue);
		btnG.setVisible(istrue);
		btnH.setVisible(istrue);
		btnI.setVisible(istrue);
		btnJ.setVisible(istrue);
		btnK.setVisible(istrue);
		btnL.setVisible(istrue);
		btnM.setVisible(istrue);
		btnN.setVisible(istrue);
		btnO.setVisible(istrue);
		btnP.setVisible(istrue);
		btnQ.setVisible(istrue);
		btnR.setVisible(istrue);
		btnS.setVisible(istrue);
		btnT.setVisible(istrue);
		btnU.setVisible(istrue);
		btnV.setVisible(istrue);
		btnW.setVisible(istrue);
		btnX.setVisible(istrue);
		btnY.setVisible(istrue);
		btnZ.setVisible(istrue);
	}


	public void exit() throws RemoteException {
		try {
			UnicastRemoteObject.unexportObject(this, true);
			System.out.print("Game ended.");
		}
		catch (Exception e) {
			System.out.print("Game ending failed.");
		}

	}


	private boolean isFirstPut(){
		boolean temp;
		int count = 0;
		for(int p = 0; p<bt.length;p++){
			for(int q = 0; q<bt.length;q++){

			if(bt[p][q].getActionCommand().contains("-")){
				count ++;
			}else {
				count --;
			}

			}
		}
		if(count == 400){
			temp = true;
		}else{
			temp = false;
		}

		return temp;

	}


	private void initialize() {
		try {
			// this.connect("aaa");
			// Rs = new RemoteScrabbleImpl();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		frame = new JFrame();
		frame.setBounds(300, 300, 717, 639);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				disconnect();
			}
		});

		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				disconnect();
				try {
					exit();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}

			public void windowClosed(WindowEvent e)
			{
				disconnect();
				try {
					exit();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		for (i = 0; i < 20; i++) {

			for (j = 0; j < 20; j++) {
				bt[i][j] = new JButton(i+"-"+j);
				bt[i][j].setForeground(Color.WHITE);
				bt[i][j].setBackground(Color.WHITE);
				bt[i][j].setBounds(36 + 21 * j, 35 + 21 * i, 18, 18);
				frame.getContentPane().add(bt[i][j]);
				bt[i][j].addActionListener(this);

			}
		}

		btnPass = new JButton("Pass");
		btnPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				    if(tempPass){
					game.pass(false);
					tempPass = false;
				    }
					else{
                        JOptionPane.showMessageDialog(null, " sorry, It is not your turn", null, JOptionPane.PLAIN_MESSAGE);
                    }

					mouseCount = 0;
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Try again! ", null, JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				}

			}
		});
		btnPass.setBounds(499, 174, 91, 26);
		frame.getContentPane().add(btnPass);
		btnPass.setVisible(isStart);

		    System.out.println(isHost);
        if(isHost){
            btnStartReady = new JButton("Start");
        }else{
            btnStartReady = new JButton("Ready");
        }

		btnStartReady.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isHost == false) {

						try {
//							btnSubmit.setVisible(true);
							game.ready();
							mouseCount = 0;
							//btnStartReady.setText("Ready");
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, "Try again! ", null, JOptionPane.PLAIN_MESSAGE);
							e1.printStackTrace();
						}

				}else{
					try {
//						btnSubmit.setVisible(true);
						game.start();
						mouseCount = 0;
						//btnStartReady.setText("Start");
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Try again! ", null, JOptionPane.PLAIN_MESSAGE);
						e1.printStackTrace();
					}
				}
			}
		});
		btnStartReady.setBounds(499, 271, 95, 26);
		frame.getContentPane().add(btnStartReady);
		boolean emp = isFirstPut();

		btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(602, 174, 91, 26);
		frame.getContentPane().add(btnSubmit);
		btnSubmit.setVisible(isStart);
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean emp = isFirstPut();
				try {
					if(tempPass&&!emp){
						if(row) {
							game.newWord(i, j, row);
							//row = false;

//						tempPass = false;
							//row = false;
						}else
						{
							game.newWord(i, j, row);
							tempPass = false;
//							row = true;
							token = false;
						}
					}
					else{
						JOptionPane.showMessageDialog(null, " sorry, It is not your turn or the grids is empty~", null, JOptionPane.PLAIN_MESSAGE);
					}



				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


			}
		});


		btnA = new JButton("A");
		btnA.setBounds(499, 44, 26, 26);
		frame.getContentPane().add(btnA);
		btnA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'A';
			}
		});
		btnA.setVisible(isStart);
		btnB = new JButton("B");
		btnB.setBounds(526, 44, 26, 26);
		frame.getContentPane().add(btnB);
		btnB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'B';
			}
		});
		btnB.setVisible(isStart);
		btnC = new JButton("C");
		btnC.setBounds(553, 44, 26, 26);
		frame.getContentPane().add(btnC);
		btnC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'C';
			}
		});
		btnC.setVisible(isStart);
		btnD = new JButton("D");
		btnD.setBounds(580, 44, 26, 26);
		frame.getContentPane().add(btnD);
		btnD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'D';
			}
		});
		btnD.setVisible(isStart);
		btnE = new JButton("E");
		btnE.setBounds(607, 44, 26, 26);
		frame.getContentPane().add(btnE);
		btnE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'E';
			}
		});
		btnE.setVisible(isStart);

		btnF = new JButton("F");
		btnF.setBounds(634, 44, 26, 26);
		frame.getContentPane().add(btnF);
		btnF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'F';
			}
		});
		btnF.setVisible(isStart);

		btnG = new JButton("G");
		btnG.setBounds(661, 44, 26, 26);
		frame.getContentPane().add(btnG);
		btnG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'G';
			}
		});
		btnG.setVisible(isStart);

		btnH = new JButton("H");
		btnH.setBounds(499, 71, 26, 26);
		frame.getContentPane().add(btnH);
		btnH.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'H';
			}
		});
		btnH.setVisible(isStart);

		btnI = new JButton("I");
		btnI.setBounds(526, 71, 26, 26);
		frame.getContentPane().add(btnI);
		btnI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'I';
			}
		});
		btnI.setVisible(isStart);

		btnJ = new JButton("J");
		btnJ.setBounds(553, 71, 26, 26);
		frame.getContentPane().add(btnJ);

		btnJ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'J';
			}
		});
		btnJ.setVisible(isStart);

		btnK = new JButton("K");
		btnK.setBounds(580, 71, 26, 26);
		frame.getContentPane().add(btnK);
		btnK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'K';
			}
		});
		btnK.setVisible(isStart);

		btnL = new JButton("L");
		btnL.setBounds(607, 71, 26, 26);
		frame.getContentPane().add(btnL);
		btnL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'L';
			}
		});
		btnL.setVisible(isStart);

		btnP = new JButton("P");
		btnP.setBounds(526, 98, 26, 26);
		frame.getContentPane().add(btnP);
		btnP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'P';
			}
		});
		btnP.setVisible(isStart);

		btnO = new JButton("O");
		btnO.setBounds(499, 98, 26, 26);
		frame.getContentPane().add(btnO);
		btnO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'O';
			}
		});
		btnO.setVisible(isStart);

		btnN = new JButton("N");
		btnN.setBounds(661, 71, 26, 26);
		frame.getContentPane().add(btnN);
		btnN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'N';
			}
		});
		btnN.setVisible(isStart);

		btnM = new JButton("M");
		btnM.setBounds(634, 71, 26, 26);
		frame.getContentPane().add(btnM);
		btnM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'M';
			}
		});
		btnM.setVisible(isStart);

		btnQ = new JButton("Q");
		btnQ.setBounds(553, 98, 26, 26);
		frame.getContentPane().add(btnQ);
		btnQ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'O';
			}
		});
		btnQ.setVisible(isStart);

		btnR = new JButton("R");
		btnR.setBounds(580, 98, 26, 26);
		frame.getContentPane().add(btnR);
		btnR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'R';
			}
		});
		btnR.setVisible(isStart);

		btnS = new JButton("S");
		btnS.setBounds(607, 98, 26, 26);
		frame.getContentPane().add(btnS);
		btnS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'S';
			}
		});
		btnS.setVisible(isStart);

		btnT = new JButton("T");
		btnT.setBounds(634, 98, 26, 26);
		frame.getContentPane().add(btnT);
		btnT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'T';
			}
		});
		btnT.setVisible(isStart);

		btnU = new JButton("U");
		btnU.setBounds(661, 98, 26, 26);
		frame.getContentPane().add(btnU);
		btnU.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'U';
			}
		});
		btnU.setVisible(isStart);

		btnV = new JButton("V");
		btnV.setBounds(499, 125, 26, 26);
		frame.getContentPane().add(btnV);
		btnV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'V';
			}
		});
		btnV.setVisible(isStart);

		btnW = new JButton("W");
		btnW.setBounds(526, 125, 26, 26);
		frame.getContentPane().add(btnW);
		btnW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'W';
			}
		});
		btnW.setVisible(isStart);

		btnX = new JButton("X");
		btnX.setBounds(553, 125, 26, 26);
		frame.getContentPane().add(btnX);
		btnX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'X';
			}
		});
		btnX.setVisible(isStart);

		btnY = new JButton("Y");
		btnY.setBounds(580, 125, 26, 26);
		frame.getContentPane().add(btnY);
		btnY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'Y';
			}
		});
		btnY.setVisible(isStart);

		btnZ = new JButton("Z");
		btnZ.setBounds(607, 125, 26, 26);
		frame.getContentPane().add(btnZ);
		btnZ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letter = 'Z';
			}
		});
		btnZ.setVisible(isStart);
		label = new JLabel("  0   1    2   3   4    5   6   7   8   9  10 11 12  13 14 15 16  17 18  19");
		label.setBounds(35, 18, 426, 15);
		frame.getContentPane().add(label);

		for (j = 0; j < 20; j++) {

			lb[0][j] = new JLabel(String.valueOf(j));
			lb[0][j].setVerticalAlignment(SwingConstants.BOTTOM);
			lb[0][j].setBounds(18, 30 + j * 21, 23, 23);
			frame.getContentPane().add(lb[0][j]);
		}



		showScore = new JTextArea();
		showScore.setEditable(false);
		showScore.setBounds(49, 501, 635, 100);

		frame.getContentPane().add(showScore);

		btnExitGame = new JButton("Exit Game");
		btnExitGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("disconnect");
				disconnect();
				try {
					exit();
					frame.setVisible(false);
					frame.dispose();
//					System.exit(0);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnExitGame.setBounds(598, 272, 95, 26);
		frame.getContentPane().add(btnExitGame);

		Agree = new JButton("Agree");
		Agree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println(row);
					game.setAgree(row);
					System.out.println("agree");
					mouseCount = 0;

				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		Agree.setBounds(499, 222, 91, 26);
		frame.getContentPane().add(Agree);
		Agree.setVisible(isStart);

		btnDisagree = new JButton("Disagree");
		btnDisagree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					game.setDisagree(row);
					mouseCount = 0;
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnDisagree.setBounds(598, 222, 95, 26);
		frame.getContentPane().add(btnDisagree);
		btnDisagree.setVisible(isStart);

		Score = new JTextArea();
		Score.setBounds(509, 349, 175, 111);
		Score.setEditable(false);
		frame.getContentPane().add(Score);

		JLabel lblScore = new JLabel("Score:");
		lblScore.setBounds(498, 311, 81, 26);
		frame.getContentPane().add(lblScore);

		JLabel lblInformationBoard = new JLabel("Information Board:");
		lblInformationBoard.setBounds(53, 473, 133, 26);
		frame.getContentPane().add(lblInformationBoard);

		JScrollPane sp1 = new JScrollPane(showScore);
		sp1.setBounds(49, 501, 635, 100);
		sp1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		sp1.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(sp1);

		JScrollPane sp2 = new JScrollPane(Score);
		sp2.setBounds(509, 349, 175, 111);
		sp2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		sp2.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(sp2);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		boolean temp;
		temp = isFirstPut();
		mouseCount++;
		if(isStart){
			if(token){
		if (e.getActionCommand().contains("-")) {
			if (mouseCount == 1) {
				try {
					String [] a = new String[2];

					a  = e.getActionCommand().split("-");
					i = Integer.parseInt(a[0]);
					j = Integer.parseInt(a[1]);
                    if (bt[i][j].getText() == e.getActionCommand() ) {
					if(temp){

						bt[i][j].setForeground(Color.BLACK);
						token = false;
//						tempPass = true;

						game.setCharacter(i, j, letter);

					} else {
//						tempPass = true;
						if(i == 0 && j == 0){
							if(!bt[i][j+1].getActionCommand().contains("-")||!bt[i+1][j].getActionCommand().contains("-")){
                                bt[i][j].setForeground(Color.BLACK);
                                token = false;
//								tempPass = true;
                                game.setCharacter(i, j, letter);

							}else{
                                mouseCount = 0;
                                JOptionPane.showMessageDialog(null, "sorry, you cannot put the character here!", null,
                                        JOptionPane.PLAIN_MESSAGE);

                            }

						}else if(i == 19 && j == 19){
                            if( !bt[i][j-1].getActionCommand().contains("-")||!bt[i-1][j].getActionCommand().contains("-")){
                                bt[i][j].setForeground(Color.BLACK);
                                token = false;
//								tempPass = true;
                                game.setCharacter(i, j, letter);

                            }else{
                                mouseCount = 0;
                                JOptionPane.showMessageDialog(null, "sorry, you cannot put the character here!", null,
                                        JOptionPane.PLAIN_MESSAGE);

                            }
                        }else if(i == 0 && j == 19){
                            if( !bt[i][j-1].getActionCommand().contains("-")||!bt[i+1][j].getActionCommand().contains("-")){
                                bt[i][j].setForeground(Color.BLACK);
                                token = false;
//								tempPass = true;
                                game.setCharacter(i, j, letter);

                            }else{
                                mouseCount = 0;
                                JOptionPane.showMessageDialog(null, "sorry, you cannot put the character here!", null,
                                        JOptionPane.PLAIN_MESSAGE);

                            }
                        }else if(i == 19 && j == 0){
                            if( !bt[i][j+1].getActionCommand().contains("-")||!bt[i][j-1].getActionCommand().contains("-")){
                                bt[i][j].setForeground(Color.BLACK);
                                token = false;
                                game.setCharacter(i, j, letter);

                            }else{
                                mouseCount = 0;
                                JOptionPane.showMessageDialog(null, "sorry, you cannot put the character here!", null,
                                        JOptionPane.PLAIN_MESSAGE);

                            }

                        }else if(i == 0 && j != 0 && j != 19){
                            if(!bt[i][j+1].getActionCommand().contains("-")|| !bt[i][j-1].getActionCommand().contains("-")||!bt[i+1][j+1].getActionCommand().contains("-")){
                                bt[i][j].setForeground(Color.BLACK);
                                token = false;
                                game.setCharacter(i, j, letter);

                            }else{
                                mouseCount = 0;
                                JOptionPane.showMessageDialog(null, "sorry, you cannot put the character here!", null,
                                        JOptionPane.PLAIN_MESSAGE);

                            }
                        }else if(i == 19 && j != 0 && j != 19){
                            if( !bt[i][j+1].getActionCommand().contains("-")|| !bt[i][j-1].getActionCommand().contains("-")||!bt[i-1][j].getActionCommand().contains("-")){
                                bt[i][j].setForeground(Color.BLACK);
                                token = false;
                                game.setCharacter(i, j, letter);

                            }else{
                                mouseCount = 0;
                                JOptionPane.showMessageDialog(null, "sorry, you cannot put the character here!", null,
                                        JOptionPane.PLAIN_MESSAGE);

                            }
                        }else if(j == 0  && i != 0 && i != 19){
                            if( !bt[i+1][j].getActionCommand().contains("-")||!bt[i-1][j].getActionCommand().contains("-")||!bt[i][j+1].getActionCommand().contains("-")){
                                bt[i][j].setForeground(Color.BLACK);
                                token = false;
                                game.setCharacter(i, j, letter);

                            }else{
                                mouseCount = 0;
                                JOptionPane.showMessageDialog(null, "sorry, you cannot put the character here!", null,
                                        JOptionPane.PLAIN_MESSAGE);

                            }
                        }else if(j == 19  && i != 0 && i != 19){
                            if( !bt[i+1][j].getActionCommand().contains("-")|| !bt[i-1][j].getActionCommand().contains("-")||!bt[i][j-1].getActionCommand().contains("-")){
                                bt[i][j].setForeground(Color.BLACK);
                                token = false;
                                game.setCharacter(i, j, letter);

                            }else{
                                mouseCount = 0;
                                JOptionPane.showMessageDialog(null, "sorry, you cannot put the character here!", null,
                                        JOptionPane.PLAIN_MESSAGE);

                            }
                        }else {
                            if( !bt[i+1][j].getActionCommand().contains("-")|| !bt[i-1][j].getActionCommand().contains("-")||!bt[i][j-1].getActionCommand().contains("-")|| !bt[i][j+1].getActionCommand().contains("-")){
                                bt[i][j].setForeground(Color.BLACK);
                                token = false;
                                game.setCharacter(i, j, letter);

                            }else{
                                mouseCount = 0;
                                JOptionPane.showMessageDialog(null, "sorry, you cannot put the character here!", null,
                                        JOptionPane.PLAIN_MESSAGE);

                            }
                        }



					}}
					else {
                        mouseCount = 0;
                        JOptionPane.showMessageDialog(null, "sorry, you cannot put the character here!", null,
                                JOptionPane.PLAIN_MESSAGE);
                    }
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "sorry, you just can input one letter in your turn", null,
						JOptionPane.PLAIN_MESSAGE);
			}}else {
			mouseCount = 0;
			JOptionPane.showMessageDialog(null, "sorry, you cannot put the character here!", null,
					JOptionPane.PLAIN_MESSAGE);

		}}else{
				JOptionPane.showMessageDialog(null, "sorry, It's not your turn~", null,
						JOptionPane.PLAIN_MESSAGE);

			} }else{
			JOptionPane.showMessageDialog(null, "sorry, the game is not ready~", null,
					JOptionPane.PLAIN_MESSAGE);
		}
//tf[i][j].setFont();
		System.out.println(e.getActionCommand()+"hello");
//  System.out.println(e.getModifiers() +"hello");

	}
}
