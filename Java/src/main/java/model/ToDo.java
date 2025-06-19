package model;

import java.util.ArrayList;
import java.util.List;

public class ToDo {
    private int id;
    private String titolo;
    private String dataDiScadenza;
    private String url;
    private byte[] immagine;
    private String descrizione;
    private String colore;
    private int posizione;
    private StatoToDo stato;
    private String proprietario;
    private List<Utente> utentiCondivisi;
    private TipoBacheca tipoBacheca;

    // Costruttore principale
    public ToDo(String titolo, String dataDiScadenza, String url, byte[] immagine,
                String descrizione, String colore) {
        this.titolo = titolo;
        this.dataDiScadenza = dataDiScadenza;
        this.url = url;
        this.immagine = immagine;
        this.descrizione = descrizione;
        this.colore = colore;
        this.stato = StatoToDo.NonCompletato;
        this.utentiCondivisi = new ArrayList<>();
        this.posizione = 0;
        this.id = -1;
        this.proprietario = null;
    }

    // Costruttore semplificato
    public ToDo(String titolo) {
        this(titolo, "", "", null, "", "FFFFFF");
    }

    // Getter e setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getDataDiScadenza() { return dataDiScadenza; }
    public void setDataDiScadenza(String dataDiScadenza) { this.dataDiScadenza = dataDiScadenza; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public byte[] getImmagine() { return immagine; }
    public void setImmagine(byte[] immagine) { this.immagine = immagine; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getColore() { return colore; }
    public void setColore(String colore) { this.colore = colore; }

    public int getPosizione() { return posizione; }
    public void setPosizione(int posizione) { this.posizione = posizione; }

    public StatoToDo getStato() { return stato; }
    public void setStato(StatoToDo stato) { this.stato = stato; }

    public String getProprietario() { return proprietario; }
    public void setProprietario(String proprietario) { this.proprietario = proprietario; }

    public List<Utente> getUtentiCondivisi() { return utentiCondivisi; }
    public void setUtentiCondivisi(List<Utente> utentiCondivisi) { this.utentiCondivisi = utentiCondivisi; }

    public TipoBacheca getTipoBacheca() { return tipoBacheca; }
    public void setTipoBacheca(TipoBacheca tipoBacheca) { this.tipoBacheca = tipoBacheca; }

    // Metodi funzionali
    public void condividiCon(Utente utente) {
        if (this.utentiCondivisi == null) this.utentiCondivisi = new ArrayList<>();
        if (!this.utentiCondivisi.contains(utente)) this.utentiCondivisi.add(utente);

        if (utente.getToDoCondivisi() == null) utente.setToDoCondivisi(new ArrayList<>());
        if (!utente.getToDoCondivisi().contains(this)) utente.getToDoCondivisi().add(this);
    }

    public void rimuoviCondivisionePer(Utente utente) {
        if (utentiCondivisi != null) utentiCondivisi.remove(utente);
        if (utente.getToDoCondivisi() != null) utente.getToDoCondivisi().remove(this);
    }

    public void segnaCompletato() {
        this.stato = StatoToDo.Completato;
    }

    public void segnaNonCompletato() {
        this.stato = StatoToDo.NonCompletato;
    }
}




