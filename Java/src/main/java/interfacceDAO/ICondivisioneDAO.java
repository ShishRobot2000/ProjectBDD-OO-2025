package interfacceDAO;

public interface ICondivisioneDAO {
    boolean condividi(String username, String proprietario, String tipoBacheca, String titoloToDo);
    boolean rimuoviCondivisione(String username, String proprietario, String tipoBacheca, String titoloToDo);
    boolean esisteCondivisione(String username, String proprietario, String tipoBacheca, String titoloToDo);
}

