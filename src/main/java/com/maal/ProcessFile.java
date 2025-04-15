package com.maal;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessFile {

    private static final Logger logger = Logger.getLogger(String.valueOf(ProcessFile.class));

    public static void run(String filePath) {
        int idealThreadCount = calculateIdealThreadCount();
        logger.info("Número ideal de threads: " + idealThreadCount);

        try {
            List<String> lines = readFile(filePath);
            logger.info("Total de linhas a serem processadas: " + lines.size());

            if (lines.isEmpty()) {
                logger.info("Nenhuma linha para processar.");
                return;
            }
            logger.info("Iniciando o processamento em paralelo...");

            processLinesInParallel(lines, idealThreadCount);
            lines.clear();
            logger.info("Processamento finalizado com sucesso!");
        }catch (IOException e){
            logger.log(Level.SEVERE, "Erro ao ler o arquivo", e);
        }
    }
    private static int calculateIdealThreadCount() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        return Math.max(2, availableProcessors); // Pelo menos 2 threads
    }


    private static List<String> readFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
    private static void processLinesInParallel(List<String> lines, int threadCount) {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<Integer>> futures = new ArrayList<>();

        // Divide o trabalho entre as threads
        int batchSize = lines.size() / threadCount;
        for (int i = 0; i < threadCount; i++) {
            int start = i * batchSize;
            int end = (i == threadCount - 1) ? lines.size() : start + batchSize;
            List<String> subList = lines.subList(start, end);

            // Cria uma tarefa Callable para cada thread
            Callable<Integer> task = () -> {
                int localCount = 0;
                for (String line : subList) {
                    processLine(line); // Processa a linha
                    localCount++;
                }
                return localCount;
            };

            futures.add(executor.submit(task));
        }

        // Coleta os resultados
        int totalProcessed = 0;
        for (Future<Integer> future : futures) {
            try {
                totalProcessed += future.get();
                logger.info("Thread processou: " + future.get() + " linhas");
            } catch (InterruptedException | ExecutionException e) {
                logger.log(Level.SEVERE, "Erro durante o processamento paralelo", e);
            }
        }

        logger.info("Total de linhas processadas: " + totalProcessed);
        executor.shutdown();
    }

    private static void processLine(String line){
        // Simula algum processamento
        // Exemplo de processamento: contar palavras na linha
        int wordCount = line.split("\\s+").length;
        String message = Thread.currentThread().getName() + " processou: '" + line + "' (" + wordCount + " palavras)";
        logger.info(message);

    }
}
