package it.polito.tdp.genes.model;

public class Event implements Comparable<Event>{
	
	private int T ; // tempo
	private int nIng ; // numero ingegnere
	
	public Event(int t, int nIng) {
		super();
		T = t;
		this.nIng = nIng;
	}

	public int getT() {
		return T;
	}

	public int getnIng() {
		return nIng;
	}
	
	@Override
	public int compareTo(Event other) {
		return this.T-other.T ;
	}
	

}