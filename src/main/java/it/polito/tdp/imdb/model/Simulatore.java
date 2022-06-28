package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.model.Event.EvenType;

public class Simulatore {
	int numGiorni;
	int cont;
	List<Actor> daIntervistare;
	List<Actor> intervistati;
	int nPause;
	int flag;
	PriorityQueue<Event> queue;
	SimpleWeightedGraph<Actor, DefaultWeightedEdge> grafo;

	public void init(int n, List<Actor> attori, SimpleWeightedGraph<Actor, DefaultWeightedEdge> graph) {
		grafo = graph;
		queue = new PriorityQueue<Event>();
		intervistati = new ArrayList<>();
		daIntervistare = attori;
		numGiorni = n;
		nPause = 0;
		cont = 0;
		flag = 0;
		queue.add(new Event(0, EvenType.INTERVISTA));
	}

	public void run() {
		while (!queue.isEmpty()) {
			Event e = queue.poll();
			processaEvento(e);
		}
	}

	private void processaEvento(Event e) {
		switch (e.getTipo()) {
		case INTERVISTA:
			if (cont == numGiorni) {
				return;
			}
			if (e.getnIntervista() == 0 || flag == 1) {
				int i = (int) (Math.random() * (daIntervistare.size() - 1));
				Actor a = daIntervistare.get(i);
				intervistati.add(a);
				daIntervistare.remove(a);
				flag = 0;
				cont++;
				queue.add(new Event(cont, EvenType.INTERVISTA));
			} else {
				Actor a1 = intervistati.get(intervistati.size() - 1);
				Actor a2 = null;
				if (e.getnIntervista() == 1) {
					a2 = null;
				} else {
					a2 = intervistati.get(intervistati.size() - 2);
				}
				if (a1 != null && a2 != null && a1.getGender().equals(a2.getGender())) {
					if (Math.random() > 0.9) {
						queue.add(new Event(cont, EvenType.INTERVISTA));
					} else {
						queue.add(new Event(cont, EvenType.PAUSA));
					}
				} else {
					if (Math.random() < 0.6) {
						int i = (int) (Math.random() * daIntervistare.size() - 1);
						Actor a = daIntervistare.get(i);
						intervistati.add(a);
						daIntervistare.remove(a);
						cont++;
						queue.add(new Event(cont, EvenType.INTERVISTA));
					} else {
						Actor a3 = vicini(intervistati.get(intervistati.size() - 1));
						if (a3 == null) {
							int i = (int) (Math.random() * daIntervistare.size());
							Actor a = daIntervistare.get(i);
							intervistati.add(a);
							daIntervistare.remove(a);
							cont++;
							queue.add(new Event(cont, EvenType.INTERVISTA));
						} else {
							intervistati.add(a3);
							daIntervistare.remove(a3);
							cont++;
							queue.add(new Event(cont, EvenType.INTERVISTA));
						}
					}
				}
			}
			break;
		case PAUSA:
			if (cont == numGiorni) {
				return;
			}
			nPause++;
			cont++;
			flag = 1;
			queue.add(new Event(cont, EvenType.INTERVISTA));
			break;
		}
	}

	private Actor vicini(Actor ultimo) {
		Actor result = null;
		int peso = 0;
		for (Actor a : Graphs.neighborListOf(grafo, ultimo)) {
			if (grafo.getEdgeWeight(grafo.getEdge(ultimo, a)) > peso) {
				result = a;
				peso = (int) grafo.getEdgeWeight(grafo.getEdge(ultimo, a));
			}
		}
		return result;
	}

	public List<Actor> getIntervistati() {
		return intervistati;
	}

	public int getnPause() {
		return nPause;
	}

}
