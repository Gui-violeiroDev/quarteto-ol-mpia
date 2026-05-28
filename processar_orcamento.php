<?php

require_once('conexao.php');

if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    header("Location: pag5.php?status=erro");
    exit();
}



$nome = trim($_POST['nome'] ?? '');
$email = trim($_POST['email'] ?? '');
$telefone = trim($_POST['telefone'] ?? '');
$data_evento = $_POST['DataEvento'] ?? '';
$hora_evento = $_POST['hora'] ?? '';
$tipo_evento_nome = $_POST['evento'] ?? '';
$endereco = trim($_POST['endereco'] ?? '');
$cep = trim($_POST['cep'] ?? '');
$cidade = trim($_POST['cidade'] ?? '');
$estado = $_POST['estado'] ?? '';
$duvidas = trim($_POST['duvida'] ?? '');


if (empty($nome) || empty($email) || empty($data_evento) || 
    empty($tipo_evento_nome) || empty($endereco) || empty($cep) || 
    empty($telefone) || empty($hora_evento) || empty($cidade) || empty($estado)) {
    
    header("Location: pag5.php?status=erro");
    exit();
}


if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    header("Location: pag5.php?status=erro");
    exit();
}

$objDb = new db();
$link = $objDb->conecta_mysql();

if (!$link) {
    header("Location: pag5.php?status=erro");
    exit();
}

mysqli_begin_transaction($link);

try {
    

    $sql_verifica_cliente = "SELECT id_cliente FROM Cliente WHERE email = ?";
    $stmt = mysqli_prepare($link, $sql_verifica_cliente);
    
    if (!$stmt) {
        throw new Exception("Erro ao preparar consulta de cliente.");
    }
    
    mysqli_stmt_bind_param($stmt, 's', $email);
    mysqli_stmt_execute($stmt);
    $resultado = mysqli_stmt_get_result($stmt);
    
    if (mysqli_num_rows($resultado) > 0) {
        // Cliente já existe
        $row = mysqli_fetch_assoc($resultado);
        $id_cliente = $row['id_cliente'];
        mysqli_stmt_close($stmt);
    } else {
        // Insere novo cliente
        mysqli_stmt_close($stmt);
        
        $sql_cliente = "INSERT INTO Cliente (nome, email) VALUES (?, ?)";
        $stmt = mysqli_prepare($link, $sql_cliente);
        
        if (!$stmt) {
            throw new Exception("Erro ao preparar inserção de cliente.");
        }
        
        mysqli_stmt_bind_param($stmt, 'ss', $nome, $email);
        
        if (!mysqli_stmt_execute($stmt)) {
            throw new Exception("Erro ao cadastrar cliente: " . mysqli_stmt_error($stmt));
        }
        
        $id_cliente = mysqli_insert_id($link);
        mysqli_stmt_close($stmt);
    }
    

    $sql_tipo = "SELECT id_tipo, preco_base FROM tipo_evento WHERE nome_evento = ?";
    $stmt = mysqli_prepare($link, $sql_tipo);
    
    if (!$stmt) {
        throw new Exception("Erro ao preparar consulta de tipo de evento.");
    }
    
    mysqli_stmt_bind_param($stmt, 's', $tipo_evento_nome);
    mysqli_stmt_execute($stmt);
    $resultado = mysqli_stmt_get_result($stmt);
    
    if (mysqli_num_rows($resultado) == 0) {
        throw new Exception("Tipo de evento não encontrado.");
    }
    
    $tipo_evento = mysqli_fetch_assoc($resultado);
    $id_tipo = $tipo_evento['id_tipo'];
    $valor_base = $tipo_evento['preco_base'];
    mysqli_stmt_close($stmt);
    

    $data_solicitacao = date('Y-m-d');
    $valor_total = $valor_base; // Pode adicionar cálculos extras aqui
    
    // Insere orçamento com fk_Event_id_evento = 0 (será atualizado depois)
    $sql_orcamento = "INSERT INTO Orcamento 
                      (status, valor_base, valor_total, data_solicitacao, fk_Event_id_evento) 
                      VALUES ('Pendente', ?, ?, ?, 0)";
    
    $stmt = mysqli_prepare($link, $sql_orcamento);
    
    if (!$stmt) {
        throw new Exception("Erro ao preparar inserção de orçamento.");
    }
    
    mysqli_stmt_bind_param($stmt, 'dds', $valor_base, $valor_total, $data_solicitacao);
    
    if (!mysqli_stmt_execute($stmt)) {
        throw new Exception("Erro ao criar orçamento: " . mysqli_stmt_error($stmt));
    }
    
    $id_orcamento = mysqli_insert_id($link);
    mysqli_stmt_close($stmt);
 
    $sql_evento = "INSERT INTO Evento 
                   (data_evento, hora_evento, endereco, cidade, estado, cep, 
                    fk_cliente_id_cliente, fk_tipo_id_tipo, fk_orcamento_id_orcamento) 
                   VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    $stmt = mysqli_prepare($link, $sql_evento);
    
    if (!$stmt) {
        throw new Exception("Erro ao preparar inserção de evento.");
    }
    
    mysqli_stmt_bind_param($stmt, 'sssssssii', 
        $data_evento, $hora_evento, $endereco, $cidade, $estado, $cep, 
        $id_cliente, $id_tipo, $id_orcamento
    );
    
    if (!mysqli_stmt_execute($stmt)) {
        throw new Exception("Erro ao cadastrar evento: " . mysqli_stmt_error($stmt));
    }
    
    $id_evento = mysqli_insert_id($link);
    mysqli_stmt_close($stmt);

    $sql_update_orcamento = "UPDATE Orcamento 
                             SET fk_Event_id_evento = ? 
                             WHERE id_orcamento = ?";
    
    $stmt = mysqli_prepare($link, $sql_update_orcamento);
    
    if (!$stmt) {
        throw new Exception("Erro ao preparar atualização de orçamento.");
    }
    
    mysqli_stmt_bind_param($stmt, 'ii', $id_evento, $id_orcamento);
    
    if (!mysqli_stmt_execute($stmt)) {
        throw new Exception("Erro ao atualizar orçamento: " . mysqli_stmt_error($stmt));
    }
    
    mysqli_stmt_close($stmt);
    

    mysqli_commit($link);
    
    // Fechar conexão
    mysqli_close($link);
    
    // Redirecionar com sucesso
    header("Location: pag5.php?status=sucesso");
    exit();
    
} catch (Exception $e) {
    

    mysqli_rollback($link);
    mysqli_close($link);
    
    // Log do erro (opcional - descomente para debug)
    // error_log("Erro no orçamento: " . $e->getMessage());
    
    // Redirecionar com erro
    header("Location: pag5.php?status=erro");
    exit();
}

?>