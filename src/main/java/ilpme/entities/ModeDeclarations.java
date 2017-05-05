package ilpme.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ilpme.xhail.core.statements.ModeB;
import ilpme.xhail.core.statements.ModeH;

public class ModeDeclarations {
	private Map<ModeH,List<ModeB>> modeDeclarations;
	
	public ModeDeclarations(){
		this.modeDeclarations = new HashMap<ModeH,List<ModeB>>();
	}
	
	public void mergeAll(Map<ModeH,List<ModeB>> modeDeclarations){
		for(Entry<ModeH, List<ModeB>> entry: modeDeclarations.entrySet()){
			if(this.modeDeclarations.containsKey(entry.getKey())){
				this.modeDeclarations.get(entry.getKey()).addAll(entry.getValue());
			}else {
				this.modeDeclarations.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	public List<ModeB> getModebsForModeh(ModeH key){
		return this.modeDeclarations.get(key);
	}
}
