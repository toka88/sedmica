package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;


/**
 * This class works in conjunction with TcpClient.java and TcpPayload.java
 *
 * This server test class opens a socket on localhost and waits for a client
 * to connect. When a client connects, this server serializes an instance of
 * TcpPayload and sends it to the client.
 */

public class TcpServer
{
    public final static int COMM_PORT = 2222;  // socket port for client comms

    private ServerSocket serverSocket;


	private volatile static ArrayList<Soba> listaSoba = new ArrayList<Soba>();
    private PaketSoba sobeNaServeru;
	@SuppressWarnings("unused")
	private InetSocketAddress inboundAddr;
    /** Default constructor. */
    public TcpServer()
    {
        NabaviPaket zahtjevaniPakt;
        initServerSobe();
        sobeNaServeru = new PaketSoba(listaSoba);
        initServerSocket();
        try
        {
            while (true)
            {
                // listen for and accept a client connection to serverSocket
                Socket sock = this.serverSocket.accept();
                
                ObjectInputStream oiStream = new ObjectInputStream(sock.getInputStream());
                zahtjevaniPakt = (NabaviPaket) oiStream.readObject();
                
                if( zahtjevaniPakt.isIndex(0)){
                	ObjectOutputStream ooStream = new ObjectOutputStream(sock.getOutputStream());
                	ooStream.writeObject(sobeNaServeru);  // send serilized payload
                	ooStream.close();               	
                }
                else System.out.println("Paket nije dobrog index.a");

                Thread.sleep(1000);
            }
        }
        catch (SecurityException se)
        {
            System.err.println("Unable to get host address due to security.");
            System.err.println(se.toString());
            System.exit(1);
        }
        catch (IOException ioe)
        {
            System.err.println("Unable to read data from an open socket.");
            System.err.println(ioe.toString());
            System.exit(1);
        }
        catch (InterruptedException ie) { }  // Thread sleep interrupted
        catch (ClassNotFoundException e) {
            System.err.println("Nema klase");
            System.exit(1);
		}
        finally
        {
            try
            {
                this.serverSocket.close();
            }
            catch (IOException ioe)
            {
                System.err.println("Unable to close an open socket.");
                System.err.println(ioe.toString());
                System.exit(1);
            }
        }
    }
    private static void initServerSobe() {
		for (int i = 0; i < 10; i++) {
			listaSoba.add(new Soba(i));
		}
		//listaSoba.get(0).getPopisIgraca().add(new Igrac(12));
		//System.out.println("Server: Sobe inicijalizirane.");
	}

    /** Initialize a server socket for communicating with the client. */
    private void initServerSocket()
    {
        this.inboundAddr = new InetSocketAddress(COMM_PORT);
        try
        {
            this.serverSocket = new java.net.ServerSocket(COMM_PORT);
            assert this.serverSocket.isBound();
            if (this.serverSocket.isBound())
            {
                System.out.println("SERVER inbound data port " +
                    this.serverSocket.getLocalPort() +
                    " is ready and waiting for client to connect...");
            }
        }
        catch (SocketException se)
        {
            System.err.println("Unable to create socket.");
            System.err.println(se.toString());
            System.exit(1);
        }
        catch (IOException ioe)
        {
            System.err.println("Unable to read data from an open socket.");
            System.err.println(ioe.toString());
            System.exit(1);
        }
    }

    /**
     * Run this class as an application.
     */
    public static void main(String[] args)
    {
        new TcpServer();
    }
}