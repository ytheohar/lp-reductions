package org.ytheohar.lp.reduction.maxflow

class Graph {

	def s
	def t
	def nodes = [] as Set
	def edges
	def outEdges = [:]
	def inEdges = [:]
	
	Graph(edges, s, t) {
		this.s = s
		this.t = t
		this.edges = edges
		edges.each { e, l ->
			nodes << e[0]
			nodes << e[1]
			addEdge(outEdges, e, 0)
			addEdge(inEdges, e, 1)
		}
	}

	private addEdge(nodeToEdges, e, i) {
		if (!nodeToEdges[e[i]]) {
			nodeToEdges[e[i]] = []
		}
		nodeToEdges[e[i]] << e
	}
	
}
