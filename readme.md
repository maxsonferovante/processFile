# 📂 ProcessFile - Processamento Paralelo de Arquivos em Java

Este projeto demonstra **duas abordagens distintas** para o processamento de arquivos de texto em Java, com foco em **paralelismo e desempenho**. Ideal para aplicações que precisam lidar com grandes volumes de dados, o projeto mostra como dividir o trabalho entre múltiplas threads de forma eficiente.

---

## 🧠 Objetivo

Explorar duas formas de processar arquivos linha a linha em Java:

1. **Abordagem Tradicional**: leitura completa do arquivo na memória.
2. **Abordagem Sob Demanda (Streaming)**: leitura em fluxo com processamento paralelo usando fila (`BlockingQueue`).

---

## 📁 Estrutura do Projeto

```
src/
├── Main.java
├── ProcessFile.java                  # Implementação da abordagem tradicional
└── StreamParallelProcessor.java      # Implementação com leitura sob demanda
```

---

## 🧪 Caso 1 — Abordagem Tradicional

### Como funciona:

1. Lê o **arquivo inteiro para a memória**.
2. Divide as linhas em blocos para múltiplas threads.
3. Cada thread processa suas linhas (conta palavras, por exemplo).
4. Ao final, exibe os resultados por thread e o total processado.

### Quando usar:

✅ Arquivos pequenos/médios  
✅ Processamento simples e rápido  
❌ Pode estourar a memória com arquivos grandes

### Exemplo de Saída:

```plaintext
Número ideal de threads: 8
Total de linhas a serem processadas: 14344391
Tempo total de execução: PT2.066665223S
```

---

## 🚀 Caso 2 — Abordagem Sob Demanda (Streaming com Produtor-Consumidor)

### Como funciona:

1. O arquivo é lido **linha a linha, em tempo real** (streaming).
2. Um "produtor" agrupa as linhas em lotes e envia para uma `BlockingQueue`.
3. Múltiplas threads "consumidoras" processam esses lotes em paralelo.
4. Um sinal especial (`Poison Pill`) encerra as threads no final.

### Quando usar:

✅ Arquivos grandes  
✅ Leitura eficiente com baixo uso de memória  
✅ Processamento contínuo e paralelizado  
⚠️ Maior complexidade de código

### Exemplo de Saída:

```plaintext
Iniciando processamento paralelo do arquivo: src/main/resources/rockyou.txt com 8 threads.
Processamento finalizado com sucesso! Total de linhas processadas: 14344391
Tempo total de execução: PT1.046596379S
```

---

## ⚙️ Como Executar

1. Certifique-se de ter **Java 21+** e **Gradle** instalados.
2. Coloque o arquivo `.txt` em `src/main/resources/rockyou.txt`.
3. Para rodar a abordagem tradicional:

   ```bash
   ./gradlew run
   ```

4. Para rodar a abordagem sob demanda, altere o `Main.java`:

   ```java
   StreamParallelProcessor.run("src/main/resources/rockyou.txt");
   ```

   E execute novamente:

   ```bash
   ./gradlew run
   ```

---

## 🧩 Comparativo das Abordagens

| Característica                         | Tradicional                      | Streaming com Fila (Prod/Cons)  |
|---------------------------------------|----------------------------------|----------------------------------|
| Carregamento do Arquivo               | Todo na memória                  | Linha a linha                    |
| Uso de Memória                        | Alto                             | Baixo                            |
| Complexidade de Implementação         | Baixa                            | Alta                             |
| Performance em Arquivos Pequenos      | Muito boa                        | Boa                              |
| Performance em Arquivos Grandes       | Ruim                             | Muito boa                        |
| Modelo de Execução                    | `ExecutorService` + `Future`     | `BlockingQueue` + consumidores   |

---

## 📌 Requisitos

- Java 21 ou superior
- Gradle
- Terminal compatível (Linux/macOS/Windows com bash ou PowerShell)

---

## 📈 Melhorias Futuras

- Métricas de tempo por thread
- UI com progress bar para arquivos longos
- Escolha de modo de execução via CLI
- Exportar logs para arquivo externo

---

Feito com 💻 e ☕ por desenvolvedores que curtem ver o CPU trabalhando bem!