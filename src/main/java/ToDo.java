import java.util.List;

public class ToDo {
    private String titolo;
    private String dataDiScadenza;
    private String url;
    private String immagine;
    private String descrizione;
    private String colore;
    private String posizione;
    private StatoToDo stato;
    private List<Utente> utentiCondivisi;

    public ToDo(String titolo, String dataDiScadenza, String url, String immagine,
                String descrizione, String colore, String posizione) {
        this.titolo = titolo;
        this.dataDiScadenza = dataDiScadenza;
        this.url = url;
        this.immagine = immagine;
        this.descrizione = descrizione;
        this.colore = colore;
        this.posizione = posizione;
        this.stato = StatoToDo.NonCompletato; // valore di default
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

    public String getPosizione() {
        return posizione;
    }

    public void setPosizione(String posizione) {
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

}

