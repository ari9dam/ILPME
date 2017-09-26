package ilpme.app;

import java.util.Set;

import ilpme.entities.ILPMEProblem;
import ilpme.entities.LogicProgram;
import ilpme.entities.PartialHypotheis;
import ilpme.entities.Sample;
import ilpme.utils.IPartialHypothesisPool;
import ilpme.utils.SatisfiabilityUtils;

public class IncrementalIterativeLearning {

	private IPartialHypothesisPool pool;
	private IIterativeSolver itertaiveSolver;
	private XHAILApplication xhail ;
	private SatisfiabilityUtils asp;


	public IncrementalIterativeLearning(IPartialHypothesisPool pool, 
			IIterativeSolver itertaiveSolver, SatisfiabilityUtils asp) {
		super();
		this.pool = pool;
		this.itertaiveSolver = itertaiveSolver;
		this.xhail = new XHAILApplication(asp);
		this.asp = asp;

	}



	public LogicProgram learn(ILPMEProblem problem){


		int numberOfSamples = problem.getTrainingData().size();
		PartialHypotheis best = null;

		/****
		 * initialize pool
		 * Pass Sample 1 to XHAIL to get all the answers
		 */
		Set<PartialHypotheis> seeds = null;


		do{
			seeds = this.xhail.solve(problem.getConfig(), 
					problem.getTrainingData().get(0), 
					problem.getBackground(), problem.getModeDeclarations(), numberOfSamples);
			if(seeds.isEmpty()){
				Sample elem = problem.getTrainingData().remove(0);
				problem.getTrainingData().add(elem);
			}else{
				System.out.println(problem.getTrainingData().get(0).getName());
			}
		}while(seeds.isEmpty());

		System.out.printf("Found %d seeds.\n", seeds.size());

		this.pool.addAll(seeds);
		
		if(numberOfSamples==1){
			return pool.size()==0?null: pool.getNext().getGeneralization();
		}
		
		/***
		 * search
		 */
		int i=2;
		while(this.pool.hasNext()){
			System.out.println("Iteration "+ i+" :");
			i++;

			PartialHypotheis ph = pool.getNext();

			Sample last = problem.getTrainingData().get(ph.getSampleId(ph.getIndex()+1));

			if(best==null||best.getCoverage()<ph.getCoverage())
				best = ph;

			/**
			 * If top explains last
			 * increase index of top
			 * return top
			 */	
			if(this.asp.doesEntail(ph, last, problem.getBackground())){
				ph.increaseCoverage();
				ph.slideSample(ph.getIndex()+1);
				pool.add(ph);
				if(best.getCoverage()<ph.getCoverage())
					best = ph;
			}else{

				/**
				 * Solves a solution to P(E_1,...E_i+1)
				 * using a solution of P(E_1,...E_i)
				 */
				int mimo=0;
				Set<PartialHypotheis> refinements = this.itertaiveSolver.refine(problem, ph);

				/***
				 * add the refinements to the queue
				 */
				pool.addAll(refinements);

				/**
				 * Saves a copy of the best solution.
				 * Best in terms of how many examples it explains.
				 */
				if(best.getCoverage()<=ph.getCoverage()){
					for(PartialHypotheis h:refinements){
						best = h;
						System.out.println(best.getHypothesis());
						break;
					}
				}	
			}


			System.out.println("best coverage "+ best.getCoverage());
			System.out.println("current hypothesis coverage "+ ph.getCoverage());
			System.out.println("current hypothesis size "+ ph.getGeneralization().length());
			System.out.println("queue size "+ pool.size()+"\n");

			/**
			 * Found a solution to the entire problem
			 */
			if(best.getCoverage()==numberOfSamples){
				System.out.println("Found a solution!");
				//				for(int j=0;j<problem.getTrainingData().size();j++){
				//					Sample sample = problem.getTrainingData().get(j);
				//					if(this.asp.doesEntail(best, sample, problem.getBackground())){
				//						continue;
				//					}else {
				//						System.out.println("exit on bad out");
				//						System.exit(0);
				//					}
				//				}

				return best.getHypothesis();
			}
		}

		/**
		 * Return the best incomplete solution
		 */
		System.out.println("No complete soluton found!");
		if(best!=null){
			System.out.printf("Returning the best solution that solves %d examples!\n", best.getCoverage());

		}else{
			System.out.printf("Returning the best solution that solves 0 examples!\n");
			return new LogicProgram();
		}
		return best.getHypothesis();		
	}
}
