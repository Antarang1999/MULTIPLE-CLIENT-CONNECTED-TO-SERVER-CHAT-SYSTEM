package cn_project;
import java.io.*; 
import java.net.*; 
import java.util.Scanner; 
  
public class Client1  
{ 
    final static int ServerPort = 4000;
  
    public static void main(String args[]) throws UnknownHostException, IOException  
    { 
        Scanner scn = new Scanner(System.in); 
                
        // establish the connection 
        Socket s = new Socket("localhost", ServerPort);     
        // obtaining input and out streams 
        DataInputStream dis = new DataInputStream(s.getInputStream()); 
        DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
  
        // sendMessage thread 
        Thread sendMessage = new Thread(new Runnable()  
        { 
            @Override
            public void run() { 
                while (true) { 
  
                    // read the message to deliver. 
                    String msg = scn.nextLine(); 
                      
                    try { 
                        // write on the output stream 
                        dos.writeUTF(msg); 
                    } catch (IOException e) { break;} 
                }
                System.out.println("Got a logout Signal ---- Disconnecting"); 
              System.exit(0);
            } 
        }); 
          
        // readMessage thread 
        Thread readMessage = new Thread(new Runnable()  
        { 
            @Override
            public void run() { 
  
                while (true) { 
                    try { 
                        // read the message sent to this client 
                        String msg = dis.readUTF(); 
                        System.out.println(msg); 
                    } catch (IOException e) { 
                       break;
                      } 
                }
                 System.out.println("Got a logout signal ---- Disconnecting");
                 System.exit(0);
            }
            
        }); 
  
        sendMessage.start(); 
        readMessage.start(); 
  
    } 
} 
