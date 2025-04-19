import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // 1. Creo un utente
        Utente pietro = new Utente("pietro98", "ciao123");

        // 2. Creo una bacheca
        Bacheca lavoro = new Bacheca(TipoBacheca.Lavoro, "Cose da fare per l'ufficio");

        // 3. Creo un ToDo (senza specificare lo stato → di default NonCompletato)
        ToDo task1 = new ToDo(
                "Inviare report",
                "2025-04-21",
                "http://azienda.com/report",
                null,
                "Inviare il report al capo entro domani",
                "giallo",
                "colonna 1"
        );

        // 4. Associo il ToDo alla bacheca
        List<ToDo> listaToDo = new ArrayList<>();
        listaToDo.add(task1);
        lavoro.setToDoList(listaToDo);

        // 5. Associo la bacheca all’utente
        List<Bacheca> bachechePietro = new ArrayList<>();
        bachechePietro.add(lavoro);
        pietro.setBacheche(bachechePietro);

        // 6. Condivido il ToDo con Pietro
        List<Utente> utentiCondivisi = new ArrayList<>();
        utentiCondivisi.add(pietro);
        task1.setUtentiCondivisi(utentiCondivisi);

        List<ToDo> toDoCondivisi = new ArrayList<>();
        toDoCondivisi.add(task1);
        pietro.setToDoCondivisi(toDoCondivisi);

        // 7. Stampiamo per vedere che funzioni
        System.out.println("Utente: " + pietro.getUsername());
        System.out.println("Bacheca: " + lavoro.getDescrizione());
        System.out.println("ToDo: " + task1.getTitolo());
        System.out.println("Stato: " + task1.getStato()); // deve stampare NonCompletato

        System.out.println("Dettagli ToDo:");
        System.out.println(task1);  // Questo usa toString()
    }
}

