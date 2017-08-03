package ilpme.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import ilpme.xhail.core.terms.Atom;
import ilpme.xhail.core.terms.Clause;
import ilpme.xhail.core.terms.Literal;
import ilpme.xhail.core.terms.Number;
import ilpme.xhail.core.terms.Variable;

/**
 * 
 * @author Arindam
 * Each partial hypothesis is a collection of normal clauses
 * Two partial hypothesis can be combined
 * Each partial hypothesis can be refined
 */
public class PartialHypotheis {
	private int index;  //covers the examples  E{1}...E{index} and subset
	private LogicProgram generalization; //the root of the partial hypothesis
	private LogicProgram hypothesis = null;
	private Set<Atom> literals;
	private int coverage;//number of problem it solves
	private LinkedList<Integer> samples;
	 
	
	public PartialHypotheis(int poolSize) {
		this.samples =new LinkedList<Integer>();
		for(int i = 0;i<poolSize;i++) this.samples.add(i);
		coverage = 0;
		this.literals = new HashSet<Atom>();
	}
	
	public PartialHypotheis(Clause[] generalization, Atom[] literals, int poolSize) {
		this(poolSize);
		this.generalization = new LogicProgram(generalization);
		this.literals.addAll(Arrays.asList(literals));
	}
	
	public PartialHypotheis(LogicProgram generalization, Set<Atom> literals, int poolSize) {
		this(poolSize);
		this.generalization = new LogicProgram(generalization.getRules());
		this.literals.addAll(literals);
	}


	public PartialHypotheis(Collection<Clause> lp) {
		this(0);
		this.generalization = new LogicProgram(lp);
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
		this.hypothesis = null;
	}
	
	public Set<Atom> getLiterals() {
		return literals;
	}
	public void setLiterals(Set<Atom> literals) {
		this.literals = literals;
		this.hypothesis = null;
	}

	public LogicProgram getHypothesis() {
		if(this.hypothesis!=null)
			return this.hypothesis;
		
		Set<Clause> set = new HashSet<>();
		ArrayList<Clause> generalisation = this.generalization.getRules();
		Map<Integer, Clause.Builder> builders = new HashMap<>();
		Map<Integer, Set<Literal>> types = new HashMap<>();
		
		for (Atom atom : literals) {
			int clauseId = ((Number) atom.getTerm(0)).getValue();
			int literalId = ((Number) atom.getTerm(1)).getValue();
			if (0 == literalId && 0 <= clauseId && clauseId < generalisation.size()) {
				builders.put(clauseId, new Clause.Builder().setHead(generalisation.get(clauseId)
						.getHead()));
				types.put(clauseId, new LinkedHashSet<>());
			}
		}
		for (Atom atom : literals) {
			int clauseId = ((Number) atom.getTerm(0)).getValue();
			int literalId = ((Number) atom.getTerm(1)).getValue();
			Set<Literal> literals = types.get(clauseId);
			Atom head = generalisation.get(clauseId).getHead();
			for (Variable variable : head.getVariables())
				literals.add(new Literal.Builder( //
						new Atom.Builder(variable.getType().getIdentifier()).
						addTerm(variable).build() //
				).build());
			if (literalId > 0 && 0 <= clauseId && clauseId < generalisation.size()) {

				Literal literal = generalisation.get(clauseId).getBody(literalId);
				builders.get(clauseId).addLiteral(literal);
				
				for (Variable variable : literal.getVariables())
					literals.add(new Literal.Builder( //
							new Atom.Builder(variable.getType().getIdentifier()).
							addTerm(variable).build() //
					).build());
			}
		}
		for (int c : builders.keySet()) {
			Clause.Builder builder = builders.get(c);
			for (Literal literal : types.get(c))
				builder.addLiteral(literal);
			set.add(builder.build());
		}
		this.hypothesis = new LogicProgram(set);
		return this.hypothesis;
	}

	public void increaseCoverage() {
		coverage++;
	}

	public int getCoverage() {
		return coverage;
	}
	
	public void setCoverage(int val) {
		coverage = val;
	}
	
	public void setPoolSize(int n){
		this.samples =new LinkedList<Integer>();
		for(int i = 0;i<n;i++) this.samples.add(i);
	}

	public LinkedList<Integer> getSamples() {
		return samples;
	}

	public void setSamples(LinkedList<Integer> samples) {
		this.samples = new LinkedList<Integer>();
		this.samples.addAll(samples);
	}

	public void slideSample(int i) {
		int val = this.samples.get(i);
		this.samples.remove(i);
		this.samples.add(val);
	}
	
	public int getPoolSize(){
		return this.samples.size();
	}
	
	public int getSampleId(int i){
		return this.samples.get(i);
	}
}
