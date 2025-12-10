# 📚 LES E-commerce - Backend API

API REST completa para e-commerce de livros desenvolvida como projeto da disciplina de Laboratório de Engenharia de Software (LES).

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-Academic-yellow)]()

## 📋 Sobre o Projeto

Sistema completo de e-commerce especializado em livros com funcionalidades avançadas de gestão de clientes, pedidos, estoque, cupons de desconto, sistema de trocas e recomendações inteligentes por IA.

## 🚀 Tecnologias Utilizadas

### Core
- **Java 17** - Linguagem de programação
- **Spring Boot 3.5.5** - Framework principal
- **Spring Data JPA** - Persistência de dados e ORM
- **Hibernate** - Implementação JPA
- **PostgreSQL** - Banco de dados principal
- **H2 Database** - Banco de dados para testes

### Documentação e Validação
- **SpringDoc OpenAPI 2.8.9** - Documentação automática (Swagger)
- **Spring Validation** - Validação de dados

### Integrações
- **Google Gemini API 2.5 Flash** - IA para recomendações de livros

### Ferramentas
- **Lombok** - Redução de boilerplate
- **Spring DevTools** - Hot reload em desenvolvimento
- **Thymeleaf** - Template engine
- **Maven** - Gerenciamento de dependências

## 📦 Pré-requisitos

- Java JDK 17 ou superior
- Maven 3.6 ou superior
- PostgreSQL 12 ou superior
- Chave de API do Google Gemini (para funcionalidade de chatbot)

## 🔧 Instalação e Configuração

### 1. Clone o repositório
```bash
git clone <url-do-repositorio>
cd les-ecommerce-backend
```

### 2. Configure o banco de dados
Crie um banco de dados PostgreSQL:
```sql
CREATE DATABASE les_ecommerce;
```

### 3. Configure as variáveis de ambiente
Edite o arquivo `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/les_ecommerce
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# Gemini API (para chatbot de recomendações)
gemini.api.key=sua_chave_api_gemini
```

### 4. Compile e execute
```bash
# Compilar
mvn clean install

# Executar
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`

## 📚 Documentação da API

Após iniciar a aplicação, acesse a documentação interativa:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🎯 Funcionalidades Principais

### 👤 Gestão de Clientes
- ✅ Cadastro completo de clientes (dados pessoais, endereços, cartões)
- ✅ Autenticação por email e senha
- ✅ Sistema de ranking de clientes
- ✅ Gestão de múltiplos endereços por cliente
- ✅ Gestão de múltiplos cartões de crédito
- ✅ Ativação/Inativação de contas
- ✅ Alteração de senha

### 📚 Catálogo de Livros
- ✅ Listagem de livros com categorias
- ✅ Busca por ID
- ✅ Controle de estoque integrado
- ✅ Informações detalhadas (autor, editora, preço, descrição)
- ✅ Imagens dos produtos

### 🛒 Carrinho de Compras
- ✅ Criação automática de carrinho por cliente
- ✅ Adicionar/remover itens
- ✅ Atualizar quantidades
- ✅ Aplicação de cupons de desconto
- ✅ Cálculo automático de totais

### 📦 Sistema de Pedidos
- ✅ Checkout completo com validações
- ✅ Pagamento com múltiplos cartões
- ✅ Controle de status (ABERTO, ENVIADO, ENTREGUE, EM_TROCA)
- ✅ Geração de código de rastreamento
- ✅ Controle de datas (pedido, envio, entrega)
- ✅ Baixa automática de estoque
- ✅ Validação de valor mínimo (R$ 10,00)
- ✅ Suporte a novos endereços no checkout
- ✅ Suporte a novos cartões no checkout

### 🎟️ Sistema Avançado de Cupons
- ✅ Tipos de cupons:
  - **TROCA**: Gerado automaticamente em trocas
  - **PROMOCIONAL**: Cupons de marketing
  - **PRIMEIRA_COMPRA**: Para novos clientes
- ✅ Descontos percentuais ou fixos
- ✅ Valor mínimo de compra
- ✅ Data de validade
- ✅ Cupons de uso único ou múltiplo
- ✅ Sistema de saldo: cupons de troca parcialmente usados geram novo cupom com saldo restante
- ✅ **Regra especial**: apenas cupons de TROCA podem ser combinados; outros tipos são exclusivos
- ✅ Validação automática (ativo, validade, valor mínimo)

### 🔄 Sistema Completo de Trocas
Sistema robusto com fluxo controlado de aprovação:

**Fluxo de Troca:**
1. **Solicitação (Cliente)**: Cliente solicita troca de itens de pedido entregue
2. **Autorização (Admin)**: Administrador aprova ou nega a solicitação
3. **Recebimento (Admin)**: Confirmação de recebimento dos itens
4. **Conclusão**: Geração automática de cupom de troca

**Funcionalidades:**
- ✅ Troca parcial de itens do pedido
- ✅ Motivo de troca por item
- ✅ Controle de status detalhado
- ✅ Retorno automático ao estoque (opcional por item)
- ✅ Geração de cupom com valor da troca (validade de 6 meses)
- ✅ Histórico completo de trocas
- ✅ Filtros por status e cliente
- ✅ Validações de elegibilidade (pedido deve estar ENTREGUE)

### 🤖 Chatbot de Recomendações com IA
Sistema inteligente de recomendação de livros usando Google Gemini:

**Características:**
- ✅ Conversação natural em português
- ✅ Contexto baseado nos 20 livros mais vendidos
- ✅ Recomendações personalizadas por cliente
- ✅ Informações de preço integradas
- ✅ Sistema de parsing `[BOOK:ID]` para identificar livros
- ✅ Filtro automático de livros sem estoque
- ✅ Respostas focadas exclusivamente em livros
- ✅ Tratamento de erros robusto

### 📊 Analytics de Vendas
- ✅ Volume total de vendas por período
- ✅ Análise detalhada por produto
- ✅ Análise detalhada por categoria
- ✅ Dados diários com preenchimento de datas vazias (para gráficos contínuos)
- ✅ Consultas personalizadas por data

## 📝 Endpoints da API

### 👤 Clientes (`/api/clientes`)

#### Listar todos os clientes
```http
GET /api/clientes
```

#### Buscar cliente por ID
```http
GET /api/clientes/{id}
```

#### Criar cliente
```http
POST /api/clientes
Content-Type: application/json

{
  "nome": "João Silva",
  "cpf": "12345678900",
  "email": "joao@email.com",
  "senha": "senha123",
  "genero": "MASCULINO",
  "dataNascimento": "1990-01-01",
  "tipoTelefone": "CELULAR",
  "ddd": "11",
  "numeroTelefone": "987654321",
  "enderecos": [
    {
      "tipoResidencia": "CASA",
      "tipoLogradouro": "RUA",
      "logradouro": "Das Flores",
      "numero": 123,
      "bairro": "Centro",
      "cep": "12345678",
      "cidade": "São Paulo",
      "estado": "SP",
      "pais": "Brasil",
      "tipoEndereco": "COBRANCA"
    }
  ]
}
```

#### Login
```http
POST /api/clientes/login
Content-Type: application/json

{
  "email": "joao@email.com",
  "senha": "senha123"
}
```

#### Atualizar cliente
```http
PUT /api/clientes/{id}
```

#### Alterar senha
```http
PUT /api/clientes/{id}/senha
Content-Type: application/json

{
  "senhaAtual": "senha123",
  "novaSenha": "novaSenha456",
  "confirmaSenha": "novaSenha456"
}
```

#### Inativar/Ativar cliente
```http
PUT /api/clientes/{id}/inativar
PUT /api/clientes/{id}/ativar
```

---

### 📍 Endereços (`/api/clientes/enderecos`)

#### Listar endereços do cliente
```http
GET /api/clientes/{id}/enderecos
```

#### Criar endereço
```http
POST /api/clientes/enderecos
Content-Type: application/json

{
  "clienteId": 1,
  "tipoResidencia": "APARTAMENTO",
  "tipoLogradouro": "AVENIDA",
  "logradouro": "Paulista",
  "numero": 1000,
  "bairro": "Bela Vista",
  "cep": "01310100",
  "cidade": "São Paulo",
  "estado": "SP",
  "pais": "Brasil",
  "tipoEndereco": "ENTREGA"
}
```

#### Atualizar/Deletar endereço
```http
PUT /api/clientes/enderecos/{id}
DELETE /api/clientes/enderecos/{id}
```

---

### 💳 Cartões de Crédito (`/api/clientes/cartoes`)

#### Listar cartões do cliente
```http
GET /api/clientes/{id}/cartoes
```

#### Criar cartão
```http
POST /api/clientes/cartoes
Content-Type: application/json

{
  "clienteId": 1,
  "numeroCartao": "1234567890123456",
  "nomeImpresso": "JOAO SILVA",
  "codigoSeguranca": "123",
  "bandeira": "VISA",
  "validade": "12/28"
}
```

#### Deletar cartão
```http
DELETE /api/clientes/cartoes/{id}
```

---

### 📚 Livros (`/api/livros`)

#### Listar todos os livros
```http
GET /api/livros
```

#### Buscar livro por ID
```http
GET /api/livros/{id}
```

---

### 🏷️ Categorias (`/api/categorias`)

#### Listar todas as categorias
```http
GET /api/categorias
```

#### Buscar categoria por ID
```http
GET /api/categorias/{id}
```

---

### 🛒 Carrinho (`/api/carrinhos`)

#### Buscar carrinho do cliente
```http
GET /api/carrinhos/cliente/{clienteId}
```

#### Criar carrinho
```http
POST /api/carrinhos/cliente/{clienteId}
```

#### Adicionar item ao carrinho
```http
POST /api/carrinhos/cliente/{clienteId}/itens
Content-Type: application/json

{
  "livroId": 1,
  "quantidade": 2
}
```

#### Atualizar quantidade
```http
PUT /api/carrinhos/{carrinhoId}/itens
Content-Type: application/json

{
  "livroId": 1,
  "quantidade": 3
}
```

#### Remover item
```http
DELETE /api/carrinhos/{carrinhoId}/itens/{livroId}
```

---

### 📦 Pedidos (`/api/pedidos`)

#### Checkout (Criar pedido)
```http
POST /api/pedidos/checkout
Content-Type: application/json

{
  "carrinhoId": 1,
  "enderecoEntregaId": 1,
  "cartoesPagamento": [
    {
      "cartaoId": 1,
      "valor": 100.00,
      "parcelas": 3
    }
  ],
  "cupons": [
    {
      "cupomId": 1,
      "codigo": "TROCA-ABC123"
    }
  ],
  "cupomPromocionalCodigo": "PROMO10"
}
```

#### Consultar pedido
```http
GET /api/pedidos/{id}
```

#### Listar pedidos do cliente
```http
GET /api/pedidos/cliente/{clienteId}
```

#### Listar todos os pedidos (Admin)
```http
GET /api/pedidos
```

#### Atualizar status
```http
PATCH /api/pedidos/{id}/status
Content-Type: application/json

{
  "status": "ENVIADO"
}
```

---

### 🎟️ Cupons (`/api/cupons`)

#### Validar cupom
```http
GET /api/cupons/validar/{codigo}
```

#### Listar todos os cupons (Admin)
```http
GET /api/cupons
```

#### Buscar cupom por ID
```http
GET /api/cupons/{id}
```

#### Criar cupom (Admin)
```http
POST /api/cupons
Content-Type: application/json

{
  "codigo": "PROMO10",
  "tipoCupom": "PROMOCIONAL",
  "valor": 10.0,
  "percentual": true,
  "ativo": true,
  "singleUse": false,
  "valorMinimo": 50.0,
  "dataValidade": "2025-12-31"
}
```

#### Desativar cupom (Admin)
```http
DELETE /api/cupons/{id}
```

---

### 🔄 Trocas (`/api/trocas`)

#### Solicitar troca (Cliente)
```http
POST /api/trocas
Content-Type: application/json

{
  "pedidoId": 1,
  "clienteId": 1,
  "motivoTroca": "Produto com defeito",
  "itens": [
    {
      "pedidoItemId": 1,
      "quantidade": 1,
      "motivo": "Páginas rasgadas"
    }
  ]
}
```

#### Autorizar troca (Admin)
```http
PUT /api/trocas/autorizar
Content-Type: application/json

{
  "trocaId": 1,
  "autorizada": true,
  "observacao": "Troca aprovada"
}
```

#### Confirmar recebimento (Admin)
```http
PUT /api/trocas/confirmar-recebimento
Content-Type: application/json

{
  "trocaId": 1,
  "itens": [
    {
      "trocaItemId": 1,
      "retornarEstoque": true
    }
  ],
  "observacao": "Itens recebidos em bom estado"
}
```

#### Listar trocas pendentes (Admin)
```http
GET /api/trocas/pendentes
```

#### Listar trocas do cliente
```http
GET /api/trocas/cliente/{clienteId}
```

#### Listar todas as trocas (Admin)
```http
GET /api/trocas
```

#### Listar por status
```http
GET /api/trocas/status/{status}
```

#### Buscar troca por ID
```http
GET /api/trocas/{id}
```

---

### 🤖 Chatbot de Recomendações (`/api/chat`)

#### Gerar recomendações
```http
POST /api/chat/recomendacoes
Content-Type: application/json

{
  "clienteId": 1,
  "historico": [
    {
      "role": "user",
      "content": "Estou procurando livros de ficção científica"
    }
  ]
}
```

**Resposta:**
```json
{
  "respostaIA": {
    "role": "assistant",
    "content": "Ótima escolha! Recomendo estes livros de ficção científica..."
  },
  "livrosRecomendados": [
    {
      "id": 1,
      "titulo": "Duna",
      "autor": "Frank Herbert",
      "preco": 45.90,
      "estoque": 10
    }
  ]
}
```

---

### 📊 Analytics (`/api/analytics`)

#### Volume total de vendas
```http
GET /api/analytics/vendas/total?inicio=2025-01-01&fim=2025-12-31
```

#### Vendas por produto
```http
GET /api/analytics/vendas?tipo=PRODUTO&id=1&dataInicio=2025-01-01&dataFim=2025-12-31
```

#### Vendas por categoria
```http
GET /api/analytics/vendas?tipo=CATEGORIA&id=1&dataInicio=2025-01-01&dataFim=2025-12-31
```

## 🏗️ Estrutura do Projeto

```
les-ecommerce-backend/
├── src/
│   ├── main/
│   │   ├── java/com/enzo/les/les/
│   │   │   ├── controller/              # Controladores REST
│   │   │   │   ├── ClienteController.java
│   │   │   │   ├── LivroController.java
│   │   │   │   ├── CategoriaController.java
│   │   │   │   ├── CarrinhoController.java
│   │   │   │   ├── PedidoController.java
│   │   │   │   ├── CupomController.java
│   │   │   │   ├── TrocaController.java
│   │   │   │   ├── RecomendacaoChatController.java
│   │   │   │   └── AnaliseController.java
│   │   │   ├── service/                 # Lógica de negócio
│   │   │   │   ├── ClienteService.java
│   │   │   │   ├── LivroService.java
│   │   │   │   ├── CarrinhoService.java
│   │   │   │   ├── PedidoService.java
│   │   │   │   ├── CupomService.java
│   │   │   │   ├── TrocaService.java
│   │   │   │   ├── RecomendacaoChatService.java
│   │   │   │   ├── GeminiHttpClient.java
│   │   │   │   └── AnaliseService.java
│   │   │   ├── model/entities/          # Entidades JPA
│   │   │   │   ├── Cliente.java
│   │   │   │   ├── Endereco.java
│   │   │   │   ├── CartaoCredito.java
│   │   │   │   ├── Livro.java
│   │   │   │   ├── Categoria.java
│   │   │   │   ├── Editora.java
│   │   │   │   ├── Carrinho.java
│   │   │   │   ├── CarrinhoItem.java
│   │   │   │   ├── Pedido.java
│   │   │   │   ├── PedidoItem.java
│   │   │   │   ├── Pagamento.java
│   │   │   │   ├── Cupom.java
│   │   │   │   ├── CupomUso.java
│   │   │   │   ├── Troca.java
│   │   │   │   ├── TrocaItem.java
│   │   │   │   ├── SaldoEstoque.java
│   │   │   │   └── GrupoPrecificacao.java
│   │   │   ├── repository/              # Interfaces JPA
│   │   │   ├── dtos/                    # Data Transfer Objects
│   │   │   ├── enums/                   # Enumerações
│   │   │   └── exceptions/              # Exceções customizadas
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## 🔐 Regras de Negócio

### Sistema de Cupons
- ✅ Apenas cupons do tipo TROCA podem ser combinados entre si
- ✅ Cupons PROMOCIONAL e PRIMEIRA_COMPRA são exclusivos (não podem ser combinados)
- ✅ Cupons de troca parcialmente usados geram novo cupom com saldo restante
- ✅ Cupons single-use são desativados após o primeiro uso
- ✅ Validação automática de data de validade e valor mínimo

### Sistema de Trocas
- ✅ Apenas pedidos com status ENTREGUE podem ser trocados
- ✅ Não pode existir mais de uma troca por pedido
- ✅ Itens devem pertencer ao pedido sendo trocado
- ✅ Quantidade de troca não pode exceder quantidade do pedido
- ✅ Cupom de troca válido por 6 meses
- ✅ Retorno ao estoque é opcional e controlado por item

### Sistema de Pedidos
- ✅ Valor mínimo de compra: R$ 10,00
- ✅ Baixa automática de estoque no checkout
- ✅ Validação de estoque disponível
- ✅ Suporte a pagamento com múltiplos cartões
- ✅ Suporte a múltiplos cupons (apenas TROCA)
- ✅ Geração automática de código de rastreamento ao enviar

### Controle de Estoque
- ✅ Bloqueio pessimista durante checkout (evita race conditions)
- ✅ Validação de quantidade disponível
- ✅ Atualização automática no checkout
- ✅ Retorno automático em trocas aprovadas (opcional)

## 🧪 Testes

Execute os testes com:
```bash
mvn test
```

## 🛠️ Desenvolvimento

### Modo de Desenvolvimento
O projeto utiliza Spring DevTools para reload automático durante o desenvolvimento.

### Banco H2 para Testes
O projeto está configurado com H2 Database para testes automatizados.

## 📊 Modelo de Dados

### Principais Entidades

**Cliente**
- Dados pessoais (nome, CPF, email, senha)
- Múltiplos endereços
- Múltiplos cartões de crédito
- Ranking de cliente
- Status ativo/inativo

**Livro**
- Informações bibliográficas
- Múltiplas categorias
- Editora
- Controle de estoque integrado
- Imagem

**Pedido**
- Cliente
- Endereço de entrega
- Itens do pedido
- Pagamentos (múltiplos cartões)
- Cupons usados
- Status e rastreamento

**Troca**
- Pedido original
- Itens da troca
- Fluxo de aprovação
- Cupom gerado
- Controle de status detalhado

**Cupom**
- Tipos (TROCA, PROMOCIONAL, PRIMEIRA_COMPRA)
- Validações (data, valor mínimo)
- Uso único ou múltiplo
- Cliente específico ou geral

## 🌟 Diferenciais do Projeto

1. **Sistema de Cupons Avançado**: Lógica complexa com combinação controlada e geração de saldo
2. **IA Integrada**: Chatbot inteligente com Google Gemini para recomendações
3. **Sistema de Trocas Completo**: Fluxo profissional com aprovação e controle de estoque
4. **Analytics Robusto**: Análises detalhadas com preenchimento de dados para gráficos
5. **Controle de Estoque**: Bloqueio pessimista e validações rigorosas
6. **Arquitetura Escalável**: Separação clara de responsabilidades (Controller, Service, Repository)
7. **Documentação Automática**: Swagger/OpenAPI integrado

## 🤝 Contribuindo

Este é um projeto acadêmico, mas sugestões e melhorias são bem-vindas!

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 📄 Licença

Projeto acadêmico desenvolvido para a disciplina de Laboratório de Engenharia de Software.

## 👨‍💻 Autores

Desenvolvido como projeto da disciplina LES.

## 🙏 Agradecimentos

- Disciplina de Laboratório de Engenharia de Software
- Google Gemini API
- Comunidade Spring Boot

---

⭐️ Se este projeto foi útil para você, considere dar uma estrela no repositório!

## 📞 Suporte

Para dúvidas ou problemas, abra uma issue no repositório do projeto.
