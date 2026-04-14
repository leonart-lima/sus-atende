-- ==========================================
-- SEED DATA: Unidades de Saúde do SUS
-- ==========================================

-- UBS - Zona Sul
INSERT INTO unidades_saude (nome, endereco, telefone, tipo, capacidade_total_leitos, leitos_disponiveis)
VALUES ('UBS Zona Sul', 'Rua das Flores, 123 - Zona Sul', '(11) 3001-0001', 'UBS', 20, 18);

INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (1, 'Clínica Geral');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (1, 'Pediatria');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (1, 'Enfermagem');

-- UBS - Centro
INSERT INTO unidades_saude (nome, endereco, telefone, tipo, capacidade_total_leitos, leitos_disponiveis)
VALUES ('UBS Centro', 'Avenida Paulista, 1000 - Centro', '(11) 3001-0002', 'UBS', 25, 12);

INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (2, 'Clínica Geral');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (2, 'Odontologia');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (2, 'Vacinação');

-- Policlínica Regional
INSERT INTO unidades_saude (nome, endereco, telefone, tipo, capacidade_total_leitos, leitos_disponiveis)
VALUES ('Policlínica Regional Leste', 'Avenida Leste-Oeste, 2500 - Zona Leste', '(11) 3001-0003', 'POLICLINICA', 50, 35);

INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (3, 'Cardiologia');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (3, 'Dermatologia');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (3, 'Pneumologia');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (3, 'Gastroenterologia');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (3, 'Clínica Geral');

-- Pronto-Socorro Central
INSERT INTO unidades_saude (nome, endereco, telefone, tipo, capacidade_total_leitos, leitos_disponiveis)
VALUES ('Pronto-Socorro Central', 'Rua da Emergência, 500 - Centro', '(11) 3001-0004', 'PRONTO_SOCORRO', 100, 25);

INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (4, 'Emergência');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (4, 'Traumatologia');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (4, 'Clínica Geral');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (4, 'Cirurgia');

-- Hospital Geral
INSERT INTO unidades_saude (nome, endereco, telefone, tipo, capacidade_total_leitos, leitos_disponiveis)
VALUES ('Hospital Municipal Geral', 'Avenida Hospital, 5000 - Zona Norte', '(11) 3001-0005', 'HOSPITAL', 300, 50);

INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (5, 'Cirurgia');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (5, 'Clínica Médica');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (5, 'Cardiologia');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (5, 'Neurologia');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (5, 'Oncologia');

-- UBS - Zona Norte
INSERT INTO unidades_saude (nome, endereco, telefone, tipo, capacidade_total_leitos, leitos_disponiveis)
VALUES ('UBS Zona Norte', 'Rua do Bem-Estar, 850 - Zona Norte', '(11) 3001-0006', 'UBS', 22, 20);

INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (6, 'Clínica Geral');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (6, 'Pediatria');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (6, 'Pré-natal');

-- Policlínica Oeste
INSERT INTO unidades_saude (nome, endereco, telefone, tipo, capacidade_total_leitos, leitos_disponiveis)
VALUES ('Policlínica Oeste', 'Avenida Oeste, 3200 - Zona Oeste', '(11) 3001-0007', 'POLICLINICA', 60, 28);

INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (7, 'Oftalmologia');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (7, 'Otorrinolaringologia');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (7, 'Neurologia');
INSERT INTO unidades_saude_especialidades (unidade_saude_id, especialidades)
VALUES (7, 'Clínica Geral');

