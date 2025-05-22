package service;

import model.Utente;
import model.Bacheca;
import model.ToDo;
import model.TipoBacheca;

import java.util.Map;
import java.util.HashMap;


public class AuthService {

    private static final Map<String, String> credenziali = new HashMap<>();
    private static final Map<String, Utente> utenti = new HashMap<>();

    static {
        // Aggiungiamo un utente di test: admin / 1234
        Utente admin = new Utente("admin", "1234");
        Bacheca b1 = new Bacheca(TipoBacheca.Università, "Studio");
        ToDo t1 = new ToDo("Esame Basi di Dati", "2025-06-10", "", "", "Studiare SQL", "FFDD88", "1");
        b1.aggiungiToDo(t1);

        Bacheca b2 = new Bacheca(TipoBacheca.Lavoro, "Task GUI");
        ToDo t2 = new ToDo("Fix bug GUI", "2025-05-25", "", "", "Correggere layout", "AAFFAA", "1");
        b2.aggiungiToDo(t2);

        Bacheca b3 = new Bacheca(TipoBacheca.TempoLibero, "Relax");
        ToDo t3 = new ToDo("Guardare film", "2025-06-01", "", "", "Vedere Dune Parte 2", "AACCFF", "1");
        b3.aggiungiToDo(t3);

        admin.aggiungiBacheca(b1);
        admin.aggiungiBacheca(b2);
        admin.aggiungiBacheca(b3);

        credenziali.put("admin", "1234");
        utenti.put("admin", admin);
    }

    // Verifica login
    public static boolean authenticate(String username, String password) {
        return credenziali.containsKey(username) && credenziali.get(username).equals(password);
    }

    // Ritorna l'oggetto Utente associato
    public static Utente getUtente(String username) {
        return utenti.get(username);
    }
}

