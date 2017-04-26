package ilpme.entities;

import java.util.HashSet;
import java.util.Set;

import ilpme.xhail.core.terms.Clause;

public class LogicProgram {
	private Set<Clause> program;

	public LogicProgram() {
		this.program = new HashSet<Clause>();
	}
	
	public LogicProgram(LogicProgram p, LogicProgram q) {
		this.program = new HashSet<Clause>();
		this.program.addAll(p.getRules());
		this.program.addAll(q.getRules());
	}
	
	private Set<Clause> getRules() {
		return this.program;
	}

	public void addRules(Clause[] rules){
		for(Clause rule : rules){
			this.program.add(rule);
		}
	}
}
