<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <title>Orçamento</title>
    
</head>
<body>
    <header>
    <nav>
        <a href="index.php" title="Principal">Olympia</a>
        <a href="pag2.php" title="Biografia">Biografia</a>
        <a href="pag3.php" title="Fotos">Galeria</a>
        <a href="pag4.php" title="Desenvolvedor">Desenvolvedor</a>
    </nav>
    <br>
    <figure>
        <img src="img/Logo.png" alt="Logo">
    </figure>
    </header>
    <br>

    <section class="conteudo">
        <h1>A música se comunica com todos através de sua bela melodia, sendo única. Entre em contato conosco, forneceremos o melhor atendimento e iremos esclarecer todas suas dúvidas!</h1>
        <br>
        <h2>Solicitar Orçamento</h2>
        <br>
    <div class="formulario">

<form action="processar_orcamento.php" method="post">

    <div class="form-row">
        <div class="form-group">
            <label>Nome Completo: <span class="required">*</span></label>
            <input type="text" name="nome" required minlength="3">
        </div>

        <div class="form-group">
            <label>Email: <span class="required">*</span></label>
            <input type="email" name="email" required>
        </div>
    </div>

    <div class="form-row">
        <div class="form-group">
            <label>Telefone: <span class="required">*</span></label>
            <input type="text" name="telefone" id="telefone" required placeholder="(11) 91234-5678">
        </div>
        
        <div class="form-group">
            <label>Data do Evento: <span class="required">*</span></label>
            <input type="date" name="DataEvento" id="dataEvento" required>
        </div>
    </div>

    <div class="form-row">
        <div class="form-group">
            <label>Hora do Evento: <span class="required">*</span></label>
            <input type="time" name="hora" required>
        </div>

        <div class="form-group">
            <label for="evento">Tipo de Evento: <span class="required">*</span></label>
            <select name="evento" id="evento" required>
                <option value="">Selecione...</option>
                <option value="Casamento">Casamento</option>
                <option value="Aniversário">Aniversário</option>
                <option value="Bodas">Bodas</option>
                <option value="Corporativo">Corporativo</option>
                <option value="Evento Beneficente">Evento Beneficente</option>
                <option value="Outro">Outro</option>
            </select>
        </div>
    </div>

    <div class="form-group full-width">
        <label>Endereço do Evento: <span class="required">*</span></label>
        <input type="text" name="endereco" required placeholder="Rua, número e complemento">
    </div>

    <div class="form-row">
        <div class="form-group">
            <label>CEP: <span class="required">*</span></label>
            <input type="text" name="cep" id="cep" required placeholder="12345-678">
        </div>

        <div class="form-group">
            <label>Cidade: <span class="required">*</span></label>
            <input type="text" name="cidade" required>
        </div>
    </div>

    <div class="form-group">
        <label>Estado: <span class="required">*</span></label>
        <select name="estado" id="estado" required>
            <option value="">Selecione...</option>
            <option value="AC">Acre</option>
            <option value="AL">Alagoas</option>
            <option value="AP">Amapá</option>
            <option value="AM">Amazonas</option>
            <option value="BA">Bahia</option>
            <option value="CE">Ceará</option>
            <option value="DF">Distrito Federal</option>
            <option value="ES">Espírito Santo</option>
            <option value="GO">Goiás</option>
            <option value="MA">Maranhão</option>
            <option value="MT">Mato Grosso</option>
            <option value="MS">Mato Grosso do Sul</option>
            <option value="MG">Minas Gerais</option>
            <option value="PA">Pará</option>
            <option value="PB">Paraíba</option>
            <option value="PR">Paraná</option>
            <option value="PE">Pernambuco</option>
            <option value="PI">Piauí</option>
            <option value="RJ">Rio de Janeiro</option>
            <option value="RN">Rio Grande do Norte</option>
            <option value="RS">Rio Grande do Sul</option>
            <option value="RO">Rondônia</option>
            <option value="RR">Roraima</option>
            <option value="SC">Santa Catarina</option>
            <option value="SP">São Paulo</option>
            <option value="SE">Sergipe</option>
            <option value="TO">Tocantins</option>
        </select>
    </div>

    <div class="form-group full-width">
        <label>Informações Adicionais / Dúvidas:</label>
        <textarea name="duvida" placeholder="Conte-nos mais sobre seu evento, músicas especiais que gostaria de ouvir, etc."></textarea>
    </div>

    <input type="submit" value="SOLICITAR ORÇAMENTO">

</form>

<script>
    // Data mínima é hoje
    const hoje = new Date().toISOString().split('T')[0];
    document.getElementById('dataEvento').min = hoje;

    // Máscara para Telefone
    document.getElementById('telefone').addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length <= 10) {
            value = value.replace(/(\d{2})(\d{4})(\d{4})/, '($1) $2-$3');
        } else {
            value = value.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
        }
        e.target.value = value;
    });

    // Máscara para CEP
    document.getElementById('cep').addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        value = value.replace(/^(\d{5})(\d{0,3})/, '$1-$2');
        e.target.value = value;
    });

    // Buscar endereço pelo CEP
    document.getElementById('cep').addEventListener('blur', async function() {
        const cep = this.value.replace(/\D/g, '');
        if (cep.length === 8) {
            try {
                const response = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
                const data = await response.json();
                
                if (!data.erro) {
                    document.querySelector('input[name="endereco"]').value = data.logradouro;
                    document.querySelector('input[name="cidade"]').value = data.localidade;
                    document.getElementById('estado').value = data.uf;
                }
            } catch (error) {
                console.error('Erro ao buscar CEP:', error);
            }
        }
    });
</script>
    </div>
    </section>

<footer>
<h3>Quarteto Olympia</h3>
<p>Contato: quartetolympia@hotmail.com</p>
<p>WhatsApp:(11) 953550550 - Diego Pereira</p>
<br><br>
</footer>
    
</body>
</html>