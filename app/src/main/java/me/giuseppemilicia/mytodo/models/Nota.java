package me.giuseppemilicia.mytodo.models;

import android.graphics.Color;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Giuseppe on 20/02/2017.
 */

public class Nota {
    private long id;
    private String titolo;
    private String corpo;
    private Date dataCreazione;
    private Date dataUltimaModifica;
    private Date dataScandenza;
    private Stato stato;
    private boolean speciale;
    private int colore = Color.WHITE;
    private int posizione;

    public enum Stato {
        DA_FARE, ARCHIVIATA, COMPLETATA;
    }

    public Nota() {
        this.dataCreazione = new Date();
        this.dataUltimaModifica = this.dataCreazione;
        this.dataScandenza = this.dataCreazione;
        this.stato = Stato.DA_FARE;
    }

    public Nota(String titolo, String corpo) {
        this();
        this.titolo = titolo;
        this.corpo = corpo;
    }

    public int getPosizione() {
        return posizione;
    }

    public void setPosizione(int posizione) {
        this.posizione = posizione;
    }

    public boolean isSpeciale() {
        return speciale;
    }

    public void setSpeciale(boolean speciale) {
        this.speciale = speciale;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public void setDataUltimaModifica(Date dataUltimaModifica) {
        this.dataUltimaModifica = dataUltimaModifica;
    }

    public void setDataScandenza(Date dataScandenza) {
        this.dataScandenza = dataScandenza;
    }

    public void setDataCreazione(int timestamp) {
        this.dataCreazione = new Date(timestamp);
    }

    public void setDataUltimaModifica(int timestamp) {
        this.dataUltimaModifica.setTime(timestamp);
    }

    public void setDataScandenza(int timestamp) {
        this.dataScandenza.setTime(timestamp);
    }

    public int getColore() {
        return colore;
    }

    public void setColore(int colore) {
        this.colore = colore;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public Date getDataUltimaModifica() {
        return dataUltimaModifica;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Date getDataScandenza() {
        return dataScandenza;
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

    public void setStato(String stato) {
        this.stato = Stato.valueOf(stato);
    }

    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", titolo='" + titolo + '\'' +
                ", corpo='" + corpo + '\'' +
                ", dataCreazione=" + dataCreazione +
                ", dataUltimaModifica=" + dataUltimaModifica +
                ", dataScandenza=" + dataScandenza +
                ", stato=" + stato +
                ", speciale=" + speciale +
                ", colore=" + colore +
                ", posizione=" + posizione +
                '}';
    }
}
