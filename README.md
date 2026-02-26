# Autoflex – Controle Inteligente de Estoque 🏭

O **Autoflex** é uma solução Fullstack desenvolvida para a web que atende a indústrias que produzem itens variados e necessitam automatizar o controle do seu estoque de **insumos (matérias-primas)**.
O grande diferencial da ferramenta é a sua capacidade preditiva: além de registrar e associar as quantidades de matérias-primas gastas por cada produto, o sistema **avalia o estoque atual** e sugere exatamente **quais produtos** e **quantas unidades de cada um** ainda podem ser fabricados, priorizando agressivamente a ordem de fabricação pelos itens de **maior valor gerado**.

---

## 🚀 Live Demo (Deploy na Nuvem)

A aplicação encontra-se atualmente publicada na plataforma **Render**, totalmente operacional e disponível publicamente:
🔗 **Acessar Autoflex:** [https://autoflex-front-us1n.onrender.com](https://autoflex-front-us1n.onrender.com)

> ⚠️ **Aviso Importante sobre o Acesso:** O ambiente está rodando na "camada grátis" (Free Tier) do serviço Render. O provedor desliga os contêineres e o banco de dados temporariamente se o sistema ficar mais de **15 minutos** inativo ou sem requisições externas.
> Por conta disso, se a sua primeira abertura de tela demorar alguns segundos, ou falhar na hora de carregar produtos, **não se preocupe**: o servidor está apenas sendo ativado dinamicamente de novo por debaixo dos panos. É só aguardar um breve instante ou recarregar a tela!

---

## 📋 Funcionalidades do Sistema (Imagens)

Aqui documentamos a navegação central da ferramenta:

### 1. Gestão de Matérias-Primas
Cadastro e controle de insumos e do seu quantitativo de estoque disponível de forma limpa.
> <img width="1894" height="910" alt="image" src="https://github.com/user-attachments/assets/7cfc9826-edd8-4bd9-8eff-0b0f320404a8" />
> <img width="1907" height="908" alt="image" src="https://github.com/user-attachments/assets/7d2a3609-9360-4984-ae8d-9fe898d52c93" />

### 2. Gestão de Produtos e Associação
Cadastro de produtos finalizados informando **Nome, Código e o seu respectivo Valor ($)**, além do formulário acoplado e responsivo para associar os insumos necessários.
> <img width="1905" height="909" alt="image" src="https://github.com/user-attachments/assets/cad10732-33b2-4551-b920-159c5a4a38b5" />
> <img width="1904" height="914" alt="image" src="https://github.com/user-attachments/assets/a60607d5-d9ff-4dd7-b0d6-a684979beb79" />

### 3. Simulador de Produção Baseado em Estoque (O Diferencial)
O painel de processamento lógico da ferramenta. Ele esgota o estoque virtualmente e calcula a quantia a produzir dando a prioridade para o item cujo **Valor de venda ($)** é mais alto (evitando que produtos baratos consumam todo o estoque de um componente raro que geraria mais faturamento num produto Premium).
> <img width="1906" height="911" alt="image" src="https://github.com/user-attachments/assets/01a30238-996b-4d06-a5fe-bc35539baa71" />


---

## ✅ Cobertura do Teste Prático (Requisitos vs Implementação)

Abaixo descrevo de forma explícita e modular, como todos os Requisitos avaliados neste Processo Seletivo (Fullstack Junior) foram 100% cumpridos na versão final entregue da aplicação:

### Requisitos Não Funcionais (Atendidos)

| Requisito | Status na Aplicação | O que foi feito |
| --- | --- | --- |
| **RNF001** – Plataforma WEB nos principais navegadores | ✅ **Feito** | Interface de Single Page App desenhada em React (compatível em Node/Vite com Firefox, Chrome, Edge). |
| **RNF002** – Separação de API Frontend vs Backend | ✅ **Feito** | A separação foi tratada à risca, possuindo um servidor porta `8080` (Spring) consumido puramente por Requisições REST HTTP do Cliente React. |
| **RNF003** – Telas responsivas | ✅ **Feito** | O projeto utiliza CSS Vanila extensivo e Media Queries modernas para abrigar a experiência tanto em telas largas (Desktop) quanto num formulário ou tabela num Mobile nativo. |
| **RNF004** – Persistência em SGBDs (Postgres, MySQl, Oracle) | ✅ **Feito** | Migrado inteiramente com Spring Data JPA + Driver oficial do banco de dados **PostgreSQL** para a versão produtiva. |
| **RNF005** – Backend com Spring / similar | ✅ **Feito** | Desenhado utilizando Java 21 junto do poderoso ecossistema do **Spring Boot 3**. |
| **RNF006** – Frontend em React + Redux | ✅ **Feito** | O client é servido por **ReactJS (v19)** integrado nativamente a **Redux e (Redux Toolkit - RTK)** utilizando slices globais de estado de armazenamento em memória. |
| **RNF007** – Código totalmente em Inglês | ✅ **Feito** | Nomenclaturas de funções, declaração de objetos, DTOs e colunas ORM Hibernate do banco estão em padrão de conversação Inglês Técnico (*ex: `quantityInStock`, `ProductionSuggestionDTO`*). |

### Requisitos Funcionais (Atendidos)

| Requisito | Status | Implementação Técnica Realizada |
| --- | --- | --- |
| **RF001** – Backend: CRUD Produtos | ✅ **Feito** | Endpoints REST criados: `GET/POST/PUT/DELETE /api/products` gerenciados no Controller. |
| **RF002** – Backend: CRUD Matérias Primas | ✅ **Feito** | Endpoints REST criados: `GET/POST/PUT/DELETE /api/raw-materials` gerenciados pelo Service isolado. |
| **RF003** – Backend: Associar Matérias aos Produtos | ✅ **Feito** | Implementação de relacionamento `@OneToMany(cascade.ALL)` garantido a inclusão/deleção de `ProductMaterial` atrelados junto ao Produto numa única submissão DTO de objeto longo. |
| **RF004** – Backend: Consulta de produtos a fabricar (Estoque e Priorizando Valor) | ✅ **Feito** | Implementado lógica algorítmica no backend `/production-suggestion`. Traz os produtos ordenados descendentes de forma a esgotar blocos virtuais dinamicamente. Retorna listas de sugestão calculando o Total em Dinheiro Acumulado com ela. |
| **RF005** – Frontend: UI CRUD Produtos | ✅ **Feito** | Telas de Listagem (fetch via axios) e Formulário (`src/pages/ProductForm.jsx`), conectados por React Router v6. |
| **RF006** – Frontend: UI CRUD Matéria Primeira | ✅ **Feito** | Telas de listagem (`src/pages/RawMaterialList.jsx`) dispostas de maneira paralela às de produtos |
| **RF007** – Frontend: UI Associar Matéria a Produto em formulário de Cadastro | ✅ **Feito** | Incluído bloco estendido dinâmico e flexível no próprio fluxo interativo de `ProductForm.jsx` para adicionar blocos de Matérias necessárias atreladas em um só `submit`. |
| **RF008** – Frontend: UI Listar relatórios e quantidades priorizadas por estoque | ✅ **Feito** | Painel dinâmico gerado em `src/pages/ProductionSuggestion.jsx` que consome as propostas do backend e formata como uma Listagem de Análise Inteligente final. |

### Requisitos Desejáveis e Extras

- Testes Unitários no Backend: ✅ **Feito**
A aplicação servidor contém cobertura testada nos Services principais executadas utilizando **JUnit 5 + Mockito** (Ex: `ProductServiceTest.java`). Resguardando a regra de negócios limitantes a quebras acidentais de build.

- Testes Unitários no Frontend: ✅ **Feito** 
Aplicados em ambiente emulado local executando nativamente o binário de testes do **Jest com React Testing Library (RTL)**. Renderiza simuladores da DOM através do utils wrapper contendo conexões falsas do Mock do Redux e interage testando comportamentos base antes do software ir para nuvem. (Ex: `npm run test`)
