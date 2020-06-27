package it.polito.tdp.ufo;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.ufo.model.AnnoAvvistamenti;
import it.polito.tdp.ufo.model.Model;
import it.polito.tdp.ufo.model.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<AnnoAvvistamenti> boxAnno;

    @FXML
    private ComboBox<State> boxStato;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleAnalizza(ActionEvent event) {
    	this.txtResult.clear();
    	
    	if(this.boxStato.getValue() == null) {
    		this.txtResult.appendText("Errore! Devi selezionare uno stato dall'apposito menu a tendina.\n");
    		return;
    	}
    	
    	State selezionato = this.boxStato.getValue();
    	List<State> precedenti = this.model.getStatiPrecedenti(selezionato);
    	List<State> successivi = this.model.getStatiSuccessivi(selezionato);
    	List<State> raggiungibili = this.model.getStatiRaggiungibili(selezionato);
    	
    	this.txtResult.appendText("Stati PRECEDENTI allo stato " + selezionato + ":\n");
    	for(State stato : precedenti) {
    		this.txtResult.appendText(stato.toString() + "\n");
    	}
    	this.txtResult.appendText("Stati SUCCESSIVI allo stato " + selezionato + ":\n");
    	for(State stato : successivi) {
    		this.txtResult.appendText(stato.toString() + "\n");
    	}
    	this.txtResult.appendText("Stati RAGGIUNGIBILI dallo stato " + selezionato + ":\n");
    	for(State stato : raggiungibili) {
    		this.txtResult.appendText(stato.toString() + "\n");
    	}
    	

    }

    @FXML
    void handleAvvistamenti(ActionEvent event) {
    	this.txtResult.clear();
    	
    	if(this.boxAnno.getValue() == null) {
    		this.txtResult.appendText("Errore! Devi selezionare un anno dall'apposito menu a tendina.\n");
    		return;
    	}

    	int anno = this.boxAnno.getValue().getAnno();
    	this.model.creaGrafo(anno);
    	
    	this.boxStato.getItems().clear();
    	this.boxStato.getItems().addAll(this.model.getVertici());
    	
    	this.txtResult.appendText("Grafo creato!\n");
    	this.txtResult.appendText("# VERTICI: " + this.model.nVertici() + "\n");
    	this.txtResult.appendText("# ARCHI: " + this.model.nArchi() + "\n\n");
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	this.txtResult.clear();
    	
    	if(this.boxStato.getValue() == null) {
    		this.txtResult.appendText("Errore! Devi selezionare uno stato dall'apposito menu a tendina.\n");
    		return;
    	}
    	
    	State partenza = this.boxStato.getValue();
    	
    	List<State> best = this.model.sequenzaAvvistamenti(partenza);
    	this.txtResult.appendText("Il cammino più lungo di avvistamenti successivi a partire dallo stato " + partenza + " è formato da:\n");
    	for(State stato : best) {
    		this.txtResult.appendText(stato + "\n");
    	}
    }

    @FXML
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxAnno.getItems().addAll(this.model.getAnniAvvistamenti());
	}
}
