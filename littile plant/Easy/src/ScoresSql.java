import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JTextField;

public class ScoresSql {
	int score = GameFrame.getScore();
	String All1 = "<html>";
	String All2 = "<html>";

	public String getAll2() {
		return All2;
	}

	public void setAll2(String All2) {
		All2 = All2;
	}

	public String getAll() {
		return All1;
	}

	public void setAll(String All1) {
		this.All1 = All1;
	}

	public ScoresSql(String name) throws Exception
	// public static void main(String[] args) throws Exception
	{
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/G11?" + "user=root&password=0000&serverTimezone=UTC&useSSL=false");

			// Do something with the Connection
			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO scores VALUES ('" + score + "' , '" + name + "' )";
			stmt.executeUpdate(sql);
			ResultSet rs = stmt.executeQuery("SELECT * FROM g11.scores order by score DESC;");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + "\t" + rs.getString(2));
			}

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public ScoresSql() throws Exception {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/G11?" + "user=root&password=0000&serverTimezone=UTC&useSSL=false");

			// Do something with the Connection
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM g11.scores order by score DESC;");
			int count = 0;
			while (rs.next() && count < 10) {
				String line = String.valueOf(rs.getInt(1));
				String line2 = rs.getString(2);
				All1 += "<br>" + line + "<br>";
				All2 += "<br>" + line2 + "<br>";
				// System.out.println(line);
				count++;
			}

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

}