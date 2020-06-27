package it.polito.tdp.ufo.model;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Model m = new Model();
		m.creaGrafo(1946);
		State partenza = new State("CA", "California", "Sacramento", 38.555605, -121.468926, 163695, 37253956);
		m.sequenzaAvvistamenti(partenza);
	}

}
