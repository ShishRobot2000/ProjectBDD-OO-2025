package model;
import java.util.ArrayList;
import java.util.List;
public class Bacheca {
    private TipoBacheca tipo;
    private String descrizione;
    private List<ToDo> toDoList;

    public Bacheca(TipoBacheca tipo, String descrizione) {
        this.tipo = tipo;
        this.descrizione = descrizione;
        this.toDoList = new ArrayList<>(); // inizializza la lista
    }

    public TipoBacheca getTipo() {
        return tipo;
    }

    public void setTipo(TipoBacheca tipo) {
        this.tipo = tipo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public List<ToDo> getToDoList() {
        return toDoList;
    }

    public void setToDoList(List<ToDo> toDoList) {
        this.toDoList = toDoList;
    }

    @Override
    public String toString() {
        String testo = "Tipo: " + tipo + "\nDescrizione: " + descrizione;

        if (toDoList != null && !toDoList.isEmpty()) {
            testo += "\nToDo nella bacheca:";
            for (ToDo t : toDoList) {
                testo += "\n  - " + t.getTitolo();
            }
        } else {
            testo += "\nNessun ToDo presente.";
        }

        return testo;
    }

    public void aggiungiToDo(ToDo toDo) {
        this.toDoList.add(toDo);
    }

}

