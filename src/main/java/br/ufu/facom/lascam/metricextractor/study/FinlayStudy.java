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

	/* (non-Javadoc)
	 * @see org.repodriller.Study#execute()
	 */
	@Override
	public void execute() {
		new RepositoryMining()
		.in(GitRepository.singleProject("/home/klerisson/Documents/PhD/research_artifacts/Streaming_builds/experiment/storm"))
		.through(Commits.list(Arrays.asList("")))
		.process(new RevisionVisitor(), null)
		.mine();

	}

}
