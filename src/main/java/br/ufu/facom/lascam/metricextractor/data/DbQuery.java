/**
 * 
 */
package br.ufu.facom.lascam.metricextractor.data;

/**
 * @author klerisson
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class DbQuery {

	public static LinkedHashMap<Integer, String> fetchCommitsByProject(String project) throws SQLException {

		String query = "select tr_build_id, git_trigger_commit from " + Config.instance.travistable
				+ " where gh_project_name like ? and git_branch like 'master'"
				+ " group by tr_build_id, git_trigger_commit order by tr_build_id";
		
		try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, project);
			LinkedHashMap<Integer, String> commits = new LinkedHashMap<>();
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				commits.put(rs.getInt("tr_build_id"), rs.getString("git_trigger_commit"));
			}
			return commits;
		} catch (Exception e) {
			throw e;
		}
	}
}