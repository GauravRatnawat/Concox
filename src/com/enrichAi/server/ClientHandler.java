package com.enrichAi.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class ClientHandler extends Thread 
{ 
	DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd"); 
	DateFormat fortime = new SimpleDateFormat("hh:mm:ss"); 
	final DataInputStream dis; 
	final DataOutputStream dos; 
	final Socket s; 
	ThreadLocal<String> tt=new ThreadLocal<String>();
	// Constructor 
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) 
	{ 
		this.s = s; 
		this.dis = dis; 
		this.dos = dos; 
	} 

	@Override
	public void run() 
	{ 
		String received; 
		String toreturn; 
		int count=0;
		while (true) 
		{ 
			try { 

				// Ask user what he wants 
				//				DOS.WRITEUTF("WHAT DO YOU WANT?[DATE | TIME]..\N"+ 
				//							"TYPE EXIT TO TERMINATE CONNECTION."); 

				// receive the answer from client 
				received = dis.readUTF(); 
				char charAt = received.charAt(6);
				char charAt2 = received.charAt(7);
				if(received.length()>0&&received.startsWith("7878"))
				{
					if(charAt=='0'&&charAt2=='1')
					{
						if(checkCRC(received.substring(4, 36),received.substring(36,40))) {
							BigInteger bigInt = new BigInteger(received.substring(8, 24), 16);
							tt.set(bigInt.toString());
							StringBuffer sb=new StringBuffer();
							sb.append(received.substring(0, 5));
							sb.append("0501");
							String hexvalue=Integer.toHexString(++count);
							sb.append(String.format("%04X", Integer.parseInt(hexvalue)));
							sb.append(CRCITU.process(sb.substring(4, 12)));
							sb.append("0D0A");
							System.out.println(sb);
							toreturn=sb.toString();
							dos.writeUTF(toreturn);
						}

					}
					else
						if(charAt=='2'&&charAt2=='3')
						{
							if(checkCRC(received.substring(4, 24),received.substring(24,28))) {
								StringBuffer sb=new StringBuffer();
								sb.append(received.substring(0, 5));
								sb.append("0523");
								String hexvalue=Integer.toHexString(++count);
								sb.append(String.format("%04X", Integer.parseInt(hexvalue)));
								sb.append(CRCITU.process(sb.substring(4, 12)));
								sb.append("0D0A");
								System.out.println(sb);
								toreturn=sb.toString();
								dos.writeUTF(toreturn);
							}
						}
						else
							if(charAt=='2'&&charAt2=='2')
							{
								if(checkCRC(received.substring(4, 76),received.substring(76,80))) {
									Data d=new Data();
									String lat = received.substring(22, 30);
									long parseLat = Long.parseLong(lat,16)/1800000; 
									d.setLatitude(parseLat+"");
									String longi = received.substring(30, 38);
									long parseLong = Long.parseLong(longi,16)/1800000; 
									d.setLatitude(parseLong+"");
									String speed=received.substring(38, 42);

									long speed1 = Long.parseLong(speed,16);
									d.setSpeed(speed1+"");
									d.setDeviceId(tt.get());
									System.out.println(d.toString());
									FileUtil.datafilecreation(d.toString());
								}

							}
					if(received.equals("Exit")) 
					{ 
						System.out.println("Client " + this.s + " sends exit..."); 
						System.out.println("Closing this connection."); 
						this.s.close(); 
						System.out.println("Connection closed"); 
						break; 
					} 

					// creating Date object 
					Date date = new Date(); 

					// write on output stream based on the 
					// answer from the client 
					switch (received) { 

					case "Date" : 
						toreturn = fordate.format(date); 
						dos.writeUTF(toreturn); 
						break; 

					case "Time" : 
						toreturn = fortime.format(date); 
						dos.writeUTF(toreturn); 
						break; 

					default: 
						dos.writeUTF("Invalid input"); 
						break; 
					} 
				}
			} catch (IOException e) { 
				try {
					this.dis.close();
					this.dos.close(); 
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					this.interrupt();
				} 
				
				this.interrupt();
				e.printStackTrace(); 
			} 
		} 

		try
		{ 
			// closing resources 
			this.dis.close(); 
			this.dos.close(); 

		}catch(IOException e){ 
			e.printStackTrace(); 
		} 
	}

	private boolean checkCRC(String substring, String substring2) {
		// TODO Auto-generated method stub

		return CRCITU.process(substring).equals(substring2);
	}


} 
