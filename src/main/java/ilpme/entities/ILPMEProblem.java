package ilpme.entities;

import java.util.List;
import java.util.Map;

import ilpme.xhail.core.statements.ModeB;
import ilpme.xhail.core.statements.ModeH;

public class ILPMEProblem {
	public List<Sample> trainingData;
	String background;
	Map<ModeH,List<ModeB>> modeDeclarations;
	int maxAddition;
	public ILPMEProblem(List<Sample> trainingData, String background, Map<ModeH, List<ModeB>> modeDeclarations,
			int maxAddition) {
		super();
		this.trainingData = trainingData;
		this.background = background;
		this.modeDeclarations = modeDeclarations;
		this.maxAddition = maxAddition;
	}
	public List<Sample> getTrainingData() {
		return trainingData;
	}
	public String getBackground() {
		return background;
	}
	public Map<ModeH, List<ModeB>> getModeDeclarations() {
		return modeDeclarations;
	}
	public int getMaxAddition() {
		return maxAddition;
	}
	
}
