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
		this.modeHs = (ModeH[]) modes.keySet().toArray();
		this.modeDeclarations = new HashMap<ModeH,ModeB[]>();
		
		Set<ModeB> allmodebs = new HashSet<ModeB>();
		for(Entry<ModeH, List<ModeB>> entry: modes.entrySet()){
			ModeB[] modebs = (ModeB[]) entry.getValue().toArray();
			modeDeclarations.put(entry.getKey(), modebs);
			allmodebs.addAll(entry.getValue());
		}
		this.modeBs = (ModeB[]) allmodebs.toArray();
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
