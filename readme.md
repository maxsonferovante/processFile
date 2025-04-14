# ProcessFile

Este projeto é uma aplicação Java que processa arquivos de texto em paralelo, utilizando threads para otimizar o desempenho. Ele divide o trabalho entre múltiplas threads, processa cada linha do arquivo e exibe informações sobre o processamento.

## Estrutura do Projeto

- **`Main.java`**: Classe principal que inicia a execução do programa. Define o caminho do arquivo a ser processado e chama o método `run` da classe `ProcessFile`.
- **`ProcessFile.java`**: Contém a lógica principal para leitura do arquivo, divisão do trabalho entre threads e processamento das linhas.

## Funcionamento

1. **Leitura do Arquivo**:  
   O arquivo de texto é lido linha por linha e armazenado em uma lista.

2. **Cálculo de Threads Ideais**:  
   O número ideal de threads é calculado com base no número de processadores disponíveis na máquina, garantindo pelo menos 2 threads.

3. **Processamento em Paralelo**:
    - As linhas do arquivo são divididas em lotes, e cada lote é atribuído a uma thread.
    - Cada thread processa suas linhas e armazena o número de linhas processadas em uma variável `ThreadLocal`.

4. **Exibição de Resultados**:
    - O programa exibe o número de linhas processadas por cada thread.
    - Exibe o total de linhas processadas ao final.

5. **Simulação de Processamento**:  
   O método `processLine` simula o processamento de cada linha, contando o número de palavras e exibindo uma mensagem.

## Como Executar

1. Certifique-se de que o Gradle está instalado.
2. Coloque o arquivo de texto a ser processado no caminho `src/main/resources/rockyou.txt`.
3. Compile e execute o projeto:
   ```bash
   ./gradlew run
   ```

## Exemplo de Saída

```plaintext
Número ideal de threads: 4
Total de linhas a serem processadas: 100
Iniciando o processamento em paralelo...
Thread-1 processou: 'linha exemplo 1' (3 palavras)
Thread-2 processou: 'linha exemplo 2' (4 palavras)
...
Thread processou: 25 linhas
Thread processou: 25 linhas
Total de linhas processadas: 100
```

## Requisitos

- **Java 21+**
- **Gradle**

## Observações

- O arquivo `rockyou.txt` deve estar no formato de texto simples.
- O programa utiliza um pool de threads fixo para evitar sobrecarga no sistema.

## Melhorias Futuras

- Adicionar suporte para arquivos maiores que a memória disponível.
- Permitir configuração dinâmica do número de threads.