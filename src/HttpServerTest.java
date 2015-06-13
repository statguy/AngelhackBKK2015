
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
/*
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 8000;
    private static final int BACKLOG = 1;

    private static final String HEADER_ALLOW = "Allow";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private static final int STATUS_OK = 200;
    private static final int STATUS_METHOD_NOT_ALLOWED = 405;

    private static final int NO_RESPONSE_LENGTH = -1;

    private static final String METHOD_GET = "GET";
    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String ALLOWED_METHODS = METHOD_GET + "," + METHOD_OPTIONS;

    public static void main(final String... args) throws IOException {
        final HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);
        server.createContext("/func1", he -> {
            try {
                final Headers headers = he.getResponseHeaders();
                final String requestMethod = he.getRequestMethod().toUpperCase();
                switch (requestMethod) {
                    case METHOD_GET:
                        final Map<String, List<String>> requestParameters = getRequestParameters(he.getRequestURI());
                        // do something with the request parameters
                        final String responseBody = "['hello world!']";
                        headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
                        final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
                        he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
                        he.getResponseBody().write(rawResponseBody);
                        break;
                    case METHOD_OPTIONS:
                        headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                        he.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                        break;
                    default:
                        headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                        he.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                        break;
                }
            } finally {
                he.close();
            }
        });
        server.start();
    }

    private static Map<String, List<String>> getRequestParameters(final URI requestUri) {
        final Map<String, List<String>> requestParameters = new LinkedHashMap<>();
        final String requestQuery = requestUri.getRawQuery();
        if (requestQuery != null) {
            final String[] rawRequestParameters = requestQuery.split("[&;]", -1);
            for (final String rawRequestParameter : rawRequestParameters) {
                final String[] requestParameter = rawRequestParameter.split("=", 2);
                final String requestParameterName = decodeUrlComponent(requestParameter[0]);
                requestParameters.putIfAbsent(requestParameterName, new ArrayList<>());
                final String requestParameterValue = requestParameter.length > 1 ? decodeUrlComponent(requestParameter[1]) : null;
                requestParameters.get(requestParameterName).add(requestParameterValue);
            }
        }
        return requestParameters;
    }

    private static String decodeUrlComponent(final String urlComponent) {
        try {
            return URLDecoder.decode(urlComponent, CHARSET.name());
        } catch (final UnsupportedEncodingException ex) {
            throw new InternalError(ex);
        }
    }
*/
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
		  //String response = "<html><body>";
		  //response += "your request method was = " + requestMethod + "<br>";

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
		    		  //obj.put("field", null);
		    	  }
		    	  
		    	  response += obj.toJSONString();
		      }
		  }
		  
		  		  
    	
	      //response += "</body></html>";
	      
		  t.sendResponseHeaders(200, response.length());
		  OutputStream os = t.getResponseBody();
		  os.write(response.getBytes());
		  os.close();
	  }
  }

}
