package ilpme.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ilpme.xhail.core.Buildable;
import ilpme.xhail.core.statements.ModeB;
import ilpme.xhail.core.statements.ModeH;

public class ModeDeclarations {

	public static class Builder implements Buildable<ModeDeclarations> {
		private Map<ModeH,List<ModeB>> modeDeclarations;		
		public Builder(){
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
		
		@Override
		public ModeDeclarations build() {
			ModeDeclarations modes = new ModeDeclarations(modeDeclarations);
			return modes;
		}
	}
	
	private ModeB[] modeBs = null;
	private ModeH[] modeHs = null;
	private Map<ModeH,ModeB[]> modeDeclarations;
	
	
	private ModeDeclarations(Map<ModeH,List<ModeB>> modes){
		this.modeHs = modes.keySet().toArray(new ModeH[modes.keySet().size()]);
		this.modeDeclarations = new HashMap<ModeH,ModeB[]>();
		
		Set<ModeB> allmodebs = new HashSet<ModeB>();
		for(Entry<ModeH, List<ModeB>> entry: modes.entrySet()){
			ModeB[] modebs = entry.getValue().toArray(new ModeB[0]);
			modeDeclarations.put(entry.getKey(), modebs);
			allmodebs.addAll(entry.getValue());
		}
		this.modeBs =  allmodebs.toArray(new ModeB[0]);
	}

	public ModeB[] getModebsForModeh(ModeH key){
		return this.modeDeclarations.get(key);
	}

	public ModeB[] getAllModeBs() {
		return modeBs;
	}

	public ModeH[] getAllModeHs() {
		return modeHs;
	}
}
