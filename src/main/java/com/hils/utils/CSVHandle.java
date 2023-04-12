package com.hils.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class CSVHandle {

    public static void writeCSV(Map<String, String> data, String path, Map<String, List<String>> dataTestCase) throws IOException {
        Path filePath = Paths.get(path);
        if(Files.exists(filePath)){
            Files.delete(filePath);
        }
        Files.createFile(filePath);

        BufferedWriter writer = Files.newBufferedWriter(filePath);

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("Scenario Name", "Tag", "Procedures"));

        int rowNum = 1;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            List<String> lists = dataTestCase.get(entry.getKey());
            StringBuilder join = new StringBuilder();
            for (int i = 0; i < lists.size(); i++) {
                join.append(i+1).append(". ").append(lists.get(i)).append("\n");
            }
            csvPrinter.printRecord(entry.getKey(),entry.getValue(),join.toString());
            rowNum++;
        }
        csvPrinter.flush();

        ExcelHandle.openFile(path);
    }
}
