import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class HttpServerTest {
	static Connection db = null;
	static String connectionString = "jdbc:mysql://128.199.133.166/anglehackbkk?" +
		    		"user=rls2014ss&password=rls2014ss";
	
	public static void main(String[] args) throws Exception {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		    db = DriverManager.getConnection(connectionString);
		}
		catch (SQLException ex) {
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		    System.exit(-1);
		}
	  
	    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
	    server.createContext("/", new MyHandler());
	    server.setExecutor(null);
	    server.start();
	}

	static public Map<String, String> queryToMap(String query) {
		if (query == null) return null;
		Map<String, String> result = new HashMap<String, String>();
		for (String param : query.split("&")) {
		    String pair[] = param.split("=");
		    if (pair.length>1) {
		        result.put(pair[0], pair[1]);
		    }
		    else {
		        result.put(pair[0], "");
		    }
		}
	    return result;
	}
  
	public static List<JSONObject> getFormattedResult(ResultSet rs) {
	    List<JSONObject> resList = new ArrayList<JSONObject>();
	    try {
	        ResultSetMetaData rsMeta = rs.getMetaData();
	        int columnCnt = rsMeta.getColumnCount();
	        List<String> columnNames = new ArrayList<String>();
	        for(int i = 1; i <= columnCnt; i++) {
	            columnNames.add(rsMeta.getColumnName(i).toUpperCase());
	        }

	        while(rs.next()) { // convert each object to an human readable JSON object
	            JSONObject obj = new JSONObject();
	            for(int i = 1; i <= columnCnt; i++) {
	                String key = columnNames.get(i - 1);
	                String value = rs.getString(i);
	                obj.put(key, value);
	            }
	            resList.add(obj);
	        }
	    }
	    catch(Exception e) {
	        e.printStackTrace();
	    }
	    finally {
	        try {
	            rs.close();
	        }
	        catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return resList;
	}
  
  	static class MyHandler implements HttpHandler {
		  public void handle(HttpExchange exchange) throws IOException {
			  //final Headers headers = exchange.getResponseHeaders();
			  //final String requestMethod = exchange.getRequestMethod().toUpperCase();
			  
			  System.out.println("Request received " + exchange.getRequestURI().getQuery());
			  
			  String method = exchange.getRequestMethod();
			  if (!method.equals("GET")) {
				  System.out.println("Invalid request method.");
				  exchange.sendResponseHeaders(400, 0);
				  exchange.close();
				  return;
			  }
			  
			  String response = "";		  
			  List<JSONObject> JSONObjectList = new ArrayList<JSONObject>();
			  Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery()); 
			  
			  String path = exchange.getRequestURI().getPath();
			  if (path.equals("/findmatch")) {
				  if (params != null) {			  
					  try {
						  String lat = params.get("lat");
						  String lng = params.get("lng");
						  String dist = params.get("dist");
						  
						  // TODO: check for valid parameters
						  
						  Statement stm = db.createStatement();
						  String query = "SELECT id,name,lat,lng,type FROM fields";// WHERE lat="+lat+" AND lng"+lng;
						  ResultSet rs = stm.executeQuery(query);
						  JSONObjectList = getFormattedResult(rs);
					  }
					  catch (SQLException e) {
						e.printStackTrace();
					  }
					  
					  response += "[";
					  for (int i = 0; i < JSONObjectList.size(); i++) {
						  JSONObject o = JSONObjectList.get(i);
						  System.out.println(o.toJSONString());
				    	  response += o.toJSONString();
				    	  if (!(i == JSONObjectList.size()-1)) response += ",\r\n";
					  }
					  response += "]\r\n";
			      }
			  }
			  else {
				  System.out.println("Unrecognized query function.");
				  exchange.sendResponseHeaders(400, 0);
				  exchange.close();
				  return;
			  }
			  
			  byte[] responseBytes = response.getBytes("UTF-8");
			  System.out.println("Response length " + responseBytes.length);
			  if (responseBytes.length != 0) {
				  exchange.sendResponseHeaders(200, responseBytes.length);
			  }
			  OutputStream os = exchange.getResponseBody();
			  os.write(responseBytes);
			  os.close();
			  exchange.close();
		  }
	 }
}
