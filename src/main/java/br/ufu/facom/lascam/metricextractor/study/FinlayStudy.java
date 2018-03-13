/**
 * 
 */
package br.ufu.facom.lascam.metricextractor.study;

import java.util.Arrays;

import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.scm.GitRepository;

import br.ufu.facom.lascam.metricextractor.visitor.RevisionVisitor;

/**
 * @author klerisson
 *
 */
public class FinlayStudy implements Study {

	@Override
	public void execute() {
		new RepositoryMining()
		.in(GitRepository.singleProject("/home/klerisson/Documents/PhD/research_artifacts/Streaming_builds/experiment/storm"))
		.through(Commits.list(Arrays.asList("4e0ff2f6e238a59c13d9af6dc3db84ae5817365f")))
		.process(new RevisionVisitor(), null)
		.mine();

	}

}
