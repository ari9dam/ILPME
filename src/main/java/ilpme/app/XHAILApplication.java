package ilpme.app;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ilpme.core.ILPMEConfig;
import ilpme.entities.LogicProgram;
import ilpme.entities.PartialHypotheis;
import ilpme.entities.Sample;
import ilpme.xhail.core.Config;
import ilpme.xhail.core.entities.Answer;
import ilpme.xhail.core.entities.Answers;
import ilpme.xhail.core.entities.Problem;
import ilpme.xhail.core.statements.Example;
import ilpme.xhail.core.terms.Atom;
import ilpme.xhail.core.terms.Clause;


public class XHAILApplication {

	public Set<PartialHypotheis> solve(ILPMEConfig config, Sample sample, List<String> bk){
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


		Problem xhailProblem = problem.build();
		Answers result = xhailProblem.solve();
		for(Answer answer: result.getAnswers()){
			if(answer.hasGeneralisation()){
				Clause[] generalization = answer.getHypothesis().getGeneralisation();
				Atom[] literals = answer.getHypothesis().getLiterals();
				PartialHypotheis ph = new PartialHypotheis(generalization, literals);
				sol.add(ph);
			}
		}
		return sol;
	}

	public Set<PartialHypotheis> findGeneralizations(ILPMEConfig config, Sample sample, List<String> bk){
		Set<PartialHypotheis> sol = new HashSet<PartialHypotheis>();

		Config xhailConfig = fromILPMEToXhailConfig(config);
		Problem.Builder problem = new Problem.Builder(xhailConfig); 
		problem.addBackground(bk);
		problem.addBackground(sample.getStory());
		for(Example example:sample.getExamples())
			problem.addExample(example);


		Problem xhailProblem = problem.build();
		Answers result = xhailProblem.solve();
		
		return sol;
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
