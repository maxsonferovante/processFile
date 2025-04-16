package com.maal;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

class StreamParallelProcessor implements FileProcessor {
    private static final int BATCH_SIZE = 1000;
    private static final String POISON_PILL = "__END__";

    @Override
    public void run(String filePath) {
        LocalDateTime startTime = LocalDateTime.now();
        int threadCount = calculateIdealThreadCount();
        logInfo("Iniciando processamento paralelo do arquivo: " + filePath + " com " + threadCount + " threads.");

        BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>();
        ExecutorService consumers = Executors.newFixedThreadPool(threadCount);
        AtomicLong count = new AtomicLong();

        for (int i = 0; i < threadCount; i++) {
            consumers.submit(() -> {
                try {
                    while (true) {
                        List<String> batch = queue.take();
                        if (batch.size() == 1 && batch.getFirst().equals(POISON_PILL)) break;
                        for (String line : batch) {
                            processLine(line);
                            count.getAndIncrement();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logError("Thread interrompida - " + Thread.currentThread().getName(), e);
                }
            });
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> batch = new ArrayList<>(BATCH_SIZE);
            String line;
            while ((line = reader.readLine()) != null) {
                batch.add(line);
                if (batch.size() >= BATCH_SIZE) {
                    queue.put(new ArrayList<>(batch));
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                queue.put(new ArrayList<>(batch));
            }
            for (int i = 0; i < threadCount; i++) {
                queue.put(List.of(POISON_PILL));
            }
        } catch (IOException | InterruptedException e) {
            logError("Erro ao ler/processar o arquivo " + filePath, e);
        }

        consumers.shutdown();
        try {
            if (!consumers.awaitTermination(60, TimeUnit.SECONDS)) {
                consumers.shutdownNow();
            }
        } catch (InterruptedException e) {
            consumers.shutdownNow();
            Thread.currentThread().interrupt();
            logError("Erro ao aguardar término do processamento", e);
        }

        logInfo("Processamento finalizado com sucesso! Total de linhas processadas: " + count.get());
        logInfo("Tempo total de execução: " + Duration.between(startTime, LocalDateTime.now()));
    }

    private void processLine(String line) {
        int wordCount = line.split("\\s+").length;
    }
}
