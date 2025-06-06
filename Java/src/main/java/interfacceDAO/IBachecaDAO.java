package interfacceDAO;

import model.Bacheca;
import model.TipoBacheca;

import java.util.List;

public interface IBachecaDAO {

    boolean salvaBacheca(Bacheca bacheca, String proprietario);
    List<Bacheca> findByUtente(String username);
    Bacheca findByTipo(String username, TipoBacheca tipo);
}

