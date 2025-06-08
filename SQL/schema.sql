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
    id_todo integer NOT NULL
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
    immagine text,
    descrizione text,
    colore character varying(7),
    posizione integer,
    stato character varying(30) NOT NULL,
    proprietario character varying(100) NOT NULL,
    tipo_bacheca character varying(30) NOT NULL
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

