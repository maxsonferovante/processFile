// Interface comum para padronizar as implementações de processamento
package com.maal;

import java.util.logging.Logger;

public interface FileProcessor {
    Logger logger = Logger.getLogger(FileProcessor.class.getName());

    void run(String filePath);

    default int calculateIdealThreadCount() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        return (int) Math.max(2, availableProcessors * 0.5);
    }

    default void logInfo(String message) {
        logger.info("[INFO] " + message);
    }

    default void logError(String message, Throwable t) {
        logger.severe("[ERROR] " + message + " - " + t.getMessage());
    }
}