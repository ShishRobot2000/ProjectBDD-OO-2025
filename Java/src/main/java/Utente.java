import java.util.List;

public class Utente {
    private String username;
    private String password;
    private List<Bacheca> bacheche;
    private List<ToDo> toDoCondivisi;

    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
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
}

