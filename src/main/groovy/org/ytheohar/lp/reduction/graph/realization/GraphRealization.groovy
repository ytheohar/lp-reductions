package org.ytheohar.lp.reduction.graph.realization

import org.ytheohar.lp.reduction.LPReduction;
import org.ytheohar.lp.model.ConstraintRelationship
import org.ytheohar.lp.model.LPConstraint
import org.ytheohar.lp.model.LPFunction
import org.ytheohar.lp.model.Solution
import org.ytheohar.lp.model.LPIState

static LPReduction reduction(boolean decisionVersion) {
	def reduction = new LPReduction()

	reduction.with {

		vars = { List<Integer> seq ->
			[0..seq.size()-1, 0..seq.size()-1].combinations()
			.findAll( { u, v -> u < v } )
			.collect( { u, v -> [u, v]} )
		}

		constraints = { List<Integer> seq ->
			def constraints = (0..seq.size()-1).collect { u ->
				def coeffMap = variables.findAll{ e -> e[0]==u || e[1]==u }.collectEntries { e -> [(e) : 1] }
				LPFunction f = new LPFunction(coeffMap, 0)
				new LPConstraint(f, ConstraintRelationship.EQ, seq[u])
			}

			constraints += variables.collect { e ->
				LPFunction f = new LPFunction([(e):1], 0)
				new LPConstraint(f, ConstraintRelationship.LEQ, 1)
			}
		}

		if (decisionVersion)
			result = { Solution s-> s.state==LPIState.FEASIBLE }
		else
			result = { Solution s -> s.state==LPIState.FEASIBLE ? s.at : null }
	}

	reduction
}