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

INSERT INTO public.tipobacheca VALUES ('Universita');
INSERT INTO public.tipobacheca VALUES ('Lavoro');
INSERT INTO public.tipobacheca VALUES ('TempoLibero');


--
-- Data for Name: utente; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.utente VALUES ('admin', 'adminpass');
INSERT INTO public.utente VALUES ('pippo', '1234');
INSERT INTO public.utente VALUES ('pluto', 'abcd');
INSERT INTO public.utente VALUES ('paperino', 'quack');


--
-- Data for Name: bacheca; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.bacheca VALUES ('Universita', 'Esami e lezioni', 'admin');
INSERT INTO public.bacheca VALUES ('Lavoro', 'Progetti di lavoro', 'admin');
INSERT INTO public.bacheca VALUES ('TempoLibero', 'Serie TV e hobby', 'admin');
INSERT INTO public.bacheca VALUES ('Universita', 'Appunti e orari', 'pippo');
INSERT INTO public.bacheca VALUES ('Lavoro', 'Stage e tirocinio', 'pippo');
INSERT INTO public.bacheca VALUES ('TempoLibero', 'Giochi e tempo libero', 'pippo');
INSERT INTO public.bacheca VALUES ('Universita', 'Corsi serali', 'pluto');
INSERT INTO public.bacheca VALUES ('Lavoro', 'Ufficio e pratiche', 'pluto');
INSERT INTO public.bacheca VALUES ('TempoLibero', 'Sport e videogiochi', 'pluto');


--
-- Data for Name: statotodo; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.statotodo VALUES ('Completato');
INSERT INTO public.statotodo VALUES ('NonCompletato');


--
-- Data for Name: todo; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.todo VALUES (1, 'Finire report', '2025-06-12', '', '', 'Scrivere la relazione finale', 'CCFFCC', 2, 'NonCompletato', 'admin', 'Lavoro');
INSERT INTO public.todo VALUES (2, 'Guardare serie', '2025-06-15', '', '', 'Finire Stranger Things', '99CCFF', 1, 'Completato', 'admin', 'TempoLibero');
INSERT INTO public.todo VALUES (3, 'Ripassare Java', '2025-06-11', '', '', 'Prepararsi per il test Java', 'FF9999', 1, 'NonCompletato', 'pippo', 'Universita');
INSERT INTO public.todo VALUES (4, 'Inviare CV', '2025-06-13', '', '', 'Candidarsi a stage', '66FF66', 1, 'Completato', 'pippo', 'Lavoro');
INSERT INTO public.todo VALUES (5, 'Allenamento serale', '2025-06-10', '', '', 'Palestra alle 19', 'CCCCFF', 1, 'NonCompletato', 'pluto', 'TempoLibero');
INSERT INTO public.todo VALUES (6, 'Studiare Basi di Dati', '2025-06-11', '', '', 'Ripassare i capitoli 4 e 5', 'FFDD99', 2, 'NonCompletato', 'admin', 'Universita');


--
-- Data for Name: condivisione; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.condivisione VALUES ('pippo', 6);
INSERT INTO public.condivisione VALUES ('pluto', 1);
INSERT INTO public.condivisione VALUES ('paperino', 3);


--
-- Name: todo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.todo_id_seq', 1, false);


--
-- PostgreSQL database dump complete
--

