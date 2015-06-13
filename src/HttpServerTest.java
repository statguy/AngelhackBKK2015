
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



/*
 * a simple static http server
*/
public class HttpServerTest {
	public static void main(String[] args) throws Exception {
		
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
		    conn =
		       DriverManager.getConnection("jdbc:mysql://128.199.133.166/roomlinkdbmysql?" +
		                                   "user=rls2014ss&password=rls2014ss");

		    // Do something with the Connection
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		    System.exit(-1);
		}
		
	  
	    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
	    server.createContext("/", new MyHandler());
	    server.setExecutor(null); // creates a default executor
	    server.start();
	}

  static public Map<String, String> queryToMap(String query){
	  if (query == null) return null;
	    Map<String, String> result = new HashMap<String, String>();
	    for (String param : query.split("&")) {
	        String pair[] = param.split("=");
	        if (pair.length>1) {
	            result.put(pair[0], pair[1]);
	        }else{
	            result.put(pair[0], "");
	        }
	    }
	    return result;
	}

  
  static class MyHandler implements HttpHandler {
	  public void handle(HttpExchange t) throws IOException {
		  final Headers headers = t.getResponseHeaders();
		  final String requestMethod = t.getRequestMethod().toUpperCase();
		  
		  String response = "";

		  System.out.println("Request received " + t.getRequestURI().getQuery());
		  
		  JSONObject obj = new JSONObject();
		  Map<String, String> params = queryToMap(t.getRequestURI().getQuery()); 
		  
		  String path = t.getRequestURI().getPath();
		  if (path.equals("/findmatch")) {

		      if (params != null) {
		    	  String lat = params.get("lat");
		    	  String lng = params.get("lng");
		    	  
		    	  if (lat.equals("1") && lng.equals("1")) {
		    		  obj.put("field", "bangkok");
		    	  }
		    	  else if (lat.equals("2") && lng.equals("1")) {
		    		  obj.put("field", "pattaya");
		    	  }
		    	  else {
		    	  }
		    	  
		    	  response += obj.toJSONString();
		      }
		  }
		  	      
		  t.sendResponseHeaders(200, response.length());
		  OutputStream os = t.getResponseBody();
		  os.write(response.getBytes());
		  os.close();
	  }
  }

}
