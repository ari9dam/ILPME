package ilpme.app;

import java.util.Set;

import ilpme.entities.ILPMEProblem;
import ilpme.entities.LogicProgram;
import ilpme.entities.PartialHypotheis;
import ilpme.utils.IPartialHypothesisPool;

public class IncrementalIterativeLearning {
	
	private IPartialHypothesisPool pool;
	private IIterativeSolver itertaiveSolver;
	
	public LogicProgram learn(ILPMEProblem problem){
		
		int numberOfSamples = problem.trainingData.size();
		PartialHypotheis best = null;
		
		while(this.pool.hasNext()){
			PartialHypotheis ph = pool.getNext();
			
			/**
			 * Solves a solution to P(E_1,...E_i+1)
			 * using a solution of P(E_1,...E_i)
			 */
			Set<PartialHypotheis> refinements = this.itertaiveSolver.refine(problem, ph);
			
			/**
			 * Saves a copy of the best solution.
			 * Best in terms of how many examples it explains.
			 */
			if(!refinements.isEmpty()&& best.getIndex()<=ph.getIndex() ){
				for(PartialHypotheis h:refinements){
					best = h;
					break;
				}
			}
			
			/**
			 * Found a solution to the entire problem
			 */
			if((ph.getIndex()+1==numberOfSamples) && !refinements.isEmpty()){
				System.out.println("Found a solution!");
				return best.getTop();
			}
		}
		
		/**
		 * Return the best incomplete solution
		 */
		System.out.println("No complete soluton found!");
		return best.getTop();
		
	}
}
