package it.polito.tdp.ufo.model;

public class Adiacenza {

	private State primo;
	private State secondo;

	public Adiacenza(State primo, State secondo) {
		super();
		this.primo = primo;
		this.secondo = secondo;
	}

	public State getPrimo() {
		return primo;
	}

	public void setPrimo(State primo) {
		this.primo = primo;
	}

	public State getSecondo() {
		return secondo;
	}

	public void setSecondo(State secondo) {
		this.secondo = secondo;
	}

}
