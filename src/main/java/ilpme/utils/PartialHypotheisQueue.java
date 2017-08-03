package ilpme.utils;

import java.util.Set;
import java.util.Stack;

import ilpme.entities.PartialHypotheis;

public class PartialHypotheisQueue implements IPartialHypothesisPool{
	Stack<PartialHypotheis> queue;
	
	
	public PartialHypotheisQueue(){
		this.queue = new Stack<PartialHypotheis>();
	}

	public PartialHypotheis getNext(){
		return this.queue.pop();
	}
	
	public void addAll(Set<PartialHypotheis> refinements){
		this.queue.addAll(refinements);
	}

	public boolean hasNext() {
		
		return !queue.isEmpty();
	}

	
	public void add(PartialHypotheis ph) {
		this.queue.push(ph);
	}

	@Override
	public int size() {
		return this.queue.size();
	}
}
