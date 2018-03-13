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
		int projectsCounter = Config.instance.projectToPath.entrySet().size();
		for (Entry<String, String> entry : Config.instance.projectToPath.entrySet()) {
			
			try {
				LinkedHashMap<Integer, String> buildsToCommitHash = DbQuery.fetchCommitsByProject(entry.getKey());
				int buildsCounter = buildsToCommitHash.entrySet().size();
				for (Entry<Integer, String> builds : buildsToCommitHash.entrySet()) {
					
					new RepositoryMining().in(GitRepository.singleProject(entry.getValue()))
							.through(Commits.list(Arrays.asList(builds.getValue())))
							.process(new RevisionVisitor(entry.getKey(), builds.getKey())).mine();
					
					System.out.println("Missing projects: " +  (--projectsCounter) + " builds: " + (--buildsCounter));
				}
			} catch (Exception e) {
				e.printStackTrace();
				--projectsCounter;
			}
		}
	}

}
