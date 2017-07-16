package ilpme.app;

import java.util.Set;

import ilpme.entities.ILPMEProblem;
import ilpme.entities.LogicProgram;
import ilpme.entities.PartialHypotheis;
import ilpme.utils.IPartialHypothesisPool;

public class IncrementalIterativeLearning {
	
	private IPartialHypothesisPool pool;
	private IIterativeSolver itertaiveSolver;
	private XHAILApplication xhail ;
	
	

	public IncrementalIterativeLearning(IPartialHypothesisPool pool, IIterativeSolver itertaiveSolver) {
		super();
		this.pool = pool;
		this.itertaiveSolver = itertaiveSolver;
		this.xhail = new XHAILApplication();
	}



	public LogicProgram learn(ILPMEProblem problem){
		
		int numberOfSamples = problem.getTrainingData().size();
		PartialHypotheis best = null;
		
		/****
		 * initialize pool
		 * Pass Sample 1 to XHAIL to get all the answers
		 */
		Set<PartialHypotheis> seeds = this.xhail.solve(problem.getConfig(), problem.getTrainingData().get(0), 
				problem.getBackground());
		
		/***
		 * search
		 */
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
			if(!refinements.isEmpty()&& (best==null||best.getIndex()<=ph.getIndex())){
				for(PartialHypotheis h:refinements){
					best = h;
					break;
				}
			}
			
			/***
			 * add the refinements to the queue
			 */
			pool.addAll(refinements);
			
			/**
			 * Found a solution to the entire problem
			 */
			if((ph.getIndex()+1==numberOfSamples) && !refinements.isEmpty()){
				System.out.println("Found a solution!");
				return best.getHypothesis();
			}

		}
		
		/**
		 * Return the best incomplete solution
		 */
		System.out.println("No complete soluton found!");
		if(best!=null){
			System.out.printf("Returning the best solution that solves %d examples!\n", best.getIndex());
		}
		return best.getHypothesis();		
	}
}
