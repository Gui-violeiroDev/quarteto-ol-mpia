# 🎻 Quarteto Olympia — Sistema de Gerenciamento de Pedidos

Sistema acadêmico desenvolvido com Java + Spring Boot, HTML/CSS/JS e MySQL.

---

## 🛠️ Tecnologias

| Camada    | Tecnologia                          |
|-----------|-------------------------------------|
| Backend   | Java 17 + Spring Boot 3.2           |
| Segurança | Spring Security + JWT               |
| Banco     | MySQL (XAMPP)                       |
| Frontend  | HTML5 + CSS3 + JavaScript (puro)    |
| Docs      | Swagger / SpringDoc OpenAPI         |
| Testes    | JUnit 5 + Mockito                   |
| IDE       | IntelliJ IDEA                       |
| API Test  | Insomnia                            |

---

## ⚙️ Pré-requisitos

- Java 17+
- Maven 3.8+
- XAMPP (MySQL rodando na porta 3306)
- IntelliJ IDEA

---

## 🚀 Passo a Passo para Rodar

### 1. Iniciar o XAMPP
Abra o XAMPP Control Panel e inicie o **MySQL**.

### 2. Criar o banco de dados
Acesse `http://localhost/phpmyadmin` e execute:
```sql
CREATE DATABASE IF NOT EXISTS quarteto_olympia
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

### 3. Abrir o projeto no IntelliJ
- File → Open → selecione a pasta `quarteto-olympia`
- Aguarde o Maven baixar as dependências

### 4. Rodar a aplicação
- Abra `QuartetoOlympiaApplication.java`
- Clique no botão ▶️ Run
- O Hibernate criará as tabelas automaticamente
- O `DataInitializer` populará admin, músicos e 79 partituras

### 5. Acessar o sistema
| URL                                          | Descrição                     |
|----------------------------------------------|-------------------------------|
| `http://localhost:8080`                      | Site público (index.html)     |
| `http://localhost:8080/pages/login.html`     | Login / Cadastro              |
| `http://localhost:8080/pages/orcamento.html` | Solicitar orçamento           |
| `http://localhost:8080/pages/pedidos.html`   | Meus pedidos                  |
| `http://localhost:8080/pages/admin.html`     | Painel Admin                  |
| `http://localhost:8080/swagger-ui.html`      | Documentação da API (Swagger) |

---

## 👤 Usuários de Teste

| Email                     | Senha    | Perfil |
|---------------------------|----------|--------|
| admin@olympia.com         | admin123 | Admin  |
| guilherme@olympia.com     | senha123 | User   |

---

## 🎻 Formações e Valores

| Formação          | Composição                          | Valor     |
|-------------------|-------------------------------------|-----------|
| Duo               | 1 Violino + 1 Cello                 | R$ 3.000  |
| Trio              | 1 Violino + 1 Viola + 1 Cello       | R$ 4.000  |
| Quarteto          | 2 Violinos + 1 Viola + 1 Cello      | R$ 5.000  |
| Quarteto + Piano  | 2 Violinos + 1 Viola + 1 Cello + Piano | R$ 6.000 |

**Custos adicionais:**
- Evento fora de São Paulo: **+ R$ 800,00**
- Partitura fora do acervo: **+ R$ 150,00 cada**

---

## 🧪 Rodando os Testes

No IntelliJ, clique com botão direito na pasta `src/test` → Run All Tests

Ou via terminal:
```bash
mvn test
```

**12 testes unitários** cobrindo:
- Criação de pedido em SP (sem adicional)
- Adicional de R$800 fora de SP
- Custo de R$150 por partitura nova
- Pedido não encontrado (exceção)
- Listagem por email do cliente
- Atualização de status
- Notificação ao admin (músicos indisponíveis)
- Valores corretos por formação (Duo, Trio, Quarteto, Quarteto+Piano)
- Cancelamento de pedido
- Registro de histórico
- Combinação de adicionais

---

## 🔌 Testando com Insomnia

### 1. Login
```
POST http://localhost:8080/api/auth/login
Body: { "email": "admin@olympia.com", "senha": "admin123" }
```
Copie o `token` da resposta.

### 2. Configurar autenticação
Em cada requisição: Auth → Bearer Token → cole o token.

### 3. CRUD de Pedido

**Criar pedido:**
```
POST http://localhost:8080/api/pedidos
Body:
{
  "nomeCliente": "João Silva",
  "emailCliente": "joao@email.com",
  "telefoneCliente": "(11) 99999-9999",
  "tipoEvento": "Casamento",
  "dataEvento": "2026-08-15",
  "horaEvento": "16:00",
  "enderecoEvento": "Rua das Flores, 100",
  "cidadeEvento": "São Paulo",
  "estadoEvento": "SP",
  "cepEvento": "01310-100",
  "tipoFormacao": "QUARTETO",
  "partituraIds": [1, 2, 3],
  "qtdPartiurasNovas": 0
}
```

**Listar pedidos:** `GET http://localhost:8080/api/pedidos`

**Buscar por ID:** `GET http://localhost:8080/api/pedidos/1`

**Atualizar status:** `PUT http://localhost:8080/api/pedidos/1/status?status=CONFIRMADO`

**Cancelar:** `DELETE http://localhost:8080/api/pedidos/1`

---

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/olympia/
│   │   ├── config/          # SecurityConfig, SwaggerConfig, DataInitializer
│   │   ├── controller/      # AuthController, PedidoController, PartituraController...
│   │   ├── dto/             # request/ e response/
│   │   ├── entity/          # Usuario, Pedido, Partitura, Musico, AgendaMusico, Historico
│   │   ├── enums/           # Role, StatusPedido, TipoFormacao, TipoInstrumento
│   │   ├── exception/       # GlobalExceptionHandler, exceptions customizadas
│   │   ├── repository/      # Interfaces JPA
│   │   ├── security/        # JWT (JwtUtil, JwtAuthFilter, UserDetailsService)
│   │   └── service/impl/    # AuthService, PedidoService, PartituraService, AgendaService, EmailService
│   └── resources/
│       ├── application.properties
│       └── static/          # Frontend HTML/CSS/JS
│           ├── index.html
│           ├── css/style.css
│           ├── js/api.js
│           └── pages/       # login, orcamento, pedidos, admin, biografia, galeria
└── test/
    └── java/com/olympia/service/
        ├── PedidoServiceTest.java   # 12 testes
        └── PartituraServiceTest.java # 3 testes
```

---

## 🏗️ Arquitetura em Camadas (MVC)

```
Controller → Service → Repository → Entity (BD)
     ↑            ↑
   DTOs        Regras de negócio
```

- **Controller**: recebe requisições HTTP, valida input, retorna resposta
- **Service**: regras de negócio (cálculo de valores, disponibilidade, histórico)
- **Repository**: acesso ao banco via Spring Data JPA
- **Entity**: mapeamento das tabelas do MySQL

---

## 🔐 Segurança

- Senhas criptografadas com BCrypt
- Autenticação via JWT (Bearer Token)
- Roles: `ROLE_USER` e `ROLE_ADMIN`
- Endpoints protegidos por anotações `@PreAuthorize`

---

## 📊 Queries para Apresentação (Requisito Acadêmico)

Após fazer login, criar, listar, atualizar e cancelar pedidos via sistema ou Insomnia,
execute as queries do arquivo `banco_queries.sql` no phpMyAdmin para exibir os dados:

```sql
-- Ver pedidos criados
SELECT id, nome_cliente, tipo_formacao, valor_total, status, criado_em
FROM pedidos ORDER BY criado_em DESC;

-- Ver histórico de operações CRUD
SELECT operacao, descricao, tabela_afetada, realizado_em
FROM historico_operacoes ORDER BY realizado_em DESC;
```
