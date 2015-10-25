package org.ytheohar.lp.reduction.maxflow

import java.util.List

import org.ytheohar.lp.reduction.LPReduction;
import org.ytheohar.lp.model.LPConstraint
import org.ytheohar.lp.model.LPFunction
import org.ytheohar.lp.model.LPSolver
import org.ytheohar.lp.model.ConstraintRelationship
import org.ytheohar.lp.model.Solution
import org.ytheohar.lp.model.LPIState

static LPReduction reduction() {
	def reduction = new LPReduction()

	reduction.with {

		vars = { Graph g ->
			def vars = g.edges.collect { it.key }
			vars << [g.t, g.s]
		}

		objFunc = { Graph g ->
			def coeffMap = g.outEdges[g.s].collectEntries { [(it): 1] }
			new LPFunction(coeffMap, 0)
		}

		constraints = { Graph g ->
			def constraints = g.edges.collect { e, l ->
				LPFunction f = new LPFunction([(e):1], 0)
				new LPConstraint(f, ConstraintRelationship.LEQ, l)
			}
			constraints += g.nodes.findAll {
				it != g.s && it != g.t
			}.collect { n ->
				def coeffMap = [:]
				g.inEdges[n].each { e -> coeffMap[e] = 1 }
				g.outEdges[n].each { e -> coeffMap[e] = -1 }
				LPFunction f = new LPFunction(coeffMap, 0)
				new LPConstraint(f, ConstraintRelationship.EQ, 0)
			}
		}

		result = { Solution s -> s.state == LPIState.FEASIBLE ? new Result(s.optimum, s.at) : null }

		nonNegative = true
	}

	reduction
}