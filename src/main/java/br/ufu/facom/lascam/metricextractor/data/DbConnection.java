package br.ufu.facom.lascam.metricextractor.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DbConnection {

	public static Connection getConnection() throws SQLException {
		try {
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/" + Config.instance.database + "?user=" + Config.instance.dbuser
							+ "&password=" + Config.instance.dbpassword + "&useSSL=false" + "&serverTimezone=UTC");
			return conn;
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			throw ex;
		}
	}

}
