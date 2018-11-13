import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import org.json.*;
import java.util.*;

public class HackServer {
	private final static String GET = "GET";

	HttpServer server;

	public void startServer(int port) {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
			HttpContext addressRoute = server.createContext("/address");
			addressRoute.setHandler(HackServer::handleAddressRequest);

			HttpContext streetRoute = server.createContext("/street");
			streetRoute.setHandler(HackServer::handleStreetRequest);

			server.start();
		} catch (Exception e) {
			System.out.printf("Could not start server on port %d/n", port);
		}
	}

	public static void handleAddressRequest(HttpExchange exchange) {
		String response = "";
		try {
			if (exchange.getRequestMethod().equals(GET)) {
				Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
				Queries q = new Queries();
				JSONObject myObject = q.queryAddress(params.get("address"));
				if (myObject != null) {
					response = myObject.toString();
					exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
					exchange.sendResponseHeaders(200, response.getBytes().length);
				} else {
					JSONObject errorObject = new JSONObject();
					errorObject.put("error", "No records found.");
					response = errorObject.toString();
					exchange.sendResponseHeaders(200, response.getBytes().length);
				}
			} else {
				exchange.sendResponseHeaders(405, response.getBytes().length);
			}

			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Could not handle request");
		}
	}

	public static void handleStreetRequest(HttpExchange exchange) {
		String response = "";
		try {
			if (exchange.getRequestMethod().equals(GET)) {
				Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
				Queries q = new Queries();
				JSONObject myObject = q.queryStreet(params.get("street"));
				if (myObject != null) {
					response = myObject.toString();
					exchange.sendResponseHeaders(200, response.getBytes().length);
				} else {
					JSONObject errorObject = new JSONObject();
					errorObject.put("error", "No records found.");
					response = errorObject.toString();
					exchange.sendResponseHeaders(200, response.getBytes().length);
				}
			}

			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Could not handle request");
		}
	}

	public static Map<String, String> queryToMap(String query) {
		Map<String, String> result = new HashMap<>();
		for (String param : query.split("&")) {
			String[] entry = param.split("=");
			if (entry.length > 1) {
				result.put(entry[0], entry[1]);
			} else {
				result.put(entry[0], "");
			}
		}
		return result;
	}

}
