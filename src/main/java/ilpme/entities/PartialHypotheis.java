package ilpme.entities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ilpme.xhail.core.terms.Atom;
import ilpme.xhail.core.terms.Clause;

/**
 * 
 * @author Arindam
 * Each partial hypothesis is a collection of normal clauses
 * Two partial hypothesis can be combined
 * Each partial hypothesis can be refined
 */
public class PartialHypotheis {
	private int index;  //covers the examples  E{1}...E{index}
	private LogicProgram generalization; //the root of the partial hypothesis
	private LogicProgram inductiveForm; //the root of the partial hypothesis
	private Set<Atom> literals;
	
	public PartialHypotheis() {
		super();
	}
	
	public PartialHypotheis(Clause[] generalization, Atom[] literals) {
		this.generalization = new LogicProgram(generalization);
		this.literals = new HashSet<Atom>();
		this.literals.addAll(Arrays.asList(literals));
	}

	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public LogicProgram getGeneralization() {
		return generalization;
	}
	public void setGeneralization(LogicProgram generalization) {
		this.generalization = generalization;
	}
	public LogicProgram getInductiveForm() {
		return inductiveForm;
	}
	public void setInductiveForm(LogicProgram inductiveForm) {
		this.inductiveForm = inductiveForm;
	}
	public Set<Atom> getLiterals() {
		return literals;
	}
	public void setLiterals(Set<Atom> literals) {
		this.literals = literals;
	}

	public LogicProgram getHypothesis() {
		return null;
	}
}
