package it.polito.tdp.genes.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import it.polito.tdp.genes.db.GenesDao;

public class Model {
	
	private GenesDao dao;
	private List<Genes> essentials;
	private Graph<Genes, DefaultWeightedEdge> graph;
	private Simulator simulator;
	
	public Model() {
		this.dao = new GenesDao();
		this.graph = new SimpleWeightedGraph<Genes, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	}
	
	public List<Genes> creaGrafo() {
		this.essentials = this.dao.getEssentialGenes();
		
		Graphs.addAllVertices(this.graph, this.essentials);
		
		List<Arco> archi = this.dao.getArchi();
		
		for(Arco a: archi) {
			Double weight = a.getWeight();
			DefaultWeightedEdge edge = this.graph.addEdge(a.getGene1(), a.getGene2());
			if(edge != null) {
				if(a.getGene1().getChromosome() == a.getGene2().getChromosome()) {
					weight *= 2;
					this.graph.setEdgeWeight(edge, weight);
				} else {
					this.graph.setEdgeWeight(edge, weight);
				}
			}
		}
		
		return this.essentials;
	}

	public Graph<Genes, DefaultWeightedEdge> getGraph() {
		return graph;
	}

	public Map<Double, Genes> getAdiacenti(Genes g) {
		Map<Double, Genes> result = new TreeMap<>();
		for(Genes gg: Graphs.neighborListOf(this.graph, g)) {
			result.put(-this.graph.getEdgeWeight(this.graph.getEdge(g, gg)), gg);
		}
		return result;
	}

	public void simula(int nIng, Genes g) {
		this.simulator = new Simulator(graph, nIng, g);
		
		this.simulator.initialise();
		this.simulator.run();
	}
	
}
