package interfacceDAO;

import model.ToDo;
import model.TipoBacheca;

import java.util.List;

public interface IToDoDAO {

    // Salva un nuovo ToDo e aggiorna il suo ID dal DB
    boolean salva(ToDo todo, String proprietario, TipoBacheca tipoBacheca);

    // Ritorna tutti i ToDo associati a una bacheca
    List<ToDo> trovaPerBacheca(String proprietario, TipoBacheca tipoBacheca);

    // Aggiorna un ToDo usando il suo ID
    boolean aggiorna(ToDo todo, String proprietario, TipoBacheca tipoBacheca);

    // Elimina un ToDo usando il suo ID
    boolean elimina(int id);
}
