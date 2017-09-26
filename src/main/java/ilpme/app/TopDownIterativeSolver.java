package ilpme.app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import ilpme.entities.ILPMEProblem;
import ilpme.entities.PartialHypotheis;
import ilpme.entities.Sample;
import ilpme.utils.PartialHypothesisUtils;
import ilpme.utils.SatisfiabilityUtils;

public class TopDownIterativeSolver implements IIterativeSolver {
	private XHAILApplication xhail = null;
	private PartialHypothesisUtils util;
	private SatisfiabilityUtils asp;

	public TopDownIterativeSolver(SatisfiabilityUtils asp) {
		this.asp = asp;
		this.xhail = new XHAILApplication(asp);
		this.util = new PartialHypothesisUtils();
	}

	@Override
	public Set<PartialHypotheis> refine(ILPMEProblem problem, PartialHypotheis top) {

		Set<PartialHypotheis> answers = new HashSet<PartialHypotheis>();
		Sample last = problem.getTrainingData().get(top.getSampleId(top.getIndex()+1));

		/***
		 * Get upper bounds from XHAIL 
		 * 
		 */
		Set<PartialHypotheis> generalizations = last.getGeneralizations();
		if(generalizations==null){
			generalizations = this.xhail.findGeneralizations(problem.getConfig(), 
					last, problem.getModeDeclarations() , problem.getBackground());
			last.setGeneralizations(generalizations);
		}

		/**
		 * get upper bounds wrt current hypothesis
		 */

		boolean shouldUseHyp = false;
		if(shouldUseHyp){
			List<String> updatedBk = new LinkedList<String>();
			updatedBk.addAll(problem.getBackground());
			updatedBk.add(top.getHypothesis().toString());
			generalizations.addAll(this.xhail.findGeneralizations(problem.getConfig(), 
					last, problem.getModeDeclarations() ,updatedBk ));
		}

		System.out.println("# of Generaisations: "+ generalizations.size());
		/**
		 * create candidates for refinements
		 */
		Stack<PartialHypotheis> stack = new Stack<PartialHypotheis>();
		for(PartialHypotheis ph: generalizations ){
			stack.add(util.mergePartialHypothesis(top, ph));
		}
		
		if(generalizations.isEmpty())
			stack.add(util.mergePartialHypothesis(top, new PartialHypotheis(0)));

		int iter=0;
		int range = 10;
		while(!stack.isEmpty()&&!(answers.size()>0)){//){ /*&&!(answers.size()>0)){//iter>150||*/
			iter++;
			PartialHypotheis ph = stack.pop();
			boolean flag = true;

			for(int j=0;j<=top.getIndex()+1;j++){

				/**
				 * if ph solves example j 
				 * continue 
				 * else get refinements
				 */
				Sample sample = problem.getTrainingData().get(top.getSampleId(j));
				if(iter-range>0){
					System.out.printf("#sub iterations %d for %s, queue %d \n ",iter, 
							sample.getName(), stack.size() );
					range+=10;
				}
				if(this.asp.doesEntail(ph, sample, problem.getBackground())){
					continue;
				}else{
					flag = false;
					System.out.println("Finding inductions...");
					Set<PartialHypotheis> res = this.xhail.findInductions(problem.getConfig(), sample, 
							problem.getBackground(), ph);

					System.out.printf("Found %d inductions.", res.size());
					for(PartialHypotheis p: res)
						System.out.printf("size: %d-> %d-> %d | %d\n", top.getLiterals().size(), ph.getLiterals().size(), p.getLiterals().size() ,top.getGeneralization().length());
					if(res.size()>0)
						stack.add(new LinkedList<PartialHypotheis>(res).get(0));
					//stack.addAll(res);
				}
			}

			if(flag){
				ph.setIndex(top.getIndex()+1);
				ph.setCoverage(top.getCoverage()+1);
				answers.add(ph);
				System.out.println("#sub iterations : "+ iter +", found refinements: "+answers.size());
			}
		}
		System.out.println("#sub iterations : "+ iter +", found refinements: "+answers.size());


		return answers;
	}

}
