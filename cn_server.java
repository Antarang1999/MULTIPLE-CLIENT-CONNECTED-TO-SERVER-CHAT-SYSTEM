package cn_project;
import java.io.*; 
import java.util.*; 
import java.net.*;   

public class Server  
{ 
    
    static Vector<Mediator> client = new Vector<>();  
    static int i = 1;
    public static void main(String[] args) throws IOException  
    {  
        ServerSocket ss = new ServerSocket(4000);     
        Socket s;  
        while (true)  
        { 
           //Accepting client request
            s = ss.accept(); 
            System.out.println("New client request received from [Address: " + s.toString().substring(13, 23)+"Port:"+s.toString().substring(29,34)+"Local port "+s.toString().substring(44, 49)); 
            DataInputStream dis = new DataInputStream(s.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
              
            System.out.println("Creating a new Mediator for client "+i);  
            // Create a new Mediator for handling this request. 
            Mediator mediate = new Mediator(s,"client " + i, dis, dos); 
            // Create a new Thread with this object. 
            Thread t = new Thread(mediate);    
            System.out.println("Adding this client to active client list"); 
            client.add(mediate);  
            t.start(); 
            i++; 
        } 
    } 
} 

class Mediator implements Runnable  
{ 
    Scanner scn = new Scanner(System.in); 
    private String name; 
    final DataInputStream dis; 
    final DataOutputStream dos; 
    Socket s; 
    boolean isloggedin; 
      
    public Mediator(Socket s, String name,DataInputStream dis, DataOutputStream dos) { 
        this.dis = dis; 
        this.dos = dos; 
        this.name = name; 
        this.s = s; 
        this.isloggedin=true; 
    } 
  
    @Override
    public void run() { 
  
        
        while (true)  
        { 
            try
            { 
                String received = dis.readUTF();
                FileWriter f=new FileWriter("C:/CNA/cn.txt",true);
                BufferedWriter bw =new BufferedWriter(f);
                bw.write(received+"\n");
                
                bw.close();
                System.out.println(received);
                
                if(received.equals("logout")){
                    System.out.println("Got a logout signal");
                    this.isloggedin=false; 
                    this.s.close();//leaving the socket  
                    break; 
                } 
               //Breaking the string with #
                StringTokenizer st = new StringTokenizer(received, "#"); 
                String MsgToSend = st.nextToken(); 
                String recipient = st.nextToken(); 
             
                for (Mediator mediator : Server.client)  
                { 
                    if (mediator.name.equals(recipient) && mediator.isloggedin==true)  
                    { 
                        mediator.dos.writeUTF(this.name+" : "+MsgToSend); 
                        break; 
                    } 
                } 
            } 
            catch (IOException e) {} 
              
        }
        System.out.println("Disconnecting "+this.name.toUpperCase());
        try
        { 
            this.dis.close(); 
            this.dos.close();       
        }
        catch(IOException e){ } 
    } 
} 
