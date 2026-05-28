<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <title>Quarteto Olympia</title>
</head>
<body>
    <header> 
    <nav>
        <a href="#Eventos">Eventos</a>
        <a href="pag2.php" target="_blank" title="Biografia">Biografia</a>
        <a href="pag3.php" target="_blank"  title="Fotos">Galeria</a>
        <a href="pag4.php" target="_blank" title="Desenvolvedor">Desenvolvedor</a>
        <a href="pag5.php" target="_blank" title="Orçamento">Orçamento</a>
        
    </nav>
    <br>
    <figure>
        <img src="img/Logo.png" alt="Logo">
    </figure>
    </header>
    
<section class="banner">
    <img id="slide" src="img/imagem1.jpg" alt="Banner Olympia">
</section>
<script>
    let imagens = [
    "img/imagem1.jpg",
    "img/imagem2.jpg",
    "img/imagem3.jpg",
    ];
    
    let index = 0;
    let slide = document.getElementById("slide");

    function trocarImagem() {
    index++;
    if (index >= imagens.length) {
        index = 0;    
    }
    slide.style.opacity = 0; // fade-out
    setTimeout (() => {
       slide.src = imagens[index];
       slide.style.opacity = 1; //fade-in 
}, 500);
}
setInterval(trocarImagem, 5000); // troca a cada 3s
</script>
<br></br>

<p class="paragrafo"> A música é a essência da emoção, capaz de tornar<br></br> momentos em experiências que permanecem na memória.</p> 


<br>
<a href="#" id="Eventos"></a>
<br>
<section class="conteudo" id="grid-conteudo">
<h5>Eventos</h5> 
<br>
<div class="grid">

    <div><figure>
        <img class="imagens-site" src="img/capelinha.jpg" alt="imagem capela">  
        <figcaption>Concerto-Capelinha-Arujá</figcaption>
    </figure>
</div>
    <div><figure>
        <img class="imagens-site" src="img/Concerto de Natal-Condominio.jpg" alt="imagem Concerto de natal "> 
        <figcaption>Natal-Condominio</figcaption> 
    </figure>
</div>
    <div><figure>
        <img class="imagens-site" src="img/casamento.jpg" alt="imagem de casamento"> 
        <figcaption>Casamento</figcaption> 
    </figure>
</div>
    <div><figure>
        <img class="imagens-site" src="img/bandolim.jpg" alt="imagem Quarteto e bandolim">  
        <figcaption>Show-Ney Marques</figcaption>
    </figure>
</div>
    <div><figure>
        <img class="imagens-site" src="img/angra.jpg" alt="imagem Show do angra">  
        <figcaption>Show - Angra</figcaption>
    </figure>
</div>
    <div><figure>
        <img class="imagens-site" src="img/Haras-2.jpg" alt= "Concerto ao ar livre"> 
        <figcaption>Concerto ao ar ivre</figcaption> 
    </figure>
</div>
</div>
</section>
<br>
<a href="pag5.php"> <h5>Clique Aqui e Faça seu Orçamento</h5> </a>
    <footer> 
        <br>
        <h3>Quarteto Olympia</h3>
        
        <p>Contato: quartetolympia@hotmail.com</p>
        <p>WhatsApp:(11) 953550550 - Diego Pereira</p>
        <br><br>
    </footer>
    
</body>
</html>


