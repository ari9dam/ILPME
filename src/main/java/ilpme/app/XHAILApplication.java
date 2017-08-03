package ilpme.app;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import ilpme.core.ILPMEConfig;
import ilpme.entities.ModeDeclarations;
import ilpme.entities.PartialHypotheis;
import ilpme.entities.Sample;
import ilpme.utils.PartialHypothesisUtils;
import ilpme.utils.SatisfiabilityUtils;
import ilpme.xhail.core.Config;
import ilpme.xhail.core.entities.Answer;
import ilpme.xhail.core.entities.Answers;
import ilpme.xhail.core.entities.Problem;
import ilpme.xhail.core.entities.Values;
import ilpme.xhail.core.parser.Parser;
import ilpme.xhail.core.statements.Example;
import ilpme.xhail.core.terms.Atom;
import ilpme.xhail.core.terms.Clause;


public class XHAILApplication {
	public XHAILApplication(SatisfiabilityUtils asp) {
		this.phUtil = new PartialHypothesisUtils();
		this.aspUtil = asp;
	}

	private PartialHypothesisUtils phUtil = null;
	private SatisfiabilityUtils aspUtil = null;
	
	public Set<PartialHypotheis> solve(ILPMEConfig config, Sample sample, List<String> bk, 
			ModeDeclarations modeDeclarations, int numberOfSamples){
		/***
		 * create xhail config from ILPMEConfig
		 * call xhail appliation
		 * convert xhail output to PartialHypotheis
		 */

		Set<PartialHypotheis> sol = new HashSet<PartialHypotheis>();

		Config xhailConfig = fromILPMEToXhailConfig(config);

		Problem.Builder problem = new Problem.Builder(xhailConfig); 
		problem.addBackground(bk);
		problem.addBackground(sample.getStory());
		for(Example example:sample.getExamples())
			problem.addExample(example);
		problem.setModeDeclarations(modeDeclarations);

		Problem xhailProblem = problem.build();
		Answers result = xhailProblem.solve();
		for(Answer answer: result.getAnswers()){
			if(answer.hasGeneralisation()){
				Clause[] generalization = answer.getHypothesis().getGeneralisation();
				Atom[] literals = answer.getHypothesis().getLiterals();
				PartialHypotheis ph = new PartialHypotheis(generalization, literals, numberOfSamples);
				ph.setIndex(0);
				ph.setCoverage(1);
				sol.add(ph);
			}
		}
		return sol;
	}

	public Set<PartialHypotheis> findGeneralizations(ILPMEConfig config, Sample sample, 
			ModeDeclarations modeDeclarations, List<String> bk){
		Set<PartialHypotheis> sol = new HashSet<PartialHypotheis>();

		Config xhailConfig = fromILPMEToXhailConfig(config);
		Problem.Builder problem = new Problem.Builder(xhailConfig); 
		problem.addBackground(bk);
		problem.addBackground(sample.getStory());
		for(Example example:sample.getExamples())
			problem.addExample(example);

		problem.setModeDeclarations(modeDeclarations);
		
		Problem xhailProblem = problem.build();
		Set<Collection<Clause>> generalisations =  xhailProblem.findGeneralizations();
		
		for(Collection<Clause> lp: generalisations){
			PartialHypotheis ph = new PartialHypotheis(lp);
			sol.add(ph);
		}
		
		return sol;
	}
	
	public Set<PartialHypotheis> findInductions(ILPMEConfig config, Sample sample, List<String> bk, 
			PartialHypotheis ph){
		Set<PartialHypotheis> sol = new HashSet<PartialHypotheis>();
		/***
		 * list background
		 * list story
		 * list examples
		 * list clauses
		 */
		
		List<String> clauses = phUtil.asClauses(ph.getGeneralization().getRules(), ph.getLiterals(),
				config.getMaxLength());
		clauses.addAll(bk);
		clauses.add(sample.toASPString());
		
		/**
		 * solve and get the literals
		 */
		
		Entry<Values, Collection<Collection<String>>> result = 
				this.aspUtil.execute(StringUtils.join(clauses, "\n"), config);
		
		/**
		 * create hypothesis
		 */
		for(Collection<String> entry: result.getValue()){
			Set<Atom> selected = parseAllSelected(entry);
			PartialHypotheis p  = new PartialHypotheis(ph.getGeneralization(),
					selected, ph.getPoolSize());
			p.setCoverage(ph.getCoverage()+1);
			p.setIndex(ph.getIndex()+1);
			p.setSamples(ph.getSamples());
			sol.add(p);
		}
		
		return sol;
	}

	private Set<Atom> parseAllSelected(Collection<String> entry) {
		Set<Atom> result = new HashSet<Atom>();
		for(String src: entry){
			Atom atom = Parser.parseToken(src);
			if(atom!=null && atom.getIdentifier().equals("use_clause_literal") && 2 == atom.getArity())
				result.add(atom);
		}
		return result;
	}

	private Config fromILPMEToXhailConfig(ILPMEConfig config){
		Config.Builder builder = new Config.Builder();
		builder.setAll(config.isAll());
		builder.setBlind(config.isBlind());
		builder.setClasp(config.getClasp());
		builder.setDebug(config.isDebug());
		builder.setFull(config.isFull());
		builder.setIterations(config.getIterations());
		builder.setKill(config.getKill());
		builder.setGringo(config.getGringo());
		Config xhailConfig = builder.build();
		return xhailConfig;
	}
}
