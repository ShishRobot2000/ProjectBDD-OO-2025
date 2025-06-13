package interfacceDAO;

import java.util.List;

public interface ICondivisioneDAO {
    boolean condividi(String username, String proprietario, String tipoBacheca, String titoloToDo);
    boolean rimuoviCondivisione(String username, String proprietario, String tipoBacheca, String titoloToDo);
    boolean esisteCondivisione(String username, String proprietario, String tipoBacheca, String titoloToDo);

    // Nuovi metodi per gestire le richieste di condivisione
    List<String[]> getRichiestePendentiPerUtente(String proprietario);
    boolean aggiornaStatoRichiesta(String username, String proprietario, String tipoBacheca, String titoloToDo, String nuovoStato);
     boolean rimuoviRichiesta(String username, String proprietario, String tipoBacheca, String titoloToDo);
}
