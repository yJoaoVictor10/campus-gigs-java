# 🎓 Campus Gigs

API REST desenvolvida em **Java com Spring Boot** para gerenciamento de oportunidades acadêmicas e profissionais no ambiente universitário.

O projeto utiliza **Spring Security e JWT** para autenticação, garantindo que endpoints protegidos só possam ser acessados mediante um token válido.

## 🚀 Tecnologias

* ☕ Java 17
* 🌱 Spring Boot 4.1.1
* 🔐 Spring Security
* 🎟️ JWT / OAuth2 Resource Server
* 🗄️ Spring Data JPA
* 🐘 PostgreSQL
* 🔄 Flyway
* 🐳 Docker Compose
* 🛠️ Gradle
* 🧩 Lombok

## 📋 Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

* [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
* [Git](https://git-scm.com/)
* [Docker](https://www.docker.com/)

O projeto possui **Gradle Wrapper**, portanto não é necessário instalar o Gradle separadamente.

## 📥 Clonando o projeto

Clone o repositório:

```bash
git clone https://github.com/yJoaoVictor10/campus-gigs-java.git
```

Entre na pasta do projeto:

```bash
cd campus-gigs-java
```

## 🐘 Banco de dados

O projeto utiliza PostgreSQL.

O arquivo `compose.yaml` configura automaticamente um container PostgreSQL com:

```text
Database: campusgigs
Username: johnadam
Password: johnadam123
Port: 5432
```

Para iniciar o banco de dados:

```bash
docker compose up -d
```

Para verificar se o container está em execução:

```bash
docker compose ps
```

Para parar os containers:

```bash
docker compose down
```

## 🔑 Chaves JWT

A aplicação utiliza um par de chaves **RSA** para emissão e validação dos tokens JWT.

As configurações estão definidas em:

```properties
rsa.private-key=classpath:keys/private_key.pem
rsa.public-key=classpath:keys/public_key.pem
```

Portanto, as chaves devem estar disponíveis em:

```text
src/
└── main/
    └── resources/
        └── keys/
            ├── private_key.pem
            └── public_key.pem
```

> **Importante:** a chave privada deve ser mantida em segurança e não deve ser exposta em repositórios públicos em aplicações reais.

## ▶️ Executando a aplicação

### Windows

Execute:

```powershell
.\gradlew.bat bootRun
```

### Linux / macOS

Execute:

```bash
./gradlew bootRun
```

Após a inicialização, a API estará disponível em:

```text
http://localhost:8080
```

## 🧪 Executando os testes

Windows:

```powershell
.\gradlew.bat test
```

Linux / macOS:

```bash
./gradlew test
```

## 🔐 Autenticação JWT

A aplicação utiliza **JSON Web Token (JWT)** para autenticar requisições aos endpoints protegidos.

O fluxo de autenticação funciona da seguinte maneira:

```text
Cliente
   │
   │  Login / credenciais
   ▼
API
   │
   │  Validação das credenciais
   ▼
JWT
   │
   │  Authorization: Bearer <token>
   ▼
Endpoint protegido
   │
   │  Validação da assinatura e claims
   ▼
Resposta
```

Depois de realizar a autenticação e obter o token, ele deve ser enviado no header `Authorization`:

```http
Authorization: Bearer <SEU_TOKEN>
```

## 📌 Exemplo de chamada autenticada

Depois de obter um JWT, utilize o token retornado na requisição ao endpoint protegido.

### cURL

```bash
curl -X GET http://localhost:8080/<ENDPOINT_PROTEGIDO> \
  -H "Authorization: Bearer <SEU_TOKEN>"
```

### Exemplo no Postman

Configure uma requisição para o endpoint protegido e adicione o seguinte header:

| Key             | Value                |
| --------------- | -------------------- |
| `Authorization` | `Bearer <SEU_TOKEN>` |

Exemplo:

```http
GET http://localhost:8080/<ENDPOINT_PROTEGIDO>

Authorization: Bearer eyJhbGciOiJSUzI1NiJ9...
```

O token deve ser substituído pelo JWT efetivamente retornado pelo processo de autenticação.

## 🧪 Evidências de autenticação — Insomnia

As requisições abaixo demonstram o funcionamento do processo de autenticação da API utilizando **Insomnia**.

O cadastro de usuários foi realizado com diferentes perfis, permitindo validar a emissão de JWTs com diferentes roles.

### 👤 Usuário administrador

Requisição de cadastro/login utilizando um usuário com perfil `ADMIN`:

```json
{
  "username": "jack",
  "email": "jack@email.com",
  "password": "123456"
}
```

A API retornou um JWT contendo, entre outras informações, a role `ADMIN`:

```json
{
  "token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJmaWFwLWdpZ3MtYXBpIiwic3ViIjoiamFjayIsInJvbGUiOiJBRE1JTiIs..."
}
```

O payload do token contém informações como:

```json
{
  "iss": "fiap-gigs-api",
  "sub": "jack",
  "role": "ADMIN"
}
```

Isso demonstra que o usuário autenticado recebeu um token JWT associado ao seu perfil de administrador.

### 👤 Usuário comum

Também foi realizado o cadastro/login de um usuário com perfil `USER`:

```json
{
  "username": "mariaa",
  "email": "mariaa@email.com",
  "password": "123456",
  "cep": "00000000"
}
```

A API retornou um JWT contendo a role `USER`:

```json
{
  "token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJmaWFwLWdpZ3MtYXBpIiwic3ViIjoibWFyaWFhIiwicm9sZSI6IlVTRVIi..."
}
```

O payload do token contém:

```json
{
  "iss": "fiap-gigs-api",
  "sub": "mariaa",
  "role": "USER"
}
```

### 🔐 Utilização do JWT

Após a autenticação, o token retornado pela API deve ser utilizado nas requisições aos endpoints protegidos através do header:

```http
Authorization: Bearer <SEU_TOKEN>
```

No Insomnia, a configuração pode ser feita adicionando o token na aba de autenticação da requisição ou diretamente no header:

| Key             | Value                |
| --------------- | -------------------- |
| `Authorization` | `Bearer <SEU_TOKEN>` |

Essas evidências demonstram:

* cadastro/autenticação de usuários;
* emissão de JWT;
* identificação do usuário através da claim `sub`;
* identificação do perfil através da claim `role`;
* utilização do token para acesso aos endpoints protegidos;
* diferenciação entre usuários com perfil `ADMIN` e `USER`.



## 🛡️ Proteção dos endpoints

Os endpoints protegidos utilizam o mecanismo de segurança do Spring Security.

Uma requisição sem token válido não deve conseguir acessar um recurso protegido.

Exemplo:

```http
GET /<ENDPOINT_PROTEGIDO>
```

Sem autenticação:

```text
401 Unauthorized
```

Com um JWT válido:

```http
Authorization: Bearer <SEU_TOKEN>
```

A requisição pode prosseguir para o endpoint, desde que o token seja válido e o usuário possua as permissões necessárias.

## 🗃️ Migrations

O projeto utiliza **Flyway** para controle das alterações do banco de dados.

As migrations ficam no projeto e são executadas automaticamente durante a inicialização da aplicação.

Isso permite que a estrutura do banco seja criada/atualizada de forma controlada sem necessidade de executar manualmente os scripts a cada inicialização.

## 📁 Estrutura básica

```text
campus-gigs-java/
├── gradle/
│   └── wrapper/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │       ├── keys/
│   │       └── application.properties
│   └── test/
├── build.gradle
├── compose.yaml
├── gradlew
├── gradlew.bat
└── settings.gradle
```

## 🧹 Parando o ambiente

Para encerrar o banco de dados:

```bash
docker compose down
```

Para remover também os volumes associados aos containers:

```bash
docker compose down -v
```

> O segundo comando remove os dados persistidos no volume do PostgreSQL. Utilize-o somente quando quiser recriar o banco do zero.

## 👨‍💻 Autor

**João Victor**

GitHub:
https://github.com/yJoaoVictor10

Repositório:
https://github.com/yJoaoVictor10/campus-gigs-java
