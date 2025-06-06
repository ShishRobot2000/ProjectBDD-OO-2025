package interfacceDAO;

import model.ToDo;
import model.TipoBacheca;

import java.util.List;

public interface IToDoDAO {

    boolean salva(ToDo todo, String proprietario, TipoBacheca tipoBacheca);
    List<ToDo> trovaPerBacheca(String proprietario, TipoBacheca tipoBacheca);
    boolean aggiorna(ToDo todo, String proprietario, TipoBacheca tipoBacheca);
    boolean elimina(String titolo, String proprietario, TipoBacheca tipoBacheca);
}