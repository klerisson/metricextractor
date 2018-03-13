package br.ufu.facom.lascam.metricextractor.data;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class Config {

	public static final Config instance = new Config();
	public Map<String, String> projectToPath = new HashMap<>();

	public String dbuser;
	public String dbpassword;
	public String database;
	public String travistable;

	private Config() {
		// Loading configurations properties
		Properties prop = new Properties();
		try (InputStream input = new FileInputStream("config.properties")) {
			// load a properties file
			prop.load(input);
			this.database = prop.getProperty("database");
			this.dbuser = prop.getProperty("dbuser");
			this.dbpassword = prop.getProperty("dbpassword");
			this.travistable = prop.getProperty("travistable");

			loadGitRepos();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Fatal Error! Configuration missing.");
		}
	}

	private void loadGitRepos() throws Exception {
		Properties prop = new Properties();
		try (InputStream input = new FileInputStream("gitclonedrepos.properties")) {
			prop.load(input);
			for (Entry<Object, Object> entry : prop.entrySet()) {
			    this.projectToPath.put((String) entry.getKey(), (String) entry.getValue());
			}
		}
	}
}
