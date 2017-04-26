package ilpme.utils;

import java.util.Set;

import ilpme.entities.PartialHypotheis;

public interface IPartialHypothesisPool {
	public PartialHypotheis getNext();
	public void addAll(Set<PartialHypotheis> refinements);
	public boolean hasNext();
}
