# Autoflex – Stock Control (Products & Raw Materials)

Sistema WEB para controle de estoque de matérias-primas e produtos, com sugestão de produção priorizada por valor.

## Requisitos atendidos

### Não funcionais
- **RNF001** – Web, principais navegadores (Chrome, Firefox, Edge).
- **RNF002** – API separada (backend Spring Boot, frontend React).
- **RNF003** – Telas responsivas (CSS responsivo e layout flex).
- **RNF004** – Persistência em SGBD: H2 em memória por padrão; PostgreSQL/MySQL/Oracle configuráveis em `application.properties`.
- **RNF005** – Backend com Spring Boot.
- **RNF006** – Frontend com React e Redux (Redux Toolkit).
- **RNF007** – Código, tabelas e colunas do banco de dados e codificação frontend e backend em inglês.

### Funcionais
- **RF001** – CRUD produtos (backend).
- **RF002** – CRUD matérias-primas (backend).
- **RF003** – CRUD associação produto ↔ matérias-primas (backend; inclusão no cadastro de produto).
- **RF004** – Consulta de produtos que podem ser produzidos com o estoque atual (prioridade por maior valor).
- **RF005** – Interface CRUD de produtos (frontend).
- **RF006** – Interface CRUD de matérias-primas (frontend).
- **RF007** – Interface para associar matérias-primas aos produtos (no formulário de produto).
- **RF008** – Interface para listar produtos e quantidades sugeridas para produção e valor total.

## Como executar

### Backend (Spring Boot)

```bash
cd backend/autoflex
./mvnw spring-boot:run
```

Ou com Maven instalado:

```bash
cd backend/autoflex
mvn spring-boot:run
```

A API sobe em **http://localhost:8080**.  
Endpoints:

- `GET/POST /api/products`, `GET/PUT/DELETE /api/products/{id}`
- `GET/POST /api/raw-materials`, `GET/PUT/DELETE /api/raw-materials/{id}`
- `GET /api/production-suggestion` – sugestão de produção (produtos, quantidades e valor total)

### Banco de dados

- **Padrão:** H2 em memória (`application.properties` já configurado). Útil para desenvolvimento sem poluir o Banco de dados e não precisar criar outro banco util também para testes.
- **PostgreSQL:** descomente as linhas de PostgreSQL em `backend/autoflex/src/main/resources/application.properties` e comente as do H2; crie o banco e ajuste URL/usuário/senha.

### Frontend (React + Vite)

```bash
cd frontend
npm install
npm run dev
```

Acesse **http://localhost:5173**. O proxy do Vite redireciona `/api` para `http://localhost:8080`; mantenha o backend rodando.

## Estrutura

- **Backend:** `backend/autoflex/`
  - Entidades: `Product`, `RawMaterial`, `ProductMaterial` (associação com quantidade).
  - Repositories, DTOs, Services, Controllers; exceções tratadas globalmente; CORS configurado.
- **Frontend:** `frontend/src/`
  - API client (axios), Redux (slices para products e rawMaterials), React Router.
  - Páginas: listagem/formação de produtos (com matérias-primas), listagem/formação de matérias-primas, sugestão de produção.

## Sugestão de produção (RF004)

O endpoint `/api/production-suggestion`:

1. Ordena produtos por **valor unitário (decrescente)**.
2. Para cada produto, calcula a quantidade máxima produzível com o estoque atual (limitação por matéria-prima).
3. “Consome” virtualmente o estoque e inclui o item na sugestão.
4. Retorna lista de itens (código, nome, valor unitário, quantidade sugerida, valor total) e **valor total** da sugestão.

Assim, matérias-primas compartilhadas são alocadas primeiro para os produtos de maior valor.
