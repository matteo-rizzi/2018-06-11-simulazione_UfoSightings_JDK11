package it.polito.tdp.ufo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {
	
	private SightingsDAO dao;
	private Map<String, State> idMap;
	private Graph<State, DefaultEdge> grafo;
	private List<State> best;
	
	public Model() {
		this.dao = new SightingsDAO();
		this.idMap = new HashMap<String, State>();
		this.dao.getStates(idMap);
		
	}
	
	public List<AnnoAvvistamenti> getAnniAvvistamenti() {
		return this.dao.getAnniAvvistamenti();
	}
	
	public void creaGrafo(int anno) {
		this.grafo = new SimpleDirectedGraph<State, DefaultEdge>(DefaultEdge.class);
		
		// aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.dao.getStatesByYear(anno));
		
		// aggiungo gli archi
		for(Adiacenza a : this.dao.getAdiacenze(anno, this.idMap)) {
			this.grafo.addEdge(a.getPrimo(), a.getSecondo());
		}
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<State> getVertici() {
		List<State> states = new ArrayList<>(this.grafo.vertexSet());
		Collections.sort(states);
		return states;
	}
	
	public List<State> getStatiPrecedenti(State selezionato) {
		return Graphs.predecessorListOf(this.grafo, selezionato);
	}

	public List<State> getStatiSuccessivi(State selezionato) {
		return Graphs.successorListOf(this.grafo, selezionato);
	}
	
	public List<State> getStatiRaggiungibili(State selezionato) {
		List<State> raggiungibili = new ArrayList<>();
		
		GraphIterator<State, DefaultEdge> bfv = new BreadthFirstIterator<>(this.grafo, selezionato);
		while(bfv.hasNext()) {
			raggiungibili.add(bfv.next());		
		}
		
		return raggiungibili;
	}
	
	public List<State> sequenzaAvvistamenti(State partenza) {
		this.best = new ArrayList<>();
		List<State> parziale = new ArrayList<>();
		parziale.add(partenza);
		

		this.cerca(parziale, 0);

		return best;
	}

	private void cerca(List<State> parziale, int livello) {
		// caso terminale
		if(parziale.size() > this.best.size())
			this.best = new ArrayList<>(parziale);
		
		// caso intermedio
		State ultimo = parziale.get(livello);
		for(State vicino : Graphs.successorListOf(this.grafo, ultimo)) {
			if(!parziale.contains(vicino)) {
				parziale.add(vicino);
				this.cerca(parziale, livello + 1);
				parziale.remove(vicino);
			}
		}
		
	}
}
