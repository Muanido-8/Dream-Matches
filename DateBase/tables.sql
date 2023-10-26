-- Criar e usar a bd
create database projecto_pooII;
use projecto_pooII;

-- Criando as tabelas
-- Tabela Users
CREATE TABLE `users` (
    `id` int not null primary key,
    `name` varchar(250) not null,
    `user_name` varchar(250) not null unique,
    `password` varchar(250) not null,
    `created_at` datetime not null
);


-- Tabela Campeonatos
CREATE TABLE `campeonatos` (
    `id` int not null primary key,
    `user_id` int not null,
    `place` varchar(250) not null,
    `min_idade` int default null,
    `max_idade` int default null,
    `gender` enum("Masculino", "Femenino") not null default "Masculino",
    `category` varchar(250) not null,
    `reward` varchar(250) default null,
    `img` varchar(250) default null,
    `max_jogadores` int not null,
    foreign key(`user_id`) references `users`(`id`)
);

-- Tabela Equipas
CREATE TABLE `teams` (
    `id` int not null primary key,
    `campeonato_id` int not null,
    `name` varchar(250) not null,
    `img` varchar(250) default null,
    `from` varchar(250) default null,
    foreign key(`campeonato_id`) references `campeonatos`(`id`)
);

-- Tabela de Jogadores
CREATE TABLE `players` (
    `id` int not null primary key,
    `team_id` int not null,
    `name` varchar(250) not null,
    `BI` varchar(50) not null,
    `birthday` date not null,
    `photo` varchar(250) not null,
    `position` enum("Guarda redes", "Meio campista", "Defesa", "Atacante") not null,
    `gender` enum("Masculino", "Femenino") not null default "Femenino",
    `shirt_number` int not null,
    foreign key(`team_id`) references `teams`(`id`)
);

-- Tabela de Treinadores
CREATE TABLE `coachs` (
    `id` int not null primary key,
    `team_id` int not null,
    `name` varchar(250) not null,
    `BI` varchar(50) not null,
    `photo` varchar(250) not null,
    `gender` enum("Masculino", "Femenino") not null default "Femenino",
    foreign key(`team_id`) references `teams`(`id`)
);

-- Tabela de Jogos
CREATE TABLE `plays` (
    `id` int not null primary key,
    `team_home` int not null,
    `team_visitor` int not null,
    `campo` varchar(250) not null,
    `img` varchar(250) default null,
    `play_time` datetime not null,
    `stage` int not null default 0,
    foreign key(`team_home`) references `teams`(`id`),
    foreign key(`team_visitor`) references `teams`(`id`)
);

-- Tabela de resultados
CREATE TABLE `results` (
    `id` int not null primary key,
    `play_id` int not null,
    `home` int not null default 0,
    `visitor` int not null default 0,
    `finish_time` datetime not null,
    `extra_info` text default null,
    foreign key(`play_id`) references `plays`(`id`)
);
