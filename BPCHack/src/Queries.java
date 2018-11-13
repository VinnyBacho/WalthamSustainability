import org.json.*;
import java.sql.*;

public class Queries {
	static Connection con = null;
	final String URL = "jdbc:mysql://localhost:3306/bpchack";
	final String Username = "root"; //
	final String Password = "password";

	public Queries() {

	}

	public void SetConnect() {
		try { // load driver into VM memory
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Failed to load MySQL driver.");
			return;
		}
		try {// create connection and statement objects
			con = DriverManager.getConnection(URL, Username, Password);
		} catch (SQLException e) {
			System.out.println("Failed to get connection");
			e.printStackTrace();
		}
	}

	public JSONObject QueryFirst() {
		JSONObject j = new JSONObject();
		try {
			this.SetConnect();
			Statement stmt = con.createStatement();
			stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("SELECT * FROM houses ORDER BY kilowattsPerHourPerMonth ASC");

			JSONArray Adresses = new JSONArray();
			JSONArray KWHMs = new JSONArray();
			JSONArray GDHs = new JSONArray();
			JSONArray Streets = new JSONArray();
			String ADDress;
			String Street;
			int KWHM = 0;
			int GDH = 0;
			while (result.next()) {
				ADDress = result.getString("Address");
				Street = result.getString("Street");
				KWHM = result.getInt("kilowattsPerHourPerMonth");
				GDH = result.getInt("gallonsPerDayPerHouse");
				Adresses.put(ADDress);
				Streets.put(Street);
				KWHMs.put(KWHM);
				GDHs.put(GDH);
			}

			j.put("Address list", Adresses);
			j.put("Street List", Streets);
			j.put("kilowattsPerHourPerMonth List", KWHMs);
			j.put("gallonsPerDayPerHouse", GDHs);

			System.out.println(j.toString());

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return j;
	}

	public JSONObject queryAddress(String addr) {
		this.SetConnect();
		JSONObject j = new JSONObject();
		int power = 0;
		int gallons = 0;
		try {
			Statement stmt = con.createStatement();
			String q = "SELECT * FROM HOUSES WHERE ADDRESS='{ADDRESS}'";
			q = q.replace("{ADDRESS}", addr);
			System.out.println(q);
			ResultSet rs = stmt.executeQuery(q);
			rs.next();
			power = rs.getInt("kilowattsPerHourPerMonth");
			gallons = rs.getInt("gallonsPerDayPerHouse");

			try {
				j.put("kilowattsPerHourPerMonth", power);
				j.put("gallonsPerDayPerHouse", gallons);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (power == 0 || gallons == 0) {
				j = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return j;
	}

	public JSONObject queryStreet(String street) {
		this.SetConnect();
		JSONObject j = new JSONObject();
		int kwSum = 0, gallonsSum = 0, counter = 0;
		try {
			Statement stmt = con.createStatement();
			String q = "SELECT * FROM HOUSES WHERE STREET='{STREET}'";
			q = q.replace("{STREET}", street);
			System.out.println(q);
			ResultSet rs = stmt.executeQuery(q);

			while (rs.next()) {
				kwSum += rs.getInt("kilowattsPerHourPerMonth");
				counter++;
				gallonsSum += rs.getInt("gallonsPerDayPerHouse");

			}

			if (counter != 0) {
				j.put("Sum Electric", kwSum);
				j.put("Sum Water", gallonsSum);
				j.put("numHouses", counter);
			} else {
				j = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return j;
	}
}
