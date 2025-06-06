package model;


import java.util.ArrayList;
import java.util.List;


// Definisce la classe e gli attributi
public class ToDo {
    private String titolo;
    private String dataDiScadenza;
    private String url;
    private String immagine;
    private String descrizione;
    private String colore;
    private int posizione;
    private StatoToDo stato;
    private List<Utente> utentiCondivisi;
    

    // Costruttore per inizializzare gli attributi
    public ToDo(String titolo, String dataDiScadenza, String url, String immagine,
                String descrizione, String colore) {
        this.titolo = titolo;
        this.dataDiScadenza = dataDiScadenza;
        this.url = url;
        this.immagine = immagine;
        this.descrizione = descrizione;
        this.colore = colore;
        this.stato = StatoToDo.NonCompletato;
        this.utentiCondivisi = new ArrayList<>();
        this.posizione = 0; // valore segnaposto, sarà sovrascritto dal DAO
    }


    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDataDiScadenza() {
        return dataDiScadenza;
    }

    public void setDataDiScadenza(String dataDiScadenza) {
        this.dataDiScadenza = dataDiScadenza;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public int getPosizione() {
        return posizione;
    }

    public void setPosizione(int posizione) {
        this.posizione = posizione;
    }

    public StatoToDo getStato() {
        return stato;
    }

    public void setStato(StatoToDo stato) {
        this.stato = stato;
    }

    public List<Utente> getUtentiCondivisi() {
        return utentiCondivisi;
    }

    public void setUtentiCondivisi(List<Utente> utentiCondivisi) {
        this.utentiCondivisi = utentiCondivisi;
    }


    @Override
    public String toString() {
        return "Titolo: " + titolo +
                "\nData di Scadenza: " + dataDiScadenza +
                "\nURL: " + (url != null ? url : "Nessun link") +
                "\nImmagine: " + (immagine != null ? immagine : "Nessuna") +
                "\nDescrizione: " + (descrizione != null ? descrizione : "Nessuna descrizione") +
                "\nColore: " + (colore != null ? colore : "Nessun colore") +
                "\nPosizione: " + (posizione != 0 ? posizione : "Nessuna posizione") +
                "\nStato: " + stato +
                "\nUtenti Condivisi: " + (utentiCondivisi != null && !utentiCondivisi.isEmpty()
                ? utentiCondivisi.stream().map(Utente::getUsername).toList()
                : "Nessuno");
    }


    // Aggiunta temporanea per l'utilizzo di ToDo in DashboardPanel
    public ToDo(String titolo) {
    this(titolo, "", "", "", "", "FFFFFF");
}

    // Serve per condividere il ToDo con un utente
    public void condividiCon(Utente utente) {
        if (this.utentiCondivisi == null) {
            this.utentiCondivisi = new ArrayList<>();
        }
        if (!this.utentiCondivisi.contains(utente)) {
            this.utentiCondivisi.add(utente);
        }

        if (utente.getToDoCondivisi() == null) {
            utente.setToDoCondivisi(new ArrayList<>());
        }
        if (!utente.getToDoCondivisi().contains(this)) {
            utente.getToDoCondivisi().add(this);
        }
    }
}

