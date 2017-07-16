package ilpme.utils;

import java.util.HashSet;
import java.util.LinkedHashSet;
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
		PartialHypotheis h = new PartialHypotheis(), big = h1, small = h2;
		
		if(h1.getGeneralization().length()<h2.getGeneralization().length()){
			big = h2;
			small = h1;
		}
		
		LogicProgram generalization = new LogicProgram(big.getGeneralization(), small.getGeneralization());
		
		/**
		 * merger literals
		 */
		Set<Atom> literals = new HashSet<Atom>();
		literals.addAll(big.getLiterals());
		int offset = big.getGeneralization().length();
		
		for(Atom lit: small.getLiterals()){
			Atom.Builder builder = new Atom.Builder(lit);
			Number newId = (new Number.Builder(((Number) lit.getTerm(0)).getValue()+offset)).build();
			builder.setTerm(0,newId);
			literals.add(builder.build());
		}
		
		h.setGeneralization(generalization);
		h.setLiterals(literals);
		h.setIndex(h1.getIndex());
		return h;
	}
	
	public String[] asClauses(Clause[] clauses, Set<Atom> selectedLiterals) {
		Set<String> result = new LinkedHashSet<>();
		if (clauses.length > 0) {
			result.add("use_clause_literal(V1,V2):-selected_use_clause_literal(V1,V2).");
			
			result.add("{ use_clause_literal(V1,0) }:-clause(V1), not selected_use_clause_literal(V1,0).");
			
			for(Atom atom: selectedLiterals){
				result.add("selected_"+ atom.toString());
			}
			
			boolean hasLiterals = false;
			for (int clauseId = 0; !hasLiterals && clauseId < clauses.length; clauseId++)
				hasLiterals = clauses[clauseId].getBody().length > 0;
			
			if (hasLiterals)
				result.add("{ use_clause_literal(V1,V2) }:-clause(V1),literal(V1,V2), not selected_use_clause_literal(V1,V2).");

			for (int clauseId = 0; clauseId < clauses.length; clauseId++) {
				result.add(String.format("%% %s", clauses[clauseId]));
				Literal[] literals = clauses[clauseId].getBody();
				result.add(String.format("clause(%d).", clauseId));
				for (int literalId = 1; literalId <= literals.length; literalId++)
					result.add(String.format("literal(%d,%d).", clauseId, literalId));

				for (int level = 0; level < clauses[clauseId].getLevels(); level++)
					result.add(String.format(":-not clause_level(%d,%d),clause_level(%d,%d).", clauseId, level, clauseId, 1 + level));

				result.add(String.format("clause_level(%d,0):-use_clause_literal(%d,0).", clauseId, clauseId));
				for (int literalId = 1; literalId <= literals.length; literalId++)
					result.add(String.format("clause_level(%d,%d):-use_clause_literal(%d,%d).", clauseId, literals[literalId - 1].getLevel(), clauseId,
							literalId));

				Atom head = clauses[clauseId].getHead();
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
		return result.toArray(new String[result.size()]);
	}
}
