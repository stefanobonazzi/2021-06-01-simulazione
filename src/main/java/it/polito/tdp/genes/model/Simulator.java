package it.polito.tdp.genes.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.genes.model.Event.EventType;

public class Simulator {

	//Queue
	private PriorityQueue<Event> queue;
	
	//Parameters
	private int nIng;
	private Genes gene;
	//every month choise
	
	//Outcome
	private Map<Genes, Integer> inCorso;
	
	//World status
	private Graph<Genes, DefaultWeightedEdge> graph;
	
	public Simulator(Graph<Genes, DefaultWeightedEdge> graph, int nIng, Genes gene) {
		this.graph = graph;
		this.nIng = nIng;
		this.gene = gene;
		this.inCorso = new HashMap<Genes, Integer>();
	}
	
	public void initialise() {
		this.queue = new PriorityQueue<Event>();
		this.inCorso.put(gene, nIng);
		for(int i=0; i<nIng; i++) {
			if(Math.random() <= 0.3) {
				Event ee = new Event(1, gene, gene, EventType.STAY, 1);
				this.queue.add(ee);
			} else {
				Genes to = null;
				List<Genes> genes = Graphs.neighborListOf(this.graph, gene);
				Double d = Math.random();
				Double sum = 0.0;
				for(Genes g: genes) {
					sum += this.graph.getEdgeWeight(this.graph.getEdge(gene, g));
				}
				for(Genes g: genes) {
					if(this.graph.getEdgeWeight(this.graph.getEdge(gene, g))/sum > d) {
						to = g;
					}
				}
				if(genes == null) {
					System.out.println("Non ci sono archi");
					return;
				}
				if(to == null) {
					to = genes.get(genes.size()-1);
				}
				
				Event ee = new Event(1, gene, to, EventType.OUT, 1);
				this.queue.add(ee);
			}
		}
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			this.processEvent(e);
		}
		
		System.out.println(this.inCorso);
	}

	private void processEvent(Event e) {
		if(e.getTime() > 36) {
			System.out.println(e.getTime());
			return;
		}
		if(e.getGeneTo() == null) {
			return;
		}
		switch (e.getType()) {
		case STAY:
			if(Math.random() <= 0.3) {
				Event ee = new Event(1, e.getGeneFrom(), e.getGeneFrom(), EventType.STAY, e.getTime()+1);
				this.queue.add(ee);
			} else {
				Genes to = null;
				List<Genes> genes = Graphs.neighborListOf(this.graph, e.getGeneTo());
				Double d = Math.random();
				Double sum = 0.0;
				for(Genes g: genes) {
					sum += this.graph.getEdgeWeight(this.graph.getEdge(e.getGeneTo(), g));
				}
				for(Genes g: genes) {
					if(this.graph.getEdgeWeight(this.graph.getEdge(e.getGeneTo(), g))/sum > d) {
						to = g;
					}
				}
				if(to == null) {
					to = genes.get(genes.size()-1);
				}
				
				Event ee = new Event(1, e.getGeneTo(), to, EventType.OUT, e.getTime()+1);
				this.queue.add(ee);
			}
			break;
			
		case OUT:
			this.inCorso.put(e.getGeneFrom(), this.inCorso.get(e.getGeneFrom()) - 1);
			
			if(this.inCorso.containsKey(e.getGeneTo())) {
				this.inCorso.put(e.getGeneTo(), this.inCorso.get(e.getGeneTo()) + 1);
			} else {
				this.inCorso.put(e.getGeneTo(), 1);
			}
			
			if(Math.random() <= 0.3) {
				Event ee = new Event(1, e.getGeneTo(), e.getGeneTo(), EventType.STAY, e.getTime()+1);
				this.queue.add(ee);
			} else {
				Genes to = null;
				List<Genes> genes = Graphs.neighborListOf(this.graph, e.getGeneTo());
				Double d = Math.random();
				Double sum = 0.0;
				for(Genes g: genes) {
					if(!g.equals(e.getGeneTo()) && !this.inCorso.containsKey(g))
						sum += this.graph.getEdgeWeight(this.graph.getEdge(e.getGeneTo(), g));
				}
				for(Genes g: genes) {
					if(!g.equals(e.getGeneTo()) && !this.inCorso.containsKey(g)) {
						if(this.graph.getEdgeWeight(this.graph.getEdge(e.getGeneTo(), g))/sum > d && !g.equals(e.getGeneTo())) {
							to = g;
						}
					}
				}
				if(to == null) {
					if(!genes.get(genes.size()-1).equals(e.getGeneTo()) && !this.inCorso.containsKey(genes.get(genes.size()-1)))
						to = genes.get(genes.size()-1);
				}
				
				Event ee = new Event(1, e.getGeneTo(), to, EventType.OUT, e.getTime()+1);
				this.queue.add(ee);
			}
			break;
		}
		return;
	}
}
