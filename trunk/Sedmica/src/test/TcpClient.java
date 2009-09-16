package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * TcpClient.java
 *
 * This class works in conjunction with TcpServer.java and TcpPayload.java
  *
 * This client test class connects to server class TcpServer, and in response,
* it receives a serialized an instance of TcpPayload.
 */

public class TcpClient
{
    public final static String SERVER_HOSTNAME = "127.0.0.1";
    public final static int COMM_PORT = 2222;  // socket port for client comms

    private Socket socket;

    private PaketSoba sobeNaServeru;
    
    /** Default constructor. */
    public TcpClient()
    {
        try
        {
            this.socket = new Socket(SERVER_HOSTNAME, COMM_PORT);
            
            ObjectOutputStream ooStream = new ObjectOutputStream(socket.getOutputStream());
            
            ooStream.writeObject( new NabaviPaket(0));  // send serilized payload
            
            ObjectInputStream oiStream = new ObjectInputStream(socket.getInputStream());
            sobeNaServeru = (PaketSoba) oiStream.readObject();
            
            ooStream.close();
            oiStream.close();
        }
        catch (UnknownHostException uhe)
        {
            System.out.println("Don't know about host: " + SERVER_HOSTNAME);
            System.exit(1);
        }
        catch (IOException ioe)
        {
            System.out.println("Couldn't get I/O for the connection to: " +
                SERVER_HOSTNAME + ":" + COMM_PORT);
            System.exit(1);
        }
        catch(ClassNotFoundException cne)
        {
            System.out.println("Wanted class TcpPayload, but got class " + cne);
        }
        
        sobeNaServeru.ispisSoba();
    }

    /**
     * Run this class as an application.
     */
    public static void main(String[] args)
    {
    	TcpClient p = new TcpClient();
    }
}