package me.giuseppemilicia.mytodo.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Giuseppe on 20/02/2017.
 */

public class Nota implements Serializable {
    private static int notaId = 0;
    private int id = notaId++;
    private String titolo;
    private String corpo;
    private Date dataCreazione;
    private Date dataUltimaModifica;
    private Date dataScandenza;
    private Stato stato;

    public enum Stato {
        DA_FARE, ARCHIVIATA, COMPLETATA;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public Date getDataUltimaModifica() {
        return dataUltimaModifica;
    }

    public Nota() {
        this.dataCreazione = new Date();
        this.stato = Stato.DA_FARE;
    }

    public Nota(String titolo, String corpo) {
        this();
        this.titolo = titolo;
        this.corpo = corpo;
    }

    public int getId () {
        return id;
    }

    public Date getDataScandenza() {
        return dataScandenza;
    }

    public void setDataScandenza(Date dataScandenza) {
        this.dataScandenza = dataScandenza;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public Stato getStato() {
        return stato;
    }

    public void setStato(Stato stato) {
        this.stato = stato;
    }

}
