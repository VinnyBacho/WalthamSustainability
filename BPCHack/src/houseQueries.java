import java.sql.*;
	
public class houseQueries {
	
	static Connection conn = null;
	
	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/test";
		String username = "root";
		String password = "";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("Error: " + e.getMessage());
			return;
		}
		
		try {
			conn = DriverManager.getConnection(url, username, password);
			queryHouses();
			
		}catch(SQLException e) {
			e.printStackTrace();
			}

	}
	public static void queryHouses() {
		int count = 0, sumPower = 0, sumGallons = 0;
		try {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM HOUSES WHERE STREET='VILLA STREET'");
		while(rs.next()) {
			int power = rs.getInt("kilowattsPerHourPerMonth");
			int gallons = rs.getInt("gallonsPerDayPerHouse");
			count++;
			sumPower += power;
			sumGallons += gallons;
		}
		System.out.println("average Power " + sumPower/count);
		System.out.println("average Gallons " + sumGallons/count);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}