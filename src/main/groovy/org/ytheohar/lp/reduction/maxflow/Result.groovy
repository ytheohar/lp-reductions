package org.ytheohar.lp.reduction.maxflow

import java.util.Map

import groovy.transform.TupleConstructor

@TupleConstructor
class Result {
	double maxFlow
	Map<Object, Double> maxFlowPerEdge
}
