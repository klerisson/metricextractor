/**
 * 
 */
package br.ufu.facom.lascam.metricextractor.visitor;

import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.CKReport;

/**
 * @author klerisson
 *
 */
public class RevisionVisitor implements CommitVisitor {

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writter) {
		try {
			repo.getScm().checkout(commit.getHash());
			CKReport report = new CK().calculate(repo.getPath());
			for(CKNumber ck : report.all()) {
				System.out.println(ck);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			repo.getScm().reset();
		}
	}
	
	@Override
	public String name() {
		return "RevisionVisitor";
	}

}
