drop database if exists gameet;
create database GaMeet;
use GaMeet;

create table Giochi
(
	id int primary key auto_increment,
    titolo varchar(100) not null unique,
    casa_produttrice varchar(100) not null,
    anno_uscita int not null,
    genere varchar(100) not null,
    piattaforme varchar(1000) not null
);

-- drop table Giochi;

create table Teams
(
	id int primary key auto_increment,
    nome varchar(100) not null unique,
    data_creazione date default (current_date),
    id_gioco int,
    foreign key (id_gioco) references Giochi(id)
    on update cascade
    on delete cascade
);

-- drop table Teams;

create table Utenti
(
	id int primary key auto_increment,
    nome varchar(100) not null,
    cognome varchar(100) not null,
    nickname varchar(100) not null unique,
    sesso char,
    nazione varchar(100) not null,
    id_team int,
    pro char default "n",
    email varchar(100) not null unique,
    pass varchar(100) not null,
    data_registrazione date default (current_date),
    adm char default "n",
    preferenze varchar(1000),
    avatar_url varchar(2000),
    foreign key (id_team) references Teams(ID)
    on update cascade
    on delete set null
);

-- drop table Utenti;

create table Amicizia
(
	id int primary key auto_increment,
    id_utente_1 int,
    id_utente_2 int,
    data_richiesta date not null default (current_date),
    data_accettata date,
    foreign key (id_utente_1) references Utenti(id)
    on update cascade
    on delete cascade,
    foreign key (id_utente_2) references Utenti(id)
    on update cascade
    on delete cascade
);

-- drop table Amicizia;

create table Utenti_Giochi
(
	id int primary key auto_increment,
    id_utente int,
    id_gioco int,
    punteggio double default 0,
    minuti_di_gioco int default 0,
    foreign key (id_utente) references Utenti(id)
    on update cascade
    on delete set null,
    foreign key (id_gioco) references Giochi(id)
    on update cascade
    on delete set null
);

create table Messaggi
(
	id int primary key auto_increment,
    from_user_id int,
    to_user_id int,
    contenuto varchar(1000) not null,
    data_messaggio date not null default (current_date),
    foreign key (from_user_id) references Utenti(ID)
    on update cascade
    on delete set null,
    foreign key (to_user_id) references Utenti(ID)
    on update cascade
    on delete set null
);

-- drop table Messaggi;

-- INSERT 
insert into Giochi(titolo,casa_produttrice,anno_uscita,genere,piattaforme) values
("Horizon Zero Dawn", "From", 2018, "Action", "PC,PS4"),
("Elden Ring", "FromSoftware", 2022, "Action", "PC/PS5"),
("Super Mario", "Nintendo", 1992, "Platform", "Gameboy"),
("World of Warcraft", "Blizzard", 2005, "MMORPG", "PC"),
("Diablo II", "Blizzard", 2002, "Action/RPG", "PC");

insert into Utenti (nome, cognome, nickname, sesso, nazione, pro, email, pass, data_registrazione, adm, preferenze, avatar_url)
values ("Michele", "Oliva", "Mic", "M", "Italia", "y", "mic@gmail.com", "micpass", "2022-01-10", "y", null, "https://cdn.gamerbrain.net/wp-content/uploads/2020/12/07111704/gaming.png
"),
("Luca", "Fioravanti", "Luke", "M", "Italia", "n", "luk@gmail.com", "lukpass", "2021-03-20", "y", null, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRt4mjA37M7Ho4FkyO9OuV1H9WWX4hOVuCQlw&usqp=CAU
"),
("Antonio", "Teora", "Anto", "M", "Italia", "y", "anto@gmail.com", "antopass", "2020-04-15", "y", null, "https://media.cdnandroid.com/item_images/1115169/imagen-avatar-gaming-logo-maker-esport-logo-ideas-0thumb.jpg
"),
("Jacopo", "Pallavidino", "Jack", "M", "Italia", "n", "jack@gmail.com", "jackpass", "2018-08-18", "y", null, "https://img.freepik.com/premium-vector/gamer-mascot-geek-boy-esports-logo-avatar-with-headphones-glasses-cartoon-character_8169-228.jpg
"),
("Alberto", "Perra", "Albe", "M", "Italia", "y", "albe@gmail.com", "albepass", "2015-06-22", "y", null, "https://media.istockphoto.com/vectors/anonymous-vector-icon-incognito-sign-privacy-concept-human-head-with-vector-id1284693553?k=20&m=1284693553&s=612x612&w=0&h=kduGGOGCXcl0PBkoyuxXrsHyFyd-Ct4LFQeXZtJug4Y=
");

insert into Teams(nome,id_gioco) values
("Kobra kai", 1),
("Dream Team", 2),
("ArteFatti", 3),
("GenerationZ", 4),
("SolinasTeam", 5);

insert into Messaggi(from_user_id,to_user_id,contenuto) values
(1,2, "Benvenuto nel nostro sito!"),
(2,3, "Ciao, ti va di giocare assieme?"),
(4,5, "Vuoi far parte di un team?"),
(3,1, "Oggi stacco prima, ci sentiamo presto!"),
(3,5, "Ohi tutto ok? Non ti vedo online da un po'...");

insert into Amicizia(id_utente_1, id_utente_2, data_accettata) values
(1,2, "2022-07-10"),
(2,3,null),
(3,4,null),
(4,5, "2022-05-13");

insert into utenti_giochi(id_utente, id_gioco) values
(1,1),
(1,2),
(1,3),
(2,1),
(2,2),
(2,3),
(3,4),
(3,3),
(4,2),
(4,5),
(5,1),
(5,2),
(5,3),
(5,4),
(5,5);


-- UPDATE

update Utenti set id_team = 3 where id = 1;
update Utenti set id_team = 3 where id = 3;
update Utenti set id_team = 5 where id = 5;

-- ritorna tutti gli id_utente (tranne me) che giocano a uno qualsisi dei miei giochi
select id_utente from utenti_giochi
where id_gioco in (select id_gioco from utenti_giochi where id_utente = 3) and id_utente != 3;

-- vediamo quanti giochi l'utente 5 ha in comune con l'utente 3
select count(*) as 'giochi in comune' from utenti_giochi where id_utente = 5
and id_gioco in (select id_gioco from utenti_giochi where id_utente = 3);

-- prende tutti gli  utenti (tranne me) che giocano a uno qualsiasi dei miei giochi
-- ma con almeno 3 giochi in comune
-- 3 è il mio id_utente (loggato)
select utenti.* from utenti_giochi as u inner join utenti on utenti.id=u.id_utente
where id_gioco in (select id_gioco from utenti_giochi where id_utente = 3) and id_utente != 3 
and (
	-- conta il numero di giochi che l'utente u.id_utente ha in comune con l'utente 3
	select count(*) from utenti_giochi where id_utente = u.id_utente
	and id_gioco in (select id_gioco from utenti_giochi where id_utente = 3)
) >= 3;

-- amici di 2
select nickname
from amicizia join utenti on id_utente_1=utenti.id or id_utente_2=utenti.id
where (id_utente_1=2 or id_utente_2=2) and utenti.id != 2 and data_accettata is not null;

-- lista di giochi ai quali gioca l'utente 2
SELECT giochi.*
FROM giochi INNER JOIN utenti_giochi ON giochi.id=utenti_giochi.id_gioco
WHERE id_utente = 2;


-- mostra gli utenti che giocano al gioco 2 con il punteggio più alto (DA IMPLEMENTARE)
select utenti.nome, punteggio
from utenti inner join utenti_giochi on id_utente=utenti.id
where id_gioco = 2 and utenti_giochi.punteggio = (
	select max(punteggio) from utenti_giochi where id_gioco=2
);

-- lista dei giochi ai quali giocano gli amici dell'utente 3
select distinct nickname
from giochi inner join utenti_giochi on id_gioco=giochi.id
			inner join utenti on id_utente=utenti.id
where utenti.id in (
	select utenti.id
    from utenti inner join amicizia on (id_utente_1=1 or id_utente_2=1)
    and data_accettata is not null
) and utenti.id != 1;

-- PRENDIAMO LE RICHIESTE DI AMICIZIE (NON ACCETTATE) TRA UTENTE 1 E 2
SELECT * FROM amicizia WHERE ((id_utente_1 = 1 AND id_utente_2 = 2) OR (id_utente_1 = 2 AND id_utente_2 = 1)) AND data_accettata IS NULL;

-- richieste non accettate verso l'utente 2
select id_utente_1, data_richiesta
from amicizia inner join utenti on utenti.id=amicizia.id_utente_2
where id_utente_2 = 4 and data_accettata is null;
        
-- numero di giochi in comune tra me e quaslsiasi altro utente
select id_utente from (
	select
	u.id as id_utente,
	(
		select count(*) from utenti_giochi where id_utente=1 and id_gioco in (
			select id_gioco from utenti_giochi where id_utente=u.id
		)
	) as gc
	from utenti as u where u.id != 1
) as t where gc >= 3;

-- elenco dei giochi che 2 utenti hanno in comune
-- solo se l'elenco >= 3 giochi in comune
	select giochi.* from utenti_giochi
	inner join utenti on utenti_giochi.id_utente=utenti.id
	inner join giochi on utenti_giochi.id_gioco=giochi.id 
	where utenti_giochi.id_gioco in (
		-- selezioniamo gli id dei giochi giocati da me
		select id_gioco from utenti_giochi where id_utente = 1
	) and utenti_giochi.id_utente in (
		select id_utente from (
			select
			u.id as id_utente,
			(
				select count(*) from utenti_giochi where id_utente=1 and id_gioco in (
					select id_gioco from utenti_giochi where id_utente=u.id
				)
			) as gc
			from utenti as u where u.id != 1
		) as t where gc >= 3
	);