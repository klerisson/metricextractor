/**
 * 
 */
package br.ufu.facom.lascam.metricextractor;

import org.repodriller.RepoDriller;

import br.ufu.facom.lascam.metricextractor.study.FinlayStudy;

/**
 * @author klerisson
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new RepoDriller().start(new FinlayStudy());
	}

}
