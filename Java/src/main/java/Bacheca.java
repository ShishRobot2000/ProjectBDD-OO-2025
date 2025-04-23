import java.util.List;
public class Bacheca {
    private TipoBacheca tipo;
    private String descrizione;
    private List<ToDo> toDoList;

    public Bacheca(TipoBacheca tipo, String descrizione) {
        this.tipo = tipo;
        this.descrizione = descrizione;
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
}

