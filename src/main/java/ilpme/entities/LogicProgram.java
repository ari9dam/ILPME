package ilpme.entities;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import ilpme.xhail.core.terms.Clause;

public class LogicProgram {
	private ArrayList<Clause> program;

	public LogicProgram() {
		this.program = new ArrayList<Clause>();
	}
	
	/***
	 * appends q to p
	 * should maintain the order
	 * @param p
	 * @param q
	 */
	public LogicProgram(LogicProgram p, LogicProgram q) {
		this.program = new ArrayList<Clause>();
		this.program.addAll(p.getRules());
		this.program.addAll(q.getRules());
	}
	
	public LogicProgram(Clause[] generalization) {
		this.program = new ArrayList<Clause>();
		for(Clause rule : generalization){
			this.program.add(rule);
		}
	}

	public LogicProgram(Collection<Clause> lp) {
		this.program = new ArrayList<Clause>();
		for(Clause rule : lp){
			this.program.add(rule);
		}
	}

	public ArrayList<Clause> getRules() {
		return this.program;
	}

	public void addRules(Clause[] rules){
		for(Clause rule : rules){
			this.program.add(rule);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((program == null) ? 0 : program.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogicProgram other = (LogicProgram) obj;
		if (program == null) {
			if (other.program != null)
				return false;
		} else if (!program.equals(other.program))
			return false;
		return true;
	}

	public int length() {
		
		return this.program.size();
	}

	@Override
	public String toString() {
		return StringUtils.join(this.program, "\n");
	}
	
	
}
