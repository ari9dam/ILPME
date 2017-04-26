package ilpme.app;

import java.util.Set;

import ilpme.entities.ILPMEProblem;
import ilpme.entities.PartialHypotheis;

public interface IIterativeSolver {
	public Set<PartialHypotheis> refine(ILPMEProblem problem, PartialHypotheis top);
}
