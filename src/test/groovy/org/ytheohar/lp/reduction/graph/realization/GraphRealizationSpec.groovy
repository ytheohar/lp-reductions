package org.ytheohar.lp.reduction.graph.realization

import org.ytheohar.lp.reduction.graph.realization.GraphRealization;
import org.ytheohar.lp.solver.ApacheSimplexSolver

import spock.lang.Specification
import spock.lang.Unroll

class GraphRealizationSpec extends Specification {

	def 'graph realization reduction n=3' () {
		given:
		def gr = GraphRealization.reduction(false)

		when:
		def lpi = gr.reduce([2, 2, 2])
		
		then:
		lpi.objectiveFunction.coeffMap.size() == 0
		lpi.objectiveFunction.constant == 0

		lpi.constraints.size() == 6
		lpi.constraints[0].function.coeffMap == [ [0, 1]:1, [0, 2]:1]
		lpi.constraints[1].function.coeffMap == [ [0, 1]:1, [1, 2]:1]
		lpi.constraints[2].function.coeffMap == [ [0, 2]:1, [1, 2]:1]
		lpi.constraints[3].function.coeffMap == [ [0, 1]:1 ]
		lpi.constraints[4].function.coeffMap == [ [0, 2]:1 ]
		lpi.constraints[5].function.coeffMap == [ [1, 2]:1 ]
	}

	@Unroll
	def 'graph realization reduction and solution n=3' () {
		given:
		def gr = GraphRealization.reduction(decisionVersion)

		when:
		def res = gr.reduceAndSolve([2, 2, 2])

		then:
		res == expected
		
		where:
		decisionVersion || expected
		false 			|| [ [0, 1]:1, [0, 2]:1, [1, 2]:1 ]
		true			|| true
	}

	@Unroll
	def 'unrealizable sequence' () {
		given:
		def gr = GraphRealization.reduction(decisionVersion)

		when:
		def res = gr.reduceAndSolve([2, 2, 1])

		then:
		res == expected
		
		where:
		decisionVersion || expected
		false 			|| null
		true			|| false
	}
}
