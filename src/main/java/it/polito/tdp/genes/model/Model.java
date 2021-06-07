package it.polito.tdp.genes.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	
	private List<Genes> essentialGenes ;
	private Map<String, Genes> essentialGenesIdMap ;
	
	private Graph<Genes, DefaultWeightedEdge> grafo ;
	
	public String creaGrafo() {
		GenesDao dao = new GenesDao() ;
		this.essentialGenes = dao.getAllEssentialGenes() ;
		this.essentialGenesIdMap = new HashMap<>() ;
		for(Genes g: this.essentialGenes) 
			this.essentialGenesIdMap.put(g.getGeneId(), g) ;
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class) ;
		
		Graphs.addAllVertices(this.grafo, this.essentialGenes) ;
		
		List<Interactions> archi = dao.getInteractions(essentialGenesIdMap) ;
		for(Interactions arco: archi) {
			if(arco.getGene1().getChromosome() == arco.getGene2().getChromosome() ) {
				Graphs.addEdge(this.grafo, arco.getGene1(), arco.getGene2(), 
						Math.abs(arco.getExpressionCorr()*2.0)) ;
			} else {
				Graphs.addEdge(this.grafo, arco.getGene1(), arco.getGene2(),
						Math.abs(arco.getExpressionCorr())) ;
			}
		}
		
		return String.format("Grafo creato con %d vertici e %d archi\n",
				this.grafo.vertexSet().size(), this.grafo.edgeSet().size()) ;
	}
	
}
