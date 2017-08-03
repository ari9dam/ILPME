package ilpme.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import ilpme.entities.LogicProgram;
import ilpme.entities.PartialHypotheis;
import ilpme.xhail.core.terms.Atom;
import ilpme.xhail.core.terms.Clause;
import ilpme.xhail.core.terms.Literal;
import ilpme.xhail.core.terms.Number;

public class PartialHypothesisUtils {
	public PartialHypotheis mergePartialHypothesis(PartialHypotheis h1, PartialHypotheis h2){
		PartialHypotheis h = new PartialHypotheis(0);
		
		List<Clause> merged = new LinkedList<Clause>();
		merged.addAll(h1.getGeneralization().getRules());
		for(Clause cl: h2.getGeneralization().getRules()){
			if(h2.getLiterals().isEmpty()&& !merged.contains(cl)){
				merged.add(cl);
			}
		}
		
		LogicProgram generalization = new LogicProgram(merged);
		
		
		/**
		 * merger literals
		 */
		Set<Atom> literals = new HashSet<Atom>();
		literals.addAll(h1.getLiterals());
		int offset = h1.getGeneralization().length();
		
		for(Atom lit: h2.getLiterals()){
			Atom.Builder builder = new Atom.Builder(lit);
			Number newId = (new Number.Builder(((Number) lit.getTerm(0)).getValue()+offset)).build();
			builder.setTerm(0,newId);
			literals.add(builder.build());
		}
		
		h.setGeneralization(generalization);
		h.setLiterals(literals);
		h.setIndex(h1.getIndex());
		h.setCoverage(h1.getCoverage());
		h.setSamples(h1.getSamples());
		h.setIndex(h1.getIndex());
		return h;
	}
	
	public List<String> asClauses(ArrayList<Clause> arrayList, Set<Atom> selectedLiterals, Integer selectionLimit) {
		Set<String> result = new LinkedHashSet<>();
		if (arrayList.size() > 0) {
			result.add("#hide.");
			result.add("#show use_clause_literal/2.");
			
			result.add("use_clause_literal(V1,V2):-selected_use_clause_literal(V1,V2).");
			
			result.add("{ use_clause_literal(V1,0) } 4 :-clause(V1), not selected_use_clause_literal(V1,0).");
			
			for(Atom atom: selectedLiterals){
				result.add("selected_"+ atom.toString()+".");
			}
			
			
			
			boolean hasLiterals = false;
			for (int clauseId = 0; !hasLiterals && clauseId < arrayList.size(); clauseId++)
				hasLiterals = arrayList.get(clauseId).getBody().length > 0;
			
			if (hasLiterals&& selectionLimit==null)
				result.add("{ use_clause_literal(V1,V2) } 8:-clause(V1),literal(V1,V2), not selected_use_clause_literal(V1,V2).");
			else if(hasLiterals){
				result.add("0 { use_clause_literal(V1,V2) }"+selectionLimit
						+" :-clause(V1),literal(V1,V2), not selected_use_clause_literal(V1,V2).");
			}
			for (int clauseId = 0; clauseId < arrayList.size(); clauseId++) {
				result.add(String.format("%% %s", arrayList.get(clauseId)));
				Literal[] literals = arrayList.get(clauseId).getBody();
				result.add(String.format("clause(%d).", clauseId));
				for (int literalId = 1; literalId <= literals.length; literalId++)
					result.add(String.format("literal(%d,%d).", clauseId, literalId));

				for (int level = 0; level < arrayList.get(clauseId).getLevels(); level++)
					result.add(String.format(":-not clause_level(%d,%d),clause_level(%d,%d).", clauseId, level, clauseId, 1 + level));

				result.add(String.format("clause_level(%d,0):-use_clause_literal(%d,0).", clauseId, clauseId));
				for (int literalId = 1; literalId <= literals.length; literalId++)
					result.add(String.format("clause_level(%d,%d):-use_clause_literal(%d,%d).", clauseId, literals[literalId - 1].getLevel(), clauseId,
							literalId));

				Atom head = arrayList.get(clauseId).getHead();
				result.add(String.format("#minimize[ use_clause_literal(%d,0) =%d @%d ].", clauseId, head.getWeight(), head.getPriority()));

				for (int literalId = 1; literalId <= literals.length; literalId++)
					result.add(String.format("#minimize[ use_clause_literal(%d,%d) =%d @%d ].", clauseId, literalId, //
							literals[literalId - 1].getWeight(), literals[literalId - 1].getPriority()));

				Set<String> set = new LinkedHashSet<>();
				for (String type : head.getTypes())
					set.add(type);
				String[] array = new String[literals.length];
				for (int literalId = 1; literalId <= literals.length; literalId++) {
					String variables = literals[literalId - 1].hasVariables() ? "," + StringUtils.join(literals[literalId - 1].getVariables(), ",") : "";
					array[literalId - 1] = String.format("try_clause_literal(%d,%d%s)", clauseId, literalId, variables);
					for (String type : literals[literalId - 1].getTypes())
						set.add(type);
				}
				String typesAll = !set.isEmpty() ? "," + StringUtils.join(set, ",") : "";
				String literalsAll = array.length > 0 ? "," + StringUtils.join(array, ",") : "";
				result.add(String.format("%s:-use_clause_literal(%d,0)%s%s.", head, clauseId, literalsAll, typesAll));

				for (int literalId = 1; literalId <= literals.length; literalId++) {
					String variables = literals[literalId - 1].hasVariables() ? "," + StringUtils.join(literals[literalId - 1].getVariables(), ",") : "";
					String types = literals[literalId - 1].hasTypes() ? "," + StringUtils.join(literals[literalId - 1].getTypes(), ",") : "";
					result.add(String.format("try_clause_literal(%d,%d%s):-use_clause_literal(%d,%d),%s%s.", //
							clauseId, literalId, variables, clauseId, literalId, literals[literalId - 1], types));
					result.add(String.format("try_clause_literal(%d,%d%s):-not use_clause_literal(%d,%d)%s.", //
							clauseId, literalId, variables, clauseId, literalId, types));
				}

			}
		}
		return new LinkedList<String>(result);
	}
}
