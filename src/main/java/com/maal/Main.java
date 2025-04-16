package com.maal;

public class Main {
    public static void main(String[] args) {

        String filePath = "src/main/resources/rockyou.txt";

        ProcessFile processFile = new ProcessFile();
        processFile.run(filePath);

        StreamParallelProcessor streamParallelProcessor = new StreamParallelProcessor();
        streamParallelProcessor.run(filePath);
    }
}