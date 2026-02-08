# Risa Nail Studio API Challenge

![Java](https://img.shields.io/badge/Java-25-orange?style=flat-square&logo=oracle)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-brightgreen?style=flat-square&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-blue?style=flat-square&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=flat-square&logo=docker)

API RESTful para gerenciamento de agendamentos de um estúdio de nail design. Desenvolvida com Spring Boot 4.0.2, Java 25, e arquitetura hexagonal (ports & adapters).

## 📋 Índice

- [Features](#-features)
- [Stack](#-stack)
- [Arquitetura](#-arquitetura)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Configuração](#-instalação-e-configuração)
- [Executando com Docker](#-executando-com-docker)
- [Executando Localmente](#-executando-localmente)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Autenticação](#-autenticação)
- [Logging](#-logging)
- [Monitoramento](#-monitoramento)
- [Testes](#-testes)
- [Estrutura do Projeto](#-estrutura-do-projeto)

## ✨ Features

- **Autenticação JWT** com refresh tokens
- **Autorização baseada em roles** (RBAC)
- **CRUD completo** de serviços e agendamentos
- **Migrations automáticas** com Flyway
- **Health checks** e métricas Prometheus
- **Documentação OpenAPI** (Swagger UI + Scalar)
- **Logging estruturado**
- **Docker-ready** com multi-stage builds
- **Validação de dados** com Bean Validation

## 🛠 Stack

- **Java 25** - Linguagem de programação
- **Spring Boot 4** - Framework backend
- **Spring Security** - Autenticação
- **Spring OAuth2 Resource Server** - Autorização
- **Spring Data JPA** - ORM
- **PostgreSQL 18** - Banco de dados relacional
- **Flyway** - Gerenciador de migrations
- **Lombok** - Redução de boilerplate
- **SpringDoc OpenAPI** - Documentação da API
- **Docker & Docker Compose** - Containerização
- **Gradle** - Build tool

## Arquitetura

O projeto segue a **Arquitetura Hexagonal** (Ports & Adapters):

```
com.smlaurindo.risanailstudio/
├── adapter/               # Adapters de ports
│   ├── inbound/
│   │   ├── web/           # Controllers REST
│   │   └── transactional/
│   └── outbound/
│       ├── persistence/
│       └── security/
├── application/
│   ├── domain/            # Entidades de domínio
│   ├── usecase/           # Casos de uso
│   └── exception/         # Exceções de negócio
├── port/                  # Interfaces de ports
│   └── outbound/
└── shared/
    ├── config/            # Configurações
    ├── dto/               # DTOs
    └── utils/             # Utilitários
```

## 📋 Pré-requisitos

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)

## 🚀 Instalação e Configuração

### 1. Clone o repositório

```bash
git clone https://github.com/smlaurindo/risa-nailstudio-challenge.git
cd risa-nailstudio-challenge
```

### 2. Configure as variáveis de ambiente

Copie o arquivo de exemplo e edite com suas configurações:

```bash
cp .env.example .env
```

#### 2.1. Variáveis de Ambiente

| Variável                       | Obrigatório | Padrão                                        | Descrição                                               |
| ------------------------------ | ----------- | --------------------------------------------- | ------------------------------------------------------- |
| **Banco de Dados**             |
| `POSTGRES_DB`                  | ✅          | `risa_demo_db`                                | Nome do banco de dados PostgreSQL                       |
| `POSTGRES_USER`                | ✅          | `risa_demo_user`                              | Usuário do banco de dados                               |
| `POSTGRES_PASSWORD`            | ✅          | `risa_demo_password`                          | Senha do banco de dados                                 |
| `POSTGRES_PORT`                | ❌          | `5432`                                        | Porta do PostgreSQL                                     |
| **API**                        |
| `API_PORT`                     | ❌          | `8080`                                        | Porta onde a API será executada                         |
| **Adminer**                    |
| `ADMINER_PORT`                 | ❌          | `8088`                                        | Porta do Adminer (gerenciador de BD)                    |
| **Connection Pool**            |
| `DB_POOL_SIZE`                 | ❌          | `10`                                          | Tamanho máximo do pool de conexões                      |
| `DB_POOL_MIN_IDLE`             | ❌          | `5`                                           | Número mínimo de conexões ociosas                       |
| **JWT (Autenticação)**         |
| `JWT_PUBLIC_KEY`               | ✅          | -                                             | Chave pública RSA para verificar tokens                 |
| `JWT_PRIVATE_KEY`              | ✅          | -                                             | Chave privada RSA para assinar tokens                   |
| `JWT_ACCESS_TOKEN_EXPIRATION`  | ❌          | `900000`                                      | Tempo de expiração do access token (ms) - 15 min        |
| `JWT_REFRESH_TOKEN_EXPIRATION` | ❌          | `604800000`                                   | Tempo de expiração do refresh token (ms) - 7 dias       |
| **Segurança**                  |
| `SECURITY_PASSWORD_SALT`       | ❌          | `16`                                          | Tamanho do salt para hash de senhas                     |
| **CORS**                       |
| `CORS_ALLOWED_ORIGINS`         | ❌          | `http://localhost:3000,http://localhost:8080` | Origens permitidas (separadas por vírgula)              |
| `CORS_ALLOWED_METHODS`         | ❌          | `GET,POST,PUT,DELETE,OPTIONS,PATCH`           | Métodos HTTP permitidos                                 |
| **Cookies**                    |
| `COOKIE_DOMAIN`                | ❌          | `""`                                          | Domínio dos cookies (vazio para localhost)              |
| `COOKIE_SECURE`                | ❌          | `false`                                       | Cookies apenas em HTTPS (true em produção)              |
| `COOKIE_SAME_SITE`             | ❌          | `Lax`                                         | Política SameSite dos cookies (`Strict`, `Lax`, `None`) |

**Notas:**

- ✅ = Obrigatório | ❌ = Opcional
- As chaves JWT devem ser geradas conforme instruções na seção seguinte
- Em produção, altere `COOKIE_SECURE` para `true` e use HTTPS
- Os valores padrão são adequados para desenvolvimento local

### 3. Gere as chaves RSA para JWT (OBRIGATÓRIO)

Você pode gerar as chaves de duas formas:

#### Opção 1: Usando ferramentas online (Recomendado - Mais Fácil)

1. **Gerar as chaves RSA:**
   - Acesse: https://cryptotools.net/rsagen
   - Configure **Key length: 4096**
   - Clique em **Generate**
   - Copie a **Public Key** e a **Private Key**

2. **Converter a chave privada para PKCS#8:**
   - Acesse: https://decoder.link/rsa_converter
   - Cole a **Private Key** gerada no campo
   - Clique em **Convert**
   - Copie a chave convertida (formato PKCS#8)

3. **Adicionar as chaves no arquivo `.env`:**
   - Use a **Public Key** original em `JWT_PUBLIC_KEY`
   - Use a **Private Key convertida (PKCS#8)** em `JWT_PRIVATE_KEY`

#### Opção 2: Usando OpenSSL (Terminal)

```bash
# Gerar chave privada RSA 4096 bits
openssl genrsa -out private_key.pem 4096

# Converter para PKCS#8
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in private_key.pem -out private_key_pkcs8.pem

# Gerar chave pública
openssl rsa -in private_key.pem -pubout -out public_key.pem

# Exibir as chaves para copiar
cat public_key.pem
cat private_key_pkcs8.pem
```

#### Formato Final no `.env`

Copie o conteúdo completo das chaves (incluindo headers `-----BEGIN...-----` e `-----END...-----`) para o arquivo `.env`:

```env
JWT_PUBLIC_KEY=-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA...
...
-----END PUBLIC KEY-----

JWT_PRIVATE_KEY=-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC...
...
-----END PRIVATE KEY-----
```

**⚠️ Importante:**

- Mantenha as quebras de linha originais (não substitua por `\n`)
- A chave privada DEVE estar no formato PKCS#8 (começa com `-----BEGIN PRIVATE KEY-----`)
- NÃO use chaves de exemplo em produção - sempre gere novas chaves

## 🐳 Executando com Docker

### Modo Demonstração

Apos configurar o .env é possível levantar a api com todos os serviços usando o docker compose

```bash
# Construir e iniciar todos os serviços
docker-compose up --build

# Ou executar em background
docker-compose up --build -d
```

**O que é iniciado:**

- API
- PostgreSQL
- Adminer (DB Manager)

### Verificar status dos containers

```bash
docker-compose ps
```

### Ver logs

```bash
# Todos os serviços
docker-compose logs -f

# Apenas a API
docker-compose logs -f api

# Apenas o PostgreSQL
docker-compose logs -f postgres
```

### Parar os serviços

```bash
docker-compose down

# Para remover também os volumes (limpar dados)
docker-compose down -v
```

## 📚 Documentação da API

Após iniciar a aplicação, acesse:

### Swagger UI (Interface Interativa)

```
http://localhost:8080/swagger-ui.html
```

### Scalar (Alternativa Moderna ao Swagger)

```
http://localhost:8080/scalar
```

> [!IMPORTANT]  
> Não esqueça de alterar a porta corretamente na URL

## Banco de dados

### Migrations

A aplicação conta com migrations sql que criam automaticamente as tabelas:

- **users** - Credenciais e roles dos usuários
- **customers** - Dados dos clientes
- **admins** - Dados dos administradores
- **services** - Serviços oferecidos pelo estúdio
- **appointments** - Agendamentos realizados
- **refresh_tokens** - Tokens de refresh JWT

### Seed de demonstração

Junto com as migrations, há tambem seeds para popular a base de dados

**Usuário Admin:**

- Email: `admin@risanailstudio.com`
- Senha: `Admin@123`

**Usuário Cliente:**

- Email: `customer@example.com`
- Senha: `Customer@123`

**Serviços pré-cadastrados:**

- Manicure Básica (45min - R$ 35,00)
- Pedicure Básica (50min - R$ 40,00)
- Manicure + Pedicure (90min - R$ 70,00)
- Esmaltação em Gel (60min - R$ 55,00)
- Unhas Decoradas (75min - R$ 80,00)
- Alongamento de Unhas (120min - R$ 120,00)
- Spa dos Pés (60min - R$ 65,00)
- Hidratação de Mãos (30min - R$ 25,00)

## Logging

### Níveis de Log por Ambiente

**Desenvolvimento (`dev`):**

- Console output colorido
- SQL queries visíveis
- Level: DEBUG para código do projeto

**Demonstração (`demo`):**

- Console output estruturado
- Arquivos de log em `/app/logs`
- Level: INFO para código do projeto
- Rotação diária com limite de 30 dias

### Arquivos de Log

- `logs/application.log` - Logs gerais
- `logs/error.log` - Apenas erros
- Rotação automática por tamanho e data

### Visualizar logs em tempo real (Docker)

```bash
docker-compose logs -f api
```

## Metricas

### Health Check

Verificação de saúde da aplicação

```bash
curl http://localhost:8080/actuator/health
```

### Informações da Aplicação

Verificação de informações da aplicação

```bash
curl http://localhost:8080/actuator/info
```

### Métricas (Prometheus)

Também é possivel exportar as metricas para um coletor como o Prometheus

```bash
curl http://localhost:8080/actuator/metrics
curl http://localhost:8080/actuator/prometheus
```

## 📁 Estrutura do Projeto

```
api/
├── src/
│   ├── main/
│   │   ├── java/com/smlaurindo/risanailstudio/
│   │   │   ├── adapter/
│   │   │   │   ├── inbound/
│   │   │   │   │   ├── transactional/
│   │   │   │   │   └── web/
│   │   │   │   └── outbound/
│   │   │   │       ├── persistence/
│   │   │   │       └── security/
│   │   │   ├── application/
│   │   │   │   ├── domain/
│   │   │   │   ├── exception/
│   │   │   │   └── usecase/
│   │   │   ├── port/
│   │   │   │   └── outbound/
│   │   │   └── shared/
│   │   │       ├── config/
│   │   │       ├── constant/
│   │   │       ├── dto/
│   │   │       └── utils/
│   │   └── resources/
│   │       ├── db/migration/         # Flyway migrations
│   │       ├── application-dev.yaml  # Config desenvolvimento
│   │       ├── application-demo.yaml # Config produção
│   │       └── logback-spring.xml    # Config logging
│   └── test/
├── Dockerfile                        # Multi-stage build
├── docker-compose.yaml               # Orquestração containers
├── .dockerignore                     # Arquivos ignorados no build
├── .env.example                      # Exemplo de variáveis
├── build.gradle.kts                  # Configuração Gradle
└── README.md                         # Esta documentação
```

## 📝 Licença

Este projeto foi desenvolvido como desafio técnico para o Discord da Risa Nail Studio.

## 👤 Autor

Desenvolvido por **Samuel Laurindo**.