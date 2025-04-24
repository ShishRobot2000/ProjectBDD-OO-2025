import java.util.List;

public class Main {
    public static void main(String[] args) {

        // 1. Creo utenti
        Utente pietro = new Utente("pietro98", "ciao123");
        Utente mario = new Utente("mario89", "password456");

        // 2. Creo una bacheca
        Bacheca lavoro = new Bacheca(TipoBacheca.Lavoro, "Cose da fare per l'ufficio");

        // 3. Creo ToDo
        ToDo task1 = new ToDo(
                "Inviare report",
                "2025-04-22",
                "http://azienda.com/report",
                null,
                "Inviare il report al capo entro domani",
                "giallo",
                "colonna 1"
        );

        ToDo task2 = new ToDo(
                "Prenotare sala riunioni e ordinare il caffè",
                "2025-04-25",
                "http://azienda.com/riunione",
                null,
                "Prenotare la sala per la riunione del team",
                "blu",
                "colonna 2"
        );

        // 4. Aggiungo i ToDo alla bacheca direttamente
        lavoro.aggiungiToDo(task1);
        lavoro.aggiungiToDo(task2);

        // 5. Associo la bacheca a Pietro
        pietro.aggiungiBacheca(lavoro);

        // 6. Condivido i ToDo con gli utenti
        task1.condividiCon(pietro);
        task1.condividiCon(mario);
        task2.condividiCon(pietro);

        // 7. Output
        System.out.println("-----------------------------");
        System.out.println("Utente:\n" + pietro);
        System.out.println("-----------------------------");
        System.out.println("Bacheca:\n" + lavoro);
        System.out.println("-----------------------------");
        System.out.println("ToDo 1 dettagli:\n" + task1);
        System.out.println("-----------------------------");
        System.out.println("ToDo 2 dettagli:\n" + task2);
        System.out.println("-----------------------------");

        System.out.println("Elenco ToDo nella bacheca:");
        for (ToDo t : lavoro.getToDoList()) {
            System.out.println("-----------------------------");
            System.out.println("Titolo: " + t.getTitolo());
            System.out.println("Data di Scadenza: " + t.getDataDiScadenza());
            System.out.println("Stato: " + t.getStato());
        }

    }
}


