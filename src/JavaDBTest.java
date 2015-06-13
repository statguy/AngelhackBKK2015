// https://db.apache.org/derby/docs/10.4/adminguide/tadmincbdjhhfd.html
// export DERBY_HOME=$HOME/bin/derby
// export PATH="$DERBY_HOME/bin:$PATH"
// startNetworkServer

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class JavaDBTest {
/*
	public static void main(String[] args) {
		try {
            // connect method #1 - embedded driver
            String dbURL1 = "jdbc:derby://localhost:1527/dbname;create=true";
            Connection conn1 = DriverManager.getConnection(dbURL1);
            if (conn1 != null) {
                System.out.println("Connected to database #1");
            }
            else {
            	System.out.println("Database connection failure.");
            	System.exit(-1);
            }
/*
            // connect method #2 - network client driver
            String dbURL2 = "jdbc:derby://localhost/webdb2;create=true";
            String user = "tom";
            String password = "secret";
            Connection conn2 = DriverManager.getConnection(dbURL2, user, password);
            if (conn2 != null) {
                System.out.println("Connected to database #2");
            }
 */
        /*                
            ServerSocket serverSocket = null;
    		try {
    			serverSocket = new ServerSocket(4444); // Server socket    			
    		}
    		catch (IOException e) {
    			System.out.println("Could not listen on port: 4444");
    			System.exit(-1);
    		}
 		//System.out.println("host address: " + InetAddress.getLocalHost());

    		Socket clientSocket = null;
    		InputStreamReader inputStreamReader = null;
    		BufferedReader bufferedReader = null;
    		PrintWriter writer = null;
    		
			try {
				clientSocket = serverSocket.accept(); // accept the client connection
				inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
				bufferedReader = new BufferedReader(inputStreamReader); // get the client message
				writer = new PrintWriter(clientSocket.getOutputStream());

	    		while (true) {
	    			System.out.println("Listening at "+ clientSocket.getInetAddress().getHostAddress());
	    			String message = bufferedReader.readLine();		
	    			System.out.println("Received " + message);
    				writer.write("Echo " + message + "\r\n");
    				writer.flush();	    				
	    			Thread.sleep(100);
	    		}
	    	}
			catch (IOException ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
			catch (InterruptedException ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
			finally {
				try {
					inputStreamReader.close();
					writer.close();	    				
					clientSocket.close();
				}
				catch (IOException ex) {
					ex.printStackTrace();
					System.exit(-1);					
				}
			}
        }
		catch (SQLException ex) {
            ex.printStackTrace();
        }
		finally {

		}
	}
*/
}
