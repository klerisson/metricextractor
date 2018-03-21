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

import br.ufu.facom.lascam.metricextractor.data.DbQuery;

/**
 * @author klerisson
 *
 */
public class RevisionVisitor implements CommitVisitor {

	private Integer buildId;
	private String projectName;

	public RevisionVisitor(String projectName, Integer buildId) {
		this.buildId = buildId;
		this.projectName = projectName;
	}

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writter) {
		try {
			repo.getScm().checkout(commit.getHash());
			CKReport report = new CK().calculate(repo.getPath());
			CKNumber finalMetric = new CKNumber(null, null, null, null);
			for (CKNumber ck : report.all()) {
				sum(ck, finalMetric);
			}
			DbQuery.insertOrUpdateMetric(projectName, buildId, finalMetric);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			repo.getScm().reset();
		}
	}

	private void sum(CKNumber ck, CKNumber fm) {
		fm.setAbstractness(fm.getAbstractness() + ck.getAbstractness());
		fm.setAfferentCoupling(fm.getAfferentCoupling() + ck.getAfferentCoupling());
		fm.setAverageAttributesClass(fm.getAverageAttributesClass() + ck.getAverageAttributesClass());
		fm.setAverageBlockDepth(fm.getAverageBlockDepth() + ck.getAverageBlockDepth());
		fm.setAverageComments(fm.getAverageComments() + ck.getAverageComments());
		fm.setAverageConstructorsClass(fm.getAverageConstructorsClass() + ck.getAverageConstructorsClass());
		fm.setAverageLOCMethod(fm.getAverageLOCMethod() + ck.getAverageLOCMethod());
		fm.setAverageNOM(fm.getAverageNOM() + ck.getAverageNOM());
		fm.setAverageNParameter(fm.getAverageNParameter() + ck.getAverageNParameter());
		fm.setCommentsRatio(fm.getCommentsRatio() + ck.getCommentsRatio());
		fm.setDifficultLevel(fm.getDifficultLevel() + ck.getDifficultLevel());
		fm.setDit(fm.getDit() + ck.getDit());
		fm.setEfferentCoupling(fm.getEfferentCoupling() + ck.getEfferentCoupling());
		fm.setEffort(fm.getEffort() + ck.getEffort());
		fm.setInstability(fm.getInstability() + ck.getInstability());
		fm.setLcom1(fm.getLcom1() + ck.getLcom1());
		fm.setLcom2(fm.getLcom2() + ck.getLcom2());
		fm.setLcom3(fm.getLcom3() + ck.getLcom3());
		fm.setLoc(fm.getLoc() + ck.getLoc());
		fm.setMaintainability(fm.getMaintainability() + ck.getMaintainability());
		fm.setAverageWMC(fm.getAverageWMC() + ck.getAverageWMC());
		fm.setNComments(fm.getNComments() + ck.getNComments());
		fm.setNDeliveredBugs(fm.getNDeliveredBugs() + ck.getNDeliveredBugs());
		fm.setNImport(fm.getNImport() + ck.getNImport());
		fm.setNoConstructors(fm.getNoConstructors() + ck.getNoConstructors());
		fm.setNof(fm.getNof() + ck.getNof());
		fm.setNOInteface(fm.getNOInteface() + ck.getNOInteface());
		fm.setNoLines(fm.getNoLines() + ck.getNoLines());
		fm.setNom(fm.getNom() + ck.getNom());
		fm.setNOperands(fm.getNOperands() + ck.getNOperands());
		fm.setNOperators(fm.getNOperators() + ck.getNOperators());
		fm.setNormalizedDistance(fm.getNormalizedDistance() + ck.getNormalizedDistance());
		fm.setNOTypesPackage(fm.getNOTypesPackage() + ck.getNOTypesPackage());
		fm.setNParameter(fm.getNParameter() + ck.getNParameter());
		fm.setNUniqueOperands(fm.getNUniqueOperands() + ck.getNUniqueOperands());
		fm.setNUniqueOperators(fm.getNUniqueOperators() + ck.getNUniqueOperators());
		fm.setProgramLength(fm.getProgramLength() + ck.getProgramLength());
		fm.setProgramLevel(fm.getProgramLevel() + ck.getProgramLevel());
		fm.setProgramVocabulary(fm.getProgramVocabulary() + ck.getProgramVocabulary());
		fm.setProgramVolume(fm.getProgramVolume() + ck.getProgramVolume());
		fm.setTimeToImplement(fm.getTimeToImplement() + ck.getTimeToImplement());
		fm.setWMC(fm.getWMC() + ck.getWMC());
	}

	@Override
	public String name() {
		return "RevisionVisitor";
	}

}
