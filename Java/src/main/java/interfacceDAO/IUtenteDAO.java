package interfacceDAO;

import model.Utente;
import java.util.List;

public interface IUtenteDAO {

    boolean salvaUtente(Utente utente); // INSERT
    Utente findByUsernameAndPassword(String username, String password); // login
    Utente findByUsername(String username); // SELECT by username
    boolean eliminaUtente(String username); // DELETE
}


