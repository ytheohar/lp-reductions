package org.ytheohar.lp.reduction.maxflow

import java.util.ArrayList
import java.util.Collection
import org.ytheohar.lp.model.ConstraintRelationship
import org.ytheohar.lp.model.Solution
import org.ytheohar.lp.solver.ApacheSimplexSolver

import spock.lang.Specification
import spock.lang.Unroll

class MaxFlowSpec extends Specification {

	def 'simple max flow example' () {
		given:
			def g = new Graph([
		        [0, 1]:3,
				[0, 2]:2,
				[0, 3]:2,
				[1, 4]:5,
				[1, 5]:1,
				[2, 4]:1,
				[2, 5]:3,
				[2, 6]:1,
				[3, 5]:1,
				[4, 7]:4,
				[5, 7]:2,
				[6, 7]:4,
			], 0, 7)
			def mf = MaxFlow.reduction()

		when:
			def lpi = mf.mapToLP(g)
			def o = lpi.objectiveFunction
			def constraints = lpi.constraints
	
		then:
			o.coeffMap[[0, 1]] == 1
			o.coeffMap[[0, 2]] == 1
			o.coeffMap[[0, 3]] == 1
			o.constant == 0

			constraints.size() == g.edges.size() + g.nodes.size() - 2
			g.edges.eachWithIndex { e, l, i ->
				assert constraints[i].function.coeffMap[e] == 1
				assert constraints[i].constant == l
				assert constraints[i].relationship == ConstraintRelationship.LEQ
			}

			constraints[g.edges.size()].function.coeffMap == [ [0, 1]:1, [1, 4]:-1, [1, 5]:-1]
			constraints[g.edges.size()+1].function.coeffMap == [ [0, 2]:1, [2, 4]:-1, [2, 5]:-1, [2, 6]:-1]
			constraints[g.edges.size()+2].function.coeffMap == [ [0, 3]:1, [3, 5]:-1]
			constraints[g.edges.size()+3].function.coeffMap == [ [1, 4]:1, [2, 4]:1, [4, 7]:-1]
			constraints[g.edges.size()+4].function.coeffMap == [ [1, 5]:1, [2, 5]:1, [3, 5]:1, [5, 7]:-1]
			constraints[g.edges.size()+5].function.coeffMap == [ [2, 6]:1, [6, 7]:-1]
	}

	def 'simple max flow example2' () {
		given:
			def g = new Graph([
			    [0, 1]:3,
			    [0, 2]:2,
			    [0, 3]:2,
				[1, 4]:5,
				[1, 5]:1,
				[2, 4]:1,
				[2, 5]:3,
				[2, 6]:1,
				[3, 5]:1,
				[4, 7]:4,
				[5, 7]:2,
				[6, 7]:4,
			], 0, 7)
			def mf = MaxFlow.reduction()

		when:
			def res = mf.reduce(g)
			
		then:
			res.maxFlow == 6
			res.maxFlowPerEdge[0, 1] == 3
			res.maxFlowPerEdge[0, 2] == 2
			res.maxFlowPerEdge[0, 3] == 1
			res.maxFlowPerEdge[1, 4] == 3
			res.maxFlowPerEdge[1, 5] == 0
			res.maxFlowPerEdge[2, 4] == 1
			res.maxFlowPerEdge[2, 5] == 1
			res.maxFlowPerEdge[2, 6] == 0
			res.maxFlowPerEdge[3, 5] == 1
			res.maxFlowPerEdge[4, 7] == 4
			res.maxFlowPerEdge[5, 7] == 2
			res.maxFlowPerEdge[6, 7] == 0
	}

}
