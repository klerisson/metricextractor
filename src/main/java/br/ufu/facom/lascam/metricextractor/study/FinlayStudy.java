/**
 * 
 */
package br.ufu.facom.lascam.metricextractor.study;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.scm.GitRepository;

import br.ufu.facom.lascam.metricextractor.data.Config;
import br.ufu.facom.lascam.metricextractor.data.DbQuery;
import br.ufu.facom.lascam.metricextractor.visitor.RevisionVisitor;

/**
 * @author klerisson
 *
 */
public class FinlayStudy implements Study {

	@Override
	public void execute() {

		for (Entry<String, String> entry : Config.instance.projectToPath.entrySet()) {
			
			try {
				LinkedHashMap<Integer, String> buildsToCommitHash = DbQuery.fetchCommitsByProject(entry.getKey());
				
				for (Entry<Integer, String> builds : buildsToCommitHash.entrySet()) {
					
					new RepositoryMining().in(GitRepository.singleProject(entry.getValue()))
							.through(Commits.list(Arrays.asList(builds.getValue())))
							.process(new RevisionVisitor()).mine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
