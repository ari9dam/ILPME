package ilpme.app;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

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
		 */
		Set<PartialHypotheis> generalizations = this.xhail.findGeneralizations(problem.getConfig(), 
				last, problem.getModeDeclarations() , problem.getBackground());
		
		/**
		 * create candidates for refinements
		 */
		Queue<PartialHypotheis> queue = new LinkedList<PartialHypotheis>();
		for(PartialHypotheis ph: generalizations ){
			queue.add(util.mergePartialHypothesis(top, ph));
		}
		
		while(!queue.isEmpty()){
			PartialHypotheis ph = queue.poll();
			boolean flag = true;
			for(int j=0;j<=top.getIndex()+1;j++){
				/**
				 * if ph solves example j 
				 * continue 
				 * else get refinements
				 */
				Sample sample = problem.getTrainingData().get(top.getSampleId(j));
				if(this.asp.doesEntail(ph, sample, problem.getBackground())){
					continue;
				}else{
					flag = false;
					queue.addAll(this.xhail.findInductions(problem.getConfig(), sample, 
							problem.getBackground(), ph));
				}
			}
			
			if(flag){
				ph.setIndex(top.getIndex()+1);
				ph.setCoverage(top.getCoverage()+1);
				answers.add(ph);
			}
		}
		return answers;
	}

}
