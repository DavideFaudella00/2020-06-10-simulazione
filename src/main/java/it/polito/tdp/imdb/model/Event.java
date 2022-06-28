package it.polito.tdp.imdb.model;

public class Event implements Comparable<Event> {

	public enum EvenType {
		INTERVISTA, PAUSA
	}

	int nIntervista;
	EvenType tipo;

	public Event(int nIntervista, EvenType tipo) {
		super();
		this.nIntervista = nIntervista;
		this.tipo = tipo;
	}

	public int getnIntervista() {
		return nIntervista;
	}

	public void setnIntervista(int nIntervista) {
		this.nIntervista = nIntervista;
	}

	public EvenType getTipo() {
		return tipo;
	}

	public void setTipo(EvenType tipo) {
		this.tipo = tipo;
	}

	@Override
	public int compareTo(Event o) {
		return this.getnIntervista() - o.getnIntervista();
	}

}
