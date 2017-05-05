package ilpme.app;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import ilpme.entities.ILPMEProblem;
import ilpme.entities.PartialHypotheis;
import ilpme.entities.Sample;

public class TopDownIterativeSolver implements IIterativeSolver {

	@Override
	public Set<PartialHypotheis> refine(ILPMEProblem problem, PartialHypotheis top) {
		Set<PartialHypotheis> answers = new HashSet<PartialHypotheis>();
		Sample last = problem.getTrainingData().get(top.getIndex());
		
		/**
		 * If top explains last
		 * increase index of top
		 * return top
		 */	
		
		/***
		 * Get upper bounds from XHAIL 
		 */
		
		/**
		 * create candidates for refinements
		 */
		Queue<PartialHypotheis> queue = new LinkedList<PartialHypotheis>();
		
		while(!queue.isEmpty()){
			PartialHypotheis ph = queue.poll();
			boolean flag = true;
			for(int j=0;j<=top.getIndex();j++){
				
			}
			
			if(flag){
				answers.add(ph);
			}
		}
		return answers;
	}
}
