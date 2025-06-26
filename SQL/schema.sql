--
-- PostgreSQL database dump
--

-- Dumped from database version 17.0
-- Dumped by pg_dump version 17.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: aggiorna_stato_condivisione(text, text, text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.aggiorna_stato_condivisione(p_destinatario text, p_proprietario text, p_tipo_bacheca text, p_titolo text, p_nuovo_stato text) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_id INTEGER;
BEGIN
    SELECT id INTO v_id
    FROM todo
    WHERE proprietario = p_proprietario AND tipo_bacheca = p_tipo_bacheca AND titolo = p_titolo;

    IF NOT FOUND THEN
        RETURN FALSE;
    END IF;

    IF UPPER(p_nuovo_stato) = 'ACCEPTED' THEN
        UPDATE condivisione
        SET stato = 'ACCEPTED'
        WHERE username_utente = p_destinatario AND id_todo = v_id;
        RETURN FOUND;

    ELSIF UPPER(p_nuovo_stato) = 'REJECTED' THEN
        DELETE FROM condivisione
        WHERE username_utente = p_destinatario AND id_todo = v_id AND stato = 'PENDING';
        RETURN FOUND;

    ELSE
        RAISE EXCEPTION 'Stato non valido: %', p_nuovo_stato;
    END IF;
END;
$$;


ALTER FUNCTION public.aggiorna_stato_condivisione(p_destinatario text, p_proprietario text, p_tipo_bacheca text, p_titolo text, p_nuovo_stato text) OWNER TO postgres;

--
-- Name: aggiorna_todo(integer, text, text, text, text, text, text, bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.aggiorna_todo(p_id integer, p_titolo text, p_descrizione text, p_data_scadenza text, p_colore text, p_stato text, p_url text, p_immagine bytea, p_posizione integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
BEGIN
    UPDATE todo
    SET titolo = p_titolo,
        descrizione = p_descrizione,
        data_scadenza = p_data_scadenza,
        colore = p_colore,
        stato = p_stato,
        url = p_url,
        immagine = p_immagine,
        posizione = p_posizione
    WHERE id = p_id;

    RETURN FOUND;
END;
$$;


ALTER FUNCTION public.aggiorna_todo(p_id integer, p_titolo text, p_descrizione text, p_data_scadenza text, p_colore text, p_stato text, p_url text, p_immagine bytea, p_posizione integer) OWNER TO postgres;

--
-- Name: check_bacheche_standard(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.check_bacheche_standard() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    n_bacheche INTEGER;
BEGIN
    -- Confronta direttamente con gli ENUM Java usati in tipobacheca
    IF NEW.tipo NOT IN ('UNIVERSITA', 'LAVORO', 'TEMPO_LIBERO') THEN
        RAISE EXCEPTION 'Tipo bacheca non valido: % (consentiti: UNIVERSITA, LAVORO, TEMPO_LIBERO)', NEW.tipo;
    END IF;

    -- Controlla se l'utente ha gi… questo tipo
    SELECT COUNT(*) INTO n_bacheche
    FROM bacheca
    WHERE proprietario = NEW.proprietario AND tipo = NEW.tipo;

    IF n_bacheche > 0 THEN
        RAISE EXCEPTION 'L''utente % ha gi… una bacheca di tipo %', NEW.proprietario, NEW.tipo;
    END IF;

    RETURN NEW;
END;
$$;


ALTER FUNCTION public.check_bacheche_standard() OWNER TO postgres;

--
-- Name: condividi_todo(text, text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.condividi_todo(p_destinatario text, p_proprietario text, p_tipo_bacheca text, p_titolo text) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_id INTEGER;
BEGIN
    SELECT id INTO v_id
    FROM todo
    WHERE proprietario = p_proprietario AND tipo_bacheca = p_tipo_bacheca AND titolo = p_titolo;

    IF NOT FOUND THEN
        RETURN FALSE;
    END IF;

    INSERT INTO condivisione (username_utente, id_todo, stato)
    VALUES (p_destinatario, v_id, 'PENDING');

    RETURN TRUE;
EXCEPTION WHEN OTHERS THEN
    RETURN FALSE;
END;
$$;


ALTER FUNCTION public.condividi_todo(p_destinatario text, p_proprietario text, p_tipo_bacheca text, p_titolo text) OWNER TO postgres;

--
-- Name: crea_bacheche_standard(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.crea_bacheche_standard() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    INSERT INTO bacheca (tipo, descrizione, proprietario)
    VALUES
        ('UNIVERSITA', 'Bacheca Universit…', NEW.username),
        ('LAVORO', 'Bacheca Lavoro', NEW.username),
        ('TEMPO_LIBERO', 'Bacheca Tempo Libero', NEW.username);
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.crea_bacheche_standard() OWNER TO postgres;

--
-- Name: default_stato_todo(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.default_stato_todo() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.stato IS NULL THEN
        NEW.stato := 'NON_COMPLETATO';
    END IF;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.default_stato_todo() OWNER TO postgres;

--
-- Name: elimina_condivisioni_collegate(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.elimina_condivisioni_collegate(p_id_todo integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM condivisione WHERE id_todo = p_id_todo;
    RETURN TRUE;
END;
$$;


ALTER FUNCTION public.elimina_condivisioni_collegate(p_id_todo integer) OWNER TO postgres;

--
-- Name: elimina_todo(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.elimina_todo(p_id integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM todo WHERE id = p_id;
    RETURN FOUND;
END;
$$;


ALTER FUNCTION public.elimina_todo(p_id integer) OWNER TO postgres;

--
-- Name: elimina_utente(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.elimina_utente(p_username text) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    id_todo INTEGER;
BEGIN
    -- 1. Elimina condivisioni ricevute
    DELETE FROM condivisione WHERE username_utente = p_username;

    -- 2. Elimina condivisioni fatte (dopo recupero id)
    FOR id_todo IN SELECT id FROM todo WHERE proprietario = p_username LOOP
        DELETE FROM condivisione WHERE id_todo = id_todo;
    END LOOP;

    -- 3. Elimina ToDo
    DELETE FROM todo WHERE proprietario = p_username;

    -- 4. Elimina bacheche
    DELETE FROM bacheca WHERE proprietario = p_username;

    -- 5. Elimina utente
    DELETE FROM utente WHERE username = p_username;

    RETURN TRUE;
EXCEPTION WHEN OTHERS THEN
    RETURN FALSE;
END;
$$;


ALTER FUNCTION public.elimina_utente(p_username text) OWNER TO postgres;

--
-- Name: esiste_condivisione(text, text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.esiste_condivisione(p_destinatario text, p_proprietario text, p_tipo_bacheca text, p_titolo text) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_exists BOOLEAN;
BEGIN
    SELECT EXISTS (
        SELECT 1
        FROM condivisione c
        JOIN todo t ON c.id_todo = t.id
        WHERE c.username_utente = p_destinatario
        AND t.proprietario = p_proprietario
        AND t.tipo_bacheca = p_tipo_bacheca
        AND t.titolo = p_titolo
    ) INTO v_exists;

    RETURN v_exists;
END;
$$;


ALTER FUNCTION public.esiste_condivisione(p_destinatario text, p_proprietario text, p_tipo_bacheca text, p_titolo text) OWNER TO postgres;

--
-- Name: esiste_utente(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.esiste_utente(p_username text) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    exists BOOLEAN;
BEGIN
    SELECT EXISTS (
        SELECT 1 FROM utente WHERE username = TRIM(p_username)
    ) INTO exists;

    RETURN exists;
END;
$$;


ALTER FUNCTION public.esiste_utente(p_username text) OWNER TO postgres;

--
-- Name: get_bacheca_by_tipo(character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_bacheca_by_tipo(p_username character varying, p_tipo character varying) RETURNS TABLE(tipo character varying, descrizione text, proprietario character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT tipo, descrizione, proprietario
    FROM bacheca
    WHERE proprietario = p_username AND tipo = p_tipo;
END;
$$;


ALTER FUNCTION public.get_bacheca_by_tipo(p_username character varying, p_tipo character varying) OWNER TO postgres;

--
-- Name: get_bacheche_by_utente(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_bacheche_by_utente(p_username character varying) RETURNS TABLE(tipo character varying, descrizione text, proprietario character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT tipo, descrizione, proprietario
    FROM bacheca
    WHERE proprietario = p_username;
END;
$$;


ALTER FUNCTION public.get_bacheche_by_utente(p_username character varying) OWNER TO postgres;

--
-- Name: get_utente_by_credenziali(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_utente_by_credenziali(p_username text, p_password text) RETURNS TABLE(username text, password text)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT username, password
    FROM utente
    WHERE username = p_username AND password = p_password;
END;
$$;


ALTER FUNCTION public.get_utente_by_credenziali(p_username text, p_password text) OWNER TO postgres;

--
-- Name: get_utente_by_username(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_utente_by_username(p_username text) RETURNS TABLE(username text, password text)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT username, password
    FROM utente
    WHERE username = TRIM(p_username);
END;
$$;


ALTER FUNCTION public.get_utente_by_username(p_username text) OWNER TO postgres;

--
-- Name: mostra_funzioni(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mostra_funzioni() RETURNS TABLE(nome text, firma text)
    LANGUAGE sql
    AS $$
  SELECT p.proname::text AS nome,
         pg_get_function_identity_arguments(p.oid) AS firma
  FROM pg_proc p
  JOIN pg_namespace n ON p.pronamespace = n.oid
  WHERE n.nspname = 'public'
    AND pg_function_is_visible(p.oid);
$$;


ALTER FUNCTION public.mostra_funzioni() OWNER TO postgres;

--
-- Name: richieste_pendenti_per_utente(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.richieste_pendenti_per_utente(p_username text) RETURNS TABLE(richiedente text, tipo_bacheca text, titolo text)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT t.proprietario, t.tipo_bacheca, t.titolo
    FROM condivisione c
    JOIN todo t ON c.id_todo = t.id
    WHERE c.username_utente = p_username AND c.stato = 'PENDING';
END;
$$;


ALTER FUNCTION public.richieste_pendenti_per_utente(p_username text) OWNER TO postgres;

--
-- Name: rimuovi_condivisione(text, text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.rimuovi_condivisione(p_destinatario text, p_proprietario text, p_tipo_bacheca text, p_titolo text) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_id INTEGER;
BEGIN
    SELECT id INTO v_id
    FROM todo
    WHERE proprietario = p_proprietario AND tipo_bacheca = p_tipo_bacheca AND titolo = p_titolo;

    IF NOT FOUND THEN
        RETURN FALSE;
    END IF;

    DELETE FROM condivisione
    WHERE username_utente = p_destinatario AND id_todo = v_id;

    RETURN FOUND;
END;
$$;


ALTER FUNCTION public.rimuovi_condivisione(p_destinatario text, p_proprietario text, p_tipo_bacheca text, p_titolo text) OWNER TO postgres;

--
-- Name: rimuovi_richiesta_pendente(text, text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.rimuovi_richiesta_pendente(p_destinatario text, p_proprietario text, p_tipo_bacheca text, p_titolo text) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_id INTEGER;
BEGIN
    SELECT id INTO v_id
    FROM todo
    WHERE proprietario = p_proprietario AND tipo_bacheca = p_tipo_bacheca AND titolo = p_titolo;

    IF NOT FOUND THEN
        RETURN FALSE;
    END IF;

    DELETE FROM condivisione
    WHERE username_utente = p_destinatario AND id_todo = v_id AND stato = 'PENDING';

    RETURN FOUND;
END;
$$;


ALTER FUNCTION public.rimuovi_richiesta_pendente(p_destinatario text, p_proprietario text, p_tipo_bacheca text, p_titolo text) OWNER TO postgres;

--
-- Name: salva_bacheca(character varying, text, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.salva_bacheca(p_tipo character varying, p_descrizione text, p_proprietario character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
BEGIN
    INSERT INTO bacheca (tipo, descrizione, proprietario)
    VALUES (p_tipo, p_descrizione, p_proprietario);
    RETURN TRUE;
EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END;
$$;


ALTER FUNCTION public.salva_bacheca(p_tipo character varying, p_descrizione text, p_proprietario character varying) OWNER TO postgres;

--
-- Name: salva_todo(text, text, text, text, text, text, bytea, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.salva_todo(p_titolo text, p_descrizione text, p_data_scadenza text, p_colore text, p_stato text, p_url text, p_immagine bytea, p_proprietario text, p_tipo_bacheca text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    new_id INTEGER;
BEGIN
    -- Shift delle posizioni esistenti
    UPDATE todo
    SET posizione = posizione + 1
    WHERE proprietario = p_proprietario AND tipo_bacheca = p_tipo_bacheca;

    -- Inserimento nuovo ToDo
    INSERT INTO todo (
        titolo, descrizione, data_scadenza, colore, stato,
        url, immagine, posizione, proprietario, tipo_bacheca
    )
    VALUES (
        p_titolo, p_descrizione, p_data_scadenza, p_colore, p_stato,
        p_url, p_immagine, 1, p_proprietario, p_tipo_bacheca
    )
    RETURNING id INTO new_id;

    RETURN new_id;
END;
$$;


ALTER FUNCTION public.salva_todo(p_titolo text, p_descrizione text, p_data_scadenza text, p_colore text, p_stato text, p_url text, p_immagine bytea, p_proprietario text, p_tipo_bacheca text) OWNER TO postgres;

--
-- Name: salva_utente(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.salva_utente(p_username text, p_password text) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
BEGIN
    INSERT INTO utente (username, password)
    VALUES (LOWER(TRIM(p_username)), p_password);
    RETURN TRUE;
EXCEPTION
    WHEN unique_violation THEN
        RAISE NOTICE 'Utente gi… esistente: %', LOWER(TRIM(p_username));
        RETURN FALSE;
    WHEN OTHERS THEN
        RAISE NOTICE 'Errore generico durante salva_utente: %', SQLERRM;
        RETURN FALSE;
END;
$$;


ALTER FUNCTION public.salva_utente(p_username text, p_password text) OWNER TO postgres;

--
-- Name: todo_condivisi_con(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.todo_condivisi_con(p_username text) RETURNS TABLE(id integer, titolo text, data_scadenza text, url text, immagine bytea, descrizione text, colore text, posizione integer, stato text, proprietario text, tipo_bacheca text)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT t.id, t.titolo, t.data_scadenza, t.url, t.immagine, t.descrizione,
           t.colore, t.posizione, t.stato, t.proprietario, t.tipo_bacheca
    FROM condivisione c
    JOIN todo t ON c.id_todo = t.id
    WHERE c.username_utente = p_username AND c.stato = 'ACCEPTED'
    ORDER BY t.tipo_bacheca, t.posizione;
END;
$$;


ALTER FUNCTION public.todo_condivisi_con(p_username text) OWNER TO postgres;

--
-- Name: trova_todo_per_bacheca(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.trova_todo_per_bacheca(p_proprietario text, p_tipo_bacheca text) RETURNS TABLE(id integer, titolo text, descrizione text, data_scadenza text, colore text, stato text, url text, immagine bytea, posizione integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT id, titolo, descrizione, data_scadenza, colore, stato, url, immagine, posizione
    FROM todo
    WHERE proprietario = p_proprietario AND tipo_bacheca = p_tipo_bacheca
    ORDER BY posizione ASC;
END;
$$;


ALTER FUNCTION public.trova_todo_per_bacheca(p_proprietario text, p_tipo_bacheca text) OWNER TO postgres;

--
-- Name: utenti_condivisi(text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.utenti_condivisi(p_proprietario text, p_tipo_bacheca text, p_titolo text) RETURNS TABLE(username text)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT c.username_utente
    FROM condivisione c
    JOIN todo t ON c.id_todo = t.id
    WHERE t.proprietario = p_proprietario AND t.tipo_bacheca = p_tipo_bacheca AND t.titolo = p_titolo;
END;
$$;


ALTER FUNCTION public.utenti_condivisi(p_proprietario text, p_tipo_bacheca text, p_titolo text) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: bacheca; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bacheca (
    tipo character varying(30) NOT NULL,
    descrizione text,
    proprietario character varying(100) NOT NULL
);


ALTER TABLE public.bacheca OWNER TO postgres;

--
-- Name: condivisione; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.condivisione (
    username_utente character varying(100) NOT NULL,
    id_todo integer NOT NULL,
    stato character varying(20) DEFAULT 'PENDING'::character varying NOT NULL,
    CONSTRAINT condivisione_stato_check CHECK (((stato)::text = ANY ((ARRAY['PENDING'::character varying, 'ACCEPTED'::character varying])::text[])))
);


ALTER TABLE public.condivisione OWNER TO postgres;

--
-- Name: statotodo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.statotodo (
    nome character varying(30) NOT NULL
);


ALTER TABLE public.statotodo OWNER TO postgres;

--
-- Name: tipobacheca; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipobacheca (
    nome character varying(30) NOT NULL
);


ALTER TABLE public.tipobacheca OWNER TO postgres;

--
-- Name: todo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.todo (
    id integer NOT NULL,
    titolo character varying(100) NOT NULL,
    data_scadenza character varying(20),
    url text,
    immagine bytea,
    descrizione text,
    colore character varying(7),
    posizione integer,
    stato character varying(30) NOT NULL,
    proprietario character varying(100) NOT NULL,
    tipo_bacheca character varying(30) NOT NULL,
    CONSTRAINT stato_todo_check CHECK (((stato)::text = ANY ((ARRAY['COMPLETATO'::character varying, 'NON_COMPLETATO'::character varying])::text[])))
);


ALTER TABLE public.todo OWNER TO postgres;

--
-- Name: todo_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.todo_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.todo_id_seq OWNER TO postgres;

--
-- Name: todo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.todo_id_seq OWNED BY public.todo.id;


--
-- Name: utente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.utente (
    username character varying(100) NOT NULL,
    password character varying(100) NOT NULL
);


ALTER TABLE public.utente OWNER TO postgres;

--
-- Name: todo id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.todo ALTER COLUMN id SET DEFAULT nextval('public.todo_id_seq'::regclass);


--
-- Name: bacheca bacheca_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bacheca
    ADD CONSTRAINT bacheca_pkey PRIMARY KEY (proprietario, tipo);


--
-- Name: condivisione condivisione_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.condivisione
    ADD CONSTRAINT condivisione_pkey PRIMARY KEY (username_utente, id_todo);


--
-- Name: statotodo statotodo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.statotodo
    ADD CONSTRAINT statotodo_pkey PRIMARY KEY (nome);


--
-- Name: tipobacheca tipobacheca_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipobacheca
    ADD CONSTRAINT tipobacheca_pkey PRIMARY KEY (nome);


--
-- Name: todo todo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.todo
    ADD CONSTRAINT todo_pkey PRIMARY KEY (id);


--
-- Name: utente utente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (username);


--
-- Name: bacheca trg_check_bacheche; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_check_bacheche BEFORE INSERT ON public.bacheca FOR EACH ROW EXECUTE FUNCTION public.check_bacheche_standard();


--
-- Name: utente trg_crea_bacheche; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_crea_bacheche AFTER INSERT ON public.utente FOR EACH ROW EXECUTE FUNCTION public.crea_bacheche_standard();


--
-- Name: todo trg_default_stato; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_default_stato BEFORE INSERT ON public.todo FOR EACH ROW EXECUTE FUNCTION public.default_stato_todo();


--
-- Name: bacheca bacheca_proprietario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bacheca
    ADD CONSTRAINT bacheca_proprietario_fkey FOREIGN KEY (proprietario) REFERENCES public.utente(username);


--
-- Name: bacheca bacheca_tipo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bacheca
    ADD CONSTRAINT bacheca_tipo_fkey FOREIGN KEY (tipo) REFERENCES public.tipobacheca(nome);


--
-- Name: condivisione condivisione_id_todo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.condivisione
    ADD CONSTRAINT condivisione_id_todo_fkey FOREIGN KEY (id_todo) REFERENCES public.todo(id) ON DELETE CASCADE;


--
-- Name: condivisione condivisione_username_utente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.condivisione
    ADD CONSTRAINT condivisione_username_utente_fkey FOREIGN KEY (username_utente) REFERENCES public.utente(username);


--
-- Name: todo todo_proprietario_tipo_bacheca_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.todo
    ADD CONSTRAINT todo_proprietario_tipo_bacheca_fkey FOREIGN KEY (proprietario, tipo_bacheca) REFERENCES public.bacheca(proprietario, tipo);


--
-- Name: todo todo_stato_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.todo
    ADD CONSTRAINT todo_stato_fkey FOREIGN KEY (stato) REFERENCES public.statotodo(nome);


--
-- PostgreSQL database dump complete
--

