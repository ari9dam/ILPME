package ilpme.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ilpme.entities.PartialHypotheis;

public class PartialHypotheisQueue implements IPartialHypothesisPool{
	List<PartialHypotheis> queue;
	
	
	public PartialHypotheisQueue(){
		this.queue = new LinkedList<PartialHypotheis>();
	}

	public PartialHypotheis getNext(){
		return null;
	}
	
	public void addAll(Set<PartialHypotheis> refinements){
		this.queue.addAll(refinements);
	}

	public boolean hasNext() {
		
		return queue.isEmpty();
	}
}
