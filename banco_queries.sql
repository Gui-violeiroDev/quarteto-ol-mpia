-- ============================================================
-- QUARTETO OLYMPIA - Script de criação do banco de dados
-- Execute este script no MySQL/XAMPP antes de rodar o projeto
-- ============================================================

CREATE DATABASE IF NOT EXISTS quarteto_olympia
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE quarteto_olympia;

-- ============================================================
-- O Hibernate cria as tabelas automaticamente (ddl-auto=update)
-- As queries abaixo são para verificar os dados após o CRUD
-- ============================================================

-- Query 1: Ver todos os usuários cadastrados
SELECT id, nome, email, role, ativo, criado_em
FROM usuarios
ORDER BY criado_em DESC;

-- Query 2: Ver todos os pedidos com detalhes
SELECT
    p.id,
    p.nome_cliente,
    p.email_cliente,
    p.tipo_evento,
    p.data_evento,
    p.hora_evento,
    p.cidade_evento,
    p.estado_evento,
    p.tipo_formacao,
    p.valor_base,
    p.adicional_deslocamento,
    p.adicional_partituras,
    p.valor_total,
    p.status,
    p.criado_em
FROM pedidos p
ORDER BY p.criado_em DESC;

-- Query 3: Ver todas as partituras do acervo
SELECT id, nome_musica, nome_compositor, disponivel
FROM partituras
WHERE disponivel = 1
ORDER BY nome_musica;

-- Query 4: Ver todos os músicos
SELECT id, nome, instrumento, ativo
FROM musicos
ORDER BY instrumento, nome;

-- Query 5: Ver histórico de operações (CRUD)
SELECT
    h.id,
    h.operacao,
    h.descricao,
    h.tabela_afetada,
    h.registro_id,
    u.nome AS usuario,
    h.realizado_em
FROM historico_operacoes h
LEFT JOIN usuarios u ON u.id = h.usuario_id
ORDER BY h.realizado_em DESC;

-- Query 6: Resumo de pedidos por status
SELECT
    status,
    COUNT(*) AS quantidade,
    SUM(valor_total) AS valor_total_acumulado
FROM pedidos
GROUP BY status;

-- Query 7: Pedidos com partituras selecionadas
SELECT
    p.id AS pedido_id,
    p.nome_cliente,
    pt.nome_musica,
    pt.nome_compositor
FROM pedidos p
JOIN pedido_partituras pp ON pp.pedido_id = p.id
JOIN partituras pt ON pt.id = pp.partitura_id
ORDER BY p.id;

-- Query 8: Verificar disponibilidade de músicos por instrumento
SELECT
    m.nome,
    m.instrumento,
    COUNT(a.id) AS total_eventos
FROM musicos m
LEFT JOIN agenda_musicos a ON a.musico_id = m.id AND a.disponivel = 0
WHERE m.ativo = 1
GROUP BY m.id, m.nome, m.instrumento
ORDER BY m.instrumento;
