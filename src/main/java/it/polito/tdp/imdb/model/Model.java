package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	ImdbDAO dao = new ImdbDAO();
	SimpleWeightedGraph<Actor, DefaultWeightedEdge> grafo;
	Map<Integer, Actor> actorsIdMap = new HashMap<>();

	public Model() {
		actorsIdMap = new HashMap<>();
		dao.listAllActors(actorsIdMap);
	}

	public void creaGrafo(String genere) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVertex(genere, actorsIdMap));
		System.out.println(grafo.vertexSet().size());
		for (Adiacenza a : dao.getAdiacenze(genere, actorsIdMap)) {
			Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA2(), a.getPeso());
		}
		System.out.println(grafo.edgeSet().size());
	}

	public List<Actor> getConnectedActors(Actor a) {
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci = new ConnectivityInspector<Actor, DefaultWeightedEdge>(
				grafo);
		List<Actor> actors = new ArrayList<>(ci.connectedSetOf(a));
		actors.remove(a);
		Collections.sort(actors, new Comparator<Actor>() {
			@Override
			public int compare(Actor o1, Actor o2) {
				return o1.lastName.compareTo(o2.lastName);
			}
		});
		return actors;
	}

	public List<Actor> getActors() {
		List<Actor> actors = new ArrayList<>(grafo.vertexSet());
		return actors;
	}

	public List<String> getAllGenre() {
		return dao.listAllGenres();
	}

	private Simulatore sim;

	List<Actor> intervistati;
	int nPaus;

	public void simula(int N) {
		sim = new Simulatore();
		sim.init(N, getActors(), grafo);
		sim.run();
		intervistati = sim.getIntervistati();
		nPaus = sim.getnPause();
	}

	public List<Actor> getIntervistati() {
		return intervistati;
	}

	public int getnPaus() {
		return nPaus;
	}

}
