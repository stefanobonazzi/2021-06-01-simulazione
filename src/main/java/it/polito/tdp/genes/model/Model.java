package it.polito.tdp.genes.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	
	private List<Genes> essentialGenes ;
	private Graph<Genes, DefaultWeightedEdge> grafo ;
	
	public String creaGrafo() {
		GenesDao dao = new GenesDao() ;
		this.essentialGenes = dao.getAllEssentialGenes() ;
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class) ;
		
		Graphs.addAllVertices(this.grafo, this.essentialGenes) ;
		
		return String.format("Grafo creato con %d vertici e %d archi\n",
				this.grafo.vertexSet().size(), this.grafo.edgeSet().size()) ;
	}
	
}
