package it.polito.tdp.genes.model;

public class Event implements Comparable<Event> {

	public enum EventType {
		STAY,
		OUT
	}
	
	private int nIng;
	private Genes geneFrom;
	private Genes geneTo;
	private EventType type;
	private int time;
	
	public Event(int nIng, Genes geneFrom, Genes geneTo,  EventType type, int time) {
		this.nIng = nIng;
		this.geneFrom = geneFrom;
		this.geneTo = geneTo;
		this.type = type;
		this.time = time;
	}

	public int getnIng() {
		return nIng;
	}

	public void setnIng(int nIng) {
		this.nIng = nIng;
	}

	public Genes getGeneFrom() {
		return geneFrom;
	}

	public void setGeneFrom(Genes geneFrom) {
		this.geneFrom = geneFrom;
	}

	public Genes getGeneTo() {
		return geneTo;
	}

	public void setGeneTo(Genes geneTo) {
		this.geneTo = geneTo;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((geneFrom == null) ? 0 : geneFrom.hashCode());
		result = prime * result + ((geneTo == null) ? 0 : geneTo.hashCode());
		result = prime * result + nIng;
		result = prime * result + time;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (geneFrom == null) {
			if (other.geneFrom != null)
				return false;
		} else if (!geneFrom.equals(other.geneFrom))
			return false;
		if (geneTo == null) {
			if (other.geneTo != null)
				return false;
		} else if (!geneTo.equals(other.geneTo))
			return false;
		if (nIng != other.nIng)
			return false;
		if (time != other.time)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public int compareTo(Event o) {
		return this.time-o.time;
	}

}
