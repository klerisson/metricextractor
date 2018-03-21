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

import com.github.mauricioaniche.ck.CKNumber;

public class DbQuery {

	public static LinkedHashMap<Integer, String> fetchCommitsByProject(String project) throws SQLException {

		String query = "select tr_build_id, git_trigger_commit from " + Config.instance.travistable
				+ " where gh_project_name like ? and git_branch like 'master' and tr_status in ('failed','passed')"
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

	public static void insertOrUpdateMetric(String project, Integer buildId, CKNumber fm) throws Exception {
		
		String status = "";
		String query = "select tr_status from " + Config.instance.travistable
				+ " where gh_project_name = ? and tr_build_id = ?;";
		
		String sql = "INSERT INTO travistorrent.metrics (tr_build_id, average_block_depth ,	weighted_methods_per_class, maintainability_index, "
				+ "cyclomatic_complexity,	number_of_operands,	number_of_operators, number_of_unique_operands, number_of_unique_operators, depth_of_inheritance, "
				+ "number_of_attributes, average_number_of_attributes_per_class, average_number_of_constructors_per_class, abstractness, afferent_coupling, "
				+ "efferent_coupling,	instability, normalized_distance, difficulty_level,	effort_to_implement, time_to_implement,	program_length,	program_level, "
				+ "average_lines_of_code_per_method, average_number_of_methods, average_number_of_parameters,	number_of_types_per_package, comment_code_ratio, "
				+ "number_of_delivered_bugs, average_number_of_comments, lack_of_cohesion_1, lack_of_cohesion_2, lack_of_cohesion_3, program_vocabulary_size, "
				+ "number_of_constructors, program_volume, number_of_import_statements, number_of_interfaces, lines_of_code, number_of_comments, number_of_methods, "
				+ "number_of_parameters, number_of_lines, gh_project_name, status) "
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) "
				+ "ON DUPLICATE KEY UPDATE average_block_depth = ?, weighted_methods_per_class = ?, maintainability_index = ?, "
				+ "cyclomatic_complexity = ?,	number_of_operands = ?,	number_of_operators = ?, number_of_unique_operands = ?, number_of_unique_operators = ?, depth_of_inheritance = ?, "
				+ "number_of_attributes = ?, average_number_of_attributes_per_class = ?, average_number_of_constructors_per_class = ?, abstractness = ?, afferent_coupling = ?, "
				+ "efferent_coupling = ?,	instability = ?, normalized_distance = ?, difficulty_level = ?,	effort_to_implement = ?, time_to_implement = ?,	program_length = ?,	program_level = ?, "
				+ "average_lines_of_code_per_method = ?, average_number_of_methods = ?, average_number_of_parameters = ?, number_of_types_per_package = ?, comment_code_ratio = ?, "
				+ "number_of_delivered_bugs = ?, average_number_of_comments = ?, lack_of_cohesion_1 = ?, lack_of_cohesion_2 = ?, lack_of_cohesion_3 = ?, program_vocabulary_size = ?, "
				+ "number_of_constructors = ?, program_volume = ?, number_of_import_statements = ?, number_of_interfaces = ?, lines_of_code = ?, number_of_comments = ?, number_of_methods = ?, "
				+ "number_of_parameters = ?, number_of_lines = ?, gh_project_name = ?, status = ?;";

		try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt2 = conn.prepareStatement(query); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			//Fetch status
			pstmt2.setString(1, project);
			pstmt2.setInt(2, buildId);
			ResultSet rs = pstmt2.executeQuery();
			if (rs.next()) {
				status = rs.getString("tr_status");
			} else {
				throw new Exception("Status not found!");
			}
			
			pstmt.setInt(1, buildId);
			pstmt.setDouble(2, fm.getAverageBlockDepth());
			pstmt.setDouble(3, fm.getWMC());
			pstmt.setDouble(4, fm.getMaintainability());
			pstmt.setDouble(5, fm.getAverageWMC());
			pstmt.setInt(6, fm.getNOperands());
			pstmt.setInt(7, fm.getNOperators());
			pstmt.setInt(8, fm.getNUniqueOperands());
			pstmt.setInt(9, fm.getNUniqueOperators());
			pstmt.setInt(10, fm.getDit());
			pstmt.setInt(11, fm.getNof());
			pstmt.setDouble(12, fm.getAverageAttributesClass());
			pstmt.setDouble(13, fm.getAverageConstructorsClass());
			pstmt.setDouble(14, fm.getAbstractness());
			pstmt.setInt(15, fm.getAfferentCoupling());
			pstmt.setInt(16, fm.getEfferentCoupling());
			pstmt.setDouble(17, fm.getInstability());
			pstmt.setDouble(18, fm.getNormalizedDistance());
			pstmt.setDouble(19, fm.getDifficultLevel());
			pstmt.setDouble(20, fm.getEffort());
			pstmt.setDouble(21, fm.getTimeToImplement());
			pstmt.setInt(22, fm.getProgramLength());
			pstmt.setDouble(23, fm.getProgramLevel());
			pstmt.setDouble(24, fm.getAverageLOCMethod());
			pstmt.setDouble(25, fm.getAverageNOM());
			pstmt.setDouble(26, fm.getAverageNParameter());
			pstmt.setInt(27, fm.getNOTypesPackage());
			pstmt.setDouble(28, fm.getCommentsRatio());
			pstmt.setDouble(29, fm.getNDeliveredBugs());
			pstmt.setDouble(30, fm.getAverageComments());
			pstmt.setDouble(31, fm.getLcom1());
			pstmt.setDouble(32, fm.getLcom2());
			pstmt.setDouble(33, fm.getLcom3());
			pstmt.setInt(34, fm.getProgramVocabulary());
			pstmt.setInt(35, fm.getNoConstructors());
			pstmt.setDouble(36, fm.getProgramVolume());
			pstmt.setInt(37, fm.getNImport());
			pstmt.setInt(38, fm.getNOInteface());
			pstmt.setInt(39, fm.getLoc());
			pstmt.setInt(40, fm.getNComments());
			pstmt.setInt(41, fm.getNom());
			pstmt.setInt(42, fm.getNParameter());
			pstmt.setInt(43, fm.getNoLines());
			pstmt.setString(44, project);
			pstmt.setString(45, status);

			pstmt.setDouble(46, fm.getAverageBlockDepth());
			pstmt.setDouble(47, fm.getWMC());
			pstmt.setDouble(48, fm.getMaintainability());
			pstmt.setDouble(49, fm.getAverageWMC());
			pstmt.setInt(50, fm.getNOperands());
			pstmt.setInt(51, fm.getNOperators());
			pstmt.setInt(52, fm.getNUniqueOperands());
			pstmt.setInt(53, fm.getNUniqueOperators());
			pstmt.setInt(54, fm.getDit());
			pstmt.setInt(55, fm.getNof());
			pstmt.setDouble(56, fm.getAverageAttributesClass());
			pstmt.setDouble(57, fm.getAverageConstructorsClass());
			pstmt.setDouble(58, fm.getAbstractness());
			pstmt.setInt(59, fm.getAfferentCoupling());
			pstmt.setInt(60, fm.getEfferentCoupling());
			pstmt.setDouble(61, fm.getInstability());
			pstmt.setDouble(62, fm.getNormalizedDistance());
			pstmt.setDouble(63, fm.getDifficultLevel());
			pstmt.setDouble(64, fm.getEffort());
			pstmt.setDouble(65, fm.getTimeToImplement());
			pstmt.setInt(66, fm.getProgramLength());
			pstmt.setDouble(67, fm.getProgramLevel());
			pstmt.setDouble(68, fm.getAverageLOCMethod());
			pstmt.setDouble(69, fm.getAverageNOM());
			pstmt.setDouble(70, fm.getAverageNParameter());
			pstmt.setInt(71, fm.getNOTypesPackage());
			pstmt.setDouble(72, fm.getCommentsRatio());
			pstmt.setDouble(73, fm.getNDeliveredBugs());
			pstmt.setDouble(74, fm.getAverageComments());
			pstmt.setDouble(75, fm.getLcom1());
			pstmt.setDouble(76, fm.getLcom2());
			pstmt.setDouble(77, fm.getLcom3());
			pstmt.setInt(78, fm.getProgramVocabulary());
			pstmt.setInt(79, fm.getNoConstructors());
			pstmt.setDouble(80, fm.getProgramVolume());
			pstmt.setInt(81, fm.getNImport());
			pstmt.setInt(82, fm.getNOInteface());
			pstmt.setInt(83, fm.getLoc());
			pstmt.setInt(84, fm.getNComments());
			pstmt.setInt(85, fm.getNom());
			pstmt.setInt(86, fm.getNParameter());
			pstmt.setInt(87, fm.getNoLines());
			pstmt.setString(88, project);
			pstmt.setString(89, status);
			
			pstmt.executeUpdate();

		} catch (Exception e) {
			throw e;
		}
	}
}