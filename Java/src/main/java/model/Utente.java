package model;
import java.util.ArrayList;
import java.util.List;

public class Utente {
    private String username;
    private String password;
    private List<Bacheca> bacheche;
    private List<ToDo> toDoCondivisi;

    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
        this.bacheche = new ArrayList<>();
        this.toDoCondivisi = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Bacheca> getBacheche() {
        return bacheche;
    }

    public void setBacheche(List<Bacheca> bacheche) {
        this.bacheche = bacheche;
    }

    public List<ToDo> getToDoCondivisi() {
        return toDoCondivisi;
    }

    public void setToDoCondivisi(List<ToDo> toDoCondivisi) {
        this.toDoCondivisi = toDoCondivisi;
    }

    @Override
    public String toString() {
        String toDoString = "ToDo Condivisi: ";

        if (toDoCondivisi != null && !toDoCondivisi.isEmpty()) {
            for (ToDo t : toDoCondivisi) {
                toDoString += "\n  - " + t.getTitolo();
            }
        } else {
            toDoString += "Nessuno";
        }
        return "Username: " + username + "\n" + toDoString;
    }


    public void aggiungiBacheca(Bacheca bacheca) {
        this.bacheche.add(bacheca);
    }

    public void aggiungiToDoCondiviso(ToDo toDo) {
        if (!this.toDoCondivisi.contains(toDo)) {
            this.toDoCondivisi.add(toDo);
        }
    }

}

