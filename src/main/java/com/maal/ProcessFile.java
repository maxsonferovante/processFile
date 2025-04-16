package com.maal;

import java.time.Duration;
import java.time.LocalDateTime;
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
class ProcessFile implements FileProcessor {
    @Override
    public void run(String filePath) {
        LocalDateTime startTime = LocalDateTime.now();
        int idealThreadCount = calculateIdealThreadCount();
        logInfo("Número ideal de threads: " + idealThreadCount);

        try {
            List<String> lines = readFile(filePath);
            logInfo("Total de linhas a serem processadas: " + lines.size());

            if (lines.isEmpty()) {
                logInfo("Nenhuma linha para processar.");
                return;
            }
            logInfo("Iniciando o processamento em paralelo...");

            processLinesInParallel(lines, idealThreadCount);
            lines.clear();
            logInfo("Processamento finalizado com sucesso!");
        } catch (IOException e) {
            logError("Erro ao ler o arquivo", e);
        } finally {
            logInfo("Tempo total de execução: " + Duration.between(startTime, LocalDateTime.now()));
        }
    }

    private List<String> readFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private void processLinesInParallel(List<String> lines, int threadCount) {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<Integer>> futures = new ArrayList<>();

        int batchSize = lines.size() / threadCount;
        for (int i = 0; i < threadCount; i++) {
            int start = i * batchSize;
            int end = (i == threadCount - 1) ? lines.size() : start + batchSize;
            List<String> subList = lines.subList(start, end);

            Callable<Integer> task = () -> {
                int localCount = 0;
                for (String line : subList) {
                    processLine(line);
                    localCount++;
                }
                return localCount;
            };

            futures.add(executor.submit(task));
        }

        int totalProcessed = 0;
        for (Future<Integer> future : futures) {
            try {
                totalProcessed += future.get();
            } catch (InterruptedException | ExecutionException e) {
                logError("Erro durante o processamento paralelo", e);
            }
        }

        logInfo("Total de linhas processadas: " + totalProcessed);
        executor.shutdown();
    }

    private void processLine(String line) {
        int wordCount = line.split("\\s+").length;
//        logInfo(Thread.currentThread().getName() + " processou: '" + line + "' (" + wordCount + " palavras)");
    }
}