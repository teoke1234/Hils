package com.hils.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class GetPathFile {

    private GetPathFile(){}

    public static List<String> getPathFile(String path, String fileExtension) {
        List<String> listPathFile = null;
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            listPathFile = walk.filter(p -> !Files.isDirectory(p)) // not a directory
                    .map(p -> p.toString()) // convert path to string
                    .filter(f -> f.endsWith(fileExtension)) // check end with
                    .collect(Collectors.toList()); // collect all matched to a List

        } catch (IOException e) {
            e.printStackTrace();
        }
        return listPathFile;
    }


    public static Map<String, List<String>> readTcOfTestScenario(List<String> paths, String extention) {
        Map<String, List<String>> testScenario = new HashMap<>();
        List<String> result;
        for (String path : paths) {
            try (Stream<String> lines = Files.lines(Paths.get(path))) {

                result = lines
                        .map(p -> p.replaceAll("\\s+", ""))
                        .filter(f -> f.endsWith("</Testcase>"))
                        .map(m -> m.substring(m.lastIndexOf("\\") + 1, m.lastIndexOf(extention) + extention.length())) //mxc
                        .collect(Collectors.toList());


            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            String scenarioName = path.substring(path.lastIndexOf("\\") + 1);
            testScenario.put(scenarioName, result);

        }
        return testScenario;
    }

}
