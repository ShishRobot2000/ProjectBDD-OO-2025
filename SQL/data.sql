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
-- Data for Name: tipobacheca; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tipobacheca (nome) FROM stdin;
Universita
Lavoro
TempoLibero
\.


--
-- Data for Name: utente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.utente (username, password) FROM stdin;
admin	adminpass
pippo	1234
pluto	abcd
paperino	quack
\.


--
-- Data for Name: bacheca; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bacheca (tipo, descrizione, proprietario) FROM stdin;
Universita	Esami e lezioni	admin
Lavoro	Progetti di lavoro	admin
TempoLibero	Serie TV e hobby	admin
Universita	Appunti e orari	pippo
Lavoro	Stage e tirocinio	pippo
TempoLibero	Giochi e tempo libero	pippo
Universita	Corsi serali	pluto
Lavoro	Ufficio e pratiche	pluto
TempoLibero	Sport e videogiochi	pluto
\.


--
-- Data for Name: statotodo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.statotodo (nome) FROM stdin;
Completato
NonCompletato
\.


--
-- Data for Name: todo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.todo (titolo, data_scadenza, url, immagine, descrizione, colore, posizione, stato, proprietario, tipo_bacheca) FROM stdin;
Finire report	2025-06-12			Scrivere la relazione finale	CCFFCC	2	NonCompletato	admin	Lavoro
Guardare serie	2025-06-15			Finire Stranger Things	99CCFF	1	Completato	admin	TempoLibero
Ripassare Java	2025-06-11			Prepararsi per il test Java	FF9999	1	NonCompletato	pippo	Universita
Inviare CV	2025-06-13			Candidarsi a stage	66FF66	1	Completato	pippo	Lavoro
Allenamento serale	2025-06-10			Palestra alle 19	CCCCFF	1	NonCompletato	pluto	TempoLibero
Studiare Basi di Dati	2025-06-11			Ripassare i capitoli 4 e 5	FFDD99	2	NonCompletato	admin	Universita
\.


--
-- Data for Name: condivisione; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.condivisione (username_utente, proprietario_todo, tipo_bacheca_todo, titolo_todo) FROM stdin;
pippo	admin	Universita	Studiare Basi di Dati
pluto	admin	Lavoro	Finire report
paperino	pippo	Universita	Ripassare Java
\.


--
-- PostgreSQL database dump complete
--

