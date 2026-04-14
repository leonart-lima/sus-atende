# SUS Atende - Sistema de Triagem e Direcionamento Inteligente

## 📋 Visão Geral

**SUS Atende** é um MVP (Produto Mínimo Viável) de um sistema backend para triagem inteligente e direcionamento de pacientes no SUS (Sistema Único de Saúde). O sistema analisa os sintomas do paciente, calcula um nível de urgência baseado em critérios clínicos, e recomenda a unidade de saúde mais apropriada para atendimento.

### Problema Identificado
O SUS enfrenta desafios como superlotação em unidades de atendimento, ineficiência no direcionamento de pacientes e falta de priorização inteligente. Isso resulta em:
- Aumento do tempo de espera
- Sobrecarga em emergências
- Pacientes em filas incorretas
- Experiência negativa para profissionais e pacientes

### Solução Proposta
Um sistema de triagem automatizado que:
- **Coleta informações** simples do paciente (sintomas, temperatura, duração)
- **Calcula urgência** usando algoritmo baseado em sinais vitais e sintomas críticos
- **Recomenda unidade** apropriada (UBS, Policlínica ou Pronto-Socorro)
- **Fornece alternativas** para maior flexibilidade

---

## 🎯 Funcionalidades

### 1. **Triagem de Pacientes**
- POST `/api/v1/triagens` - Realizar triagem completa
  - Cria/atualiza cadastro do paciente
  - Calcula score de urgência (0-10)
  - Determina nível: Baixa, Média ou Alta urgência
  - Sugere unidade principal + 3 alternativas
  - Gera observações orientadoras

### 2. **Gestão de Unidades de Saúde**
- GET `/api/v1/unidades` - Listar todas as unidades
- GET `/api/v1/unidades/{id}` - Detalhes de unidade específica
- GET `/api/v1/unidades/tipo/{tipo}` - Filtrar por tipo (UBS, POLICLINICA, PRONTO_SOCORRO, HOSPITAL)
- GET `/api/v1/unidades/disponibilidade/ativas` - Unidades com capacidade
- PUT `/api/v1/unidades/{id}/disponibilidade` - Atualizar leitos disponíveis

### 3. **Consulta de Resultados**
- GET `/api/v1/triagens/{id}` - Obter resultado completo de triagem anterior

---

## 🏗️ Arquitetura Técnica

### Stack Utilizado
- **Linguagem**: Java 21
- **Framework**: Spring Boot 3.3.0
- **ORM**: Spring Data JPA (Hibernate 6)
- **Banco de Dados**: H2 (em memória para MVP)
- **Documentação API**: Swagger/OpenAPI 3.0 (SpringDoc 2.1.0)
- **Testes**: JUnit 5 + Mockito
- **Build**: Maven

### Estrutura de Pacotes
```
com.sus.atende/
├── model/          # Entidades JPA (Paciente, UnidadeSaude, Triagem)
├── repository/     # Interfaces JPA Repository
├── service/        # Lógica de negócio (TriagemService, UnidadeSaudeService)
├── controller/     # Endpoints REST (TriagemController, UnidadeSaudeController)
├── dto/            # Data Transfer Objects (requisições/respostas)
└── config/         # Configurações (OpenAPI/Swagger)
```

### Diagrama Simplificado
```
┌─────────────────────────────────────────┐
│         Cliente (Postman/API)            │
└──────────────┬──────────────────────────┘
               │
        ┌──────▼──────┐
        │ Controllers │
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │  Services   │ ◄─── Lógica de Triagem (Score, Urgência)
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │ Repositories│
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │  H2 Database│ (Pacientes, Unidades, Triagens)
        └─────────────┘
```

---

## 🔧 Algoritmo de Triagem

O sistema calcula um **score de urgência** (0-10) baseado em:

### Critérios de Temperatura
- Febre ≥ 39.5°C: +3 pontos (febre muito alta)
- Febre 38.5-39.4°C: +2 pontos (febre moderada)
- Febre 38-38.4°C: +1 ponto (febre leve)

### Sintomas Críticos
- Falta de ar/Dispneia: +3 pontos
- Dor no peito/Palpitação: +3 pontos
- Convulsão/Paralisia: +3 pontos
- Tontura/Desmaio: +2 pontos
- Ferimento/Sangramento: +2 pontos
- Dor (moderada/forte): +2 pontos
- Alergia com inchação: +2 pontos
- Vômito/Diarreia: +1 ponto

### Fatores de Risco
- Duração > 7 dias: +1 ponto
- Histórico de morbidades (diabetes, hipertensão, cardíacos): +1 ponto

### Classificação por Score
- **0-2**: Baixa Urgência → **UBS** (pode aguardar sem risco)
- **3-5**: Média Urgência → **Policlínica** (atendimento em poucas horas)
- **6+**: Alta Urgência → **Pronto-Socorro/Hospital** (atendimento imediato)

---

## 🚀 Como Executar

### Pré-requisitos
- Java 21+
- Maven 3.8+
- Git

### Instalação e Execução

```bash
# 1. Clonar repositório
git clone https://github.com/leonartlima/sus-atende.git
cd sus-atende

# 2. Compilar
mvn clean install

# 3. Executar aplicação
mvn spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8080**

### Acessar Documentação Interativa
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:susdb`
  - User: `sa` (sem senha)

---

## 📚 Exemplos de Requisições

### 1. Criar Triagem (Urgência Baixa)
```bash
curl -X POST http://localhost:8080/api/v1/triagens \
  -H "Content-Type: application/json" \
  -d '{
    "nomePaciente": "João Silva",
    "cpf": "12345678901",
    "idade": 35,
    "telefone": "(11) 99999-9999",
    "email": "joao@example.com",
    "sintomas": "Dor de cabeça leve",
    "temperatura": 37.2,
    "diasComSintomas": 2,
    "sexo": "MASCULINO"
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "nomePaciente": "João Silva",
  "cpf": "12345678901",
  "nivelUrgencia": "BAIXA",
  "descricaoUrgencia": "Baixa Urgência - Pode aguardar sem risco",
  "scoreTriagem": 1,
  "unidadeSugerida": {
    "id": 1,
    "nome": "UBS Zona Sul",
    "endereco": "Rua das Flores, 123 - Zona Sul",
    "telefone": "(11) 3001-0001",
    "tipo": "UBS",
    "taxaOcupacao": 10.0,
    "especialidades": ["Clínica Geral", "Pediatria", "Enfermagem"]
  },
  "unidadesAlternativas": [
    {
      "id": 6,
      "nome": "UBS Zona Norte",
      "tipo": "UBS",
      "leitosDisponiveis": 20,
      "taxaOcupacao": 9.09
    }
  ],
  "dataTriagem": "2026-04-13T21:35:00"
}
```

### 2. Criar Triagem (Urgência Alta)
```bash
curl -X POST http://localhost:8080/api/v1/triagens \
  -H "Content-Type: application/json" \
  -d '{
    "nomePaciente": "Maria Santos",
    "cpf": "98765432101",
    "idade": 55,
    "telefone": "(11) 98888-8888",
    "email": "maria@example.com",
    "sintomas": "Falta de ar, dor no peito, palpitação",
    "temperatura": 40.0,
    "diasComSintomas": 3,
    "historicoMorbidades": "Doença cardíaca",
    "sexo": "FEMININO"
  }'
```

**Resposta:**
```json
{
  "id": 2,
  "nomePaciente": "Maria Santos",
  "nivelUrgencia": "ALTA",
  "descricaoUrgencia": "Alta Urgência - Atendimento imediato",
  "scoreTriagem": 9,
  "unidadeSugerida": {
    "id": 4,
    "nome": "Pronto-Socorro Central",
    "tipo": "PRONTO_SOCORRO",
    "leitosDisponiveis": 25,
    "taxaOcupacao": 75.0,
    "especialidades": ["Emergência", "Traumatologia", "Cirurgia"]
  }
}
```

### 3. Listar Todas as Unidades
```bash
curl http://localhost:8080/api/v1/unidades
```

### 4. Filtrar por Tipo
```bash
curl http://localhost:8080/api/v1/unidades/tipo/UBS
```

### 5. Obter Triagem Anterior
```bash
curl http://localhost:8080/api/v1/triagens/1
```

### 6. Atualizar Leitos Disponíveis
```bash
curl -X PUT "http://localhost:8080/api/v1/unidades/1/disponibilidade?novaDisponibilidade=15"
```

---

## ✅ Testes

### Executar Todos os Testes
```bash
mvn test
```

### Executar Teste Específico
```bash
mvn test -Dtest=TriagemServiceTest
mvn test -Dtest=SusAtendeApplicationTests
```

### Cobertura de Testes
- ✅ Triagem com urgência baixa/média/alta
- ✅ Direcionamento correto para unidades
- ✅ Validação de dados de entrada
- ✅ Testes de integração (API completa)
- ✅ Tratamento de erros

---

## 📊 Dados de Seed (Unidades Pré-carregadas)

O banco H2 inicia com 7 unidades de saúde:

| ID | Nome | Tipo | Leitos | Especialidades |
|---|---|---|---|---|
| 1 | UBS Zona Sul | UBS | 20 | Clínica Geral, Pediatria |
| 2 | UBS Centro | UBS | 25 | Clínica Geral, Odontologia |
| 3 | Policlínica Regional Leste | POLICLINICA | 50 | Cardiologia, Dermatologia, Pneumologia |
| 4 | Pronto-Socorro Central | PRONTO_SOCORRO | 100 | Emergência, Traumatologia, Cirurgia |
| 5 | Hospital Municipal Geral | HOSPITAL | 300 | Cirurgia, Clínica Médica, Oncologia |
| 6 | UBS Zona Norte | UBS | 22 | Clínica Geral, Pediatria, Pré-natal |
| 7 | Policlínica Oeste | POLICLINICA | 60 | Oftalmologia, Neurologia |

---

## 🔮 Melhorias Futuras

### Curto Prazo (MVP v2)
- [ ] Integração com banco de dados PostgreSQL
- [ ] Autenticação e autorização (JWT)
- [ ] Historicização completa do paciente
- [ ] Sistema de avaliação de atendimento
- [ ] Alertas de superlotação

### Médio Prazo
- [ ] Machine Learning para otimizar recomendações
- [ ] Integração com sistemas existentes do SUS
- [ ] API Gateway e load balancing
- [ ] Cache distribuído (Redis)
- [ ] Integração com Google Maps (distância/tempo)

### Longo Prazo
- [ ] Frontend web/mobile
- [ ] Telemedicina integrada
- [ ] Dashboard de analytics para gestores
- [ ] Sistema de agendamento automático
- [ ] Notificações push ao paciente

---

## 👥 Equipe

**Projeto desenvolvido como hackathon do curso de Pós-Graduação em Arquitetura e Desenvolvimento Java - FIAP**

---

## 📄 Licença

Este projeto é fornecido como MVP educacional para fins de demonstração e desenvolvimento.

---

## 🔗 Links Úteis

- **Documentação OpenAPI**: `/swagger-ui.html`
- **GitHub**: https://github.com/leonartlima/sus-atende
- **Spring Boot Docs**: https://spring.io/projects/spring-boot
- **H2 Database**: https://www.h2database.com/

---

**Desenvolvido com ❤️ para otimização do SUS**
