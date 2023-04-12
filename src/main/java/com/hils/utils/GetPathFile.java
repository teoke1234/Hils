package com.hils.utils;

import com.hils.constants.FrameworkConstants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class GetPathFile {

    private GetPathFile(){}

    public static List<String> getPathFile(String[] paths, String fileExtension) {
        List<String> listPathFile = new ArrayList<>();
        for (String path:paths) {
            path = FrameworkConstants.PROJECTPATH+"\\"+path;
            System.out.println(path);
            try (Stream<Path> walk = Files.walk(Paths.get(path))) {
                List<String> collect = walk.filter(p -> !Files.isDirectory(p)) // not a directory
                        .map(p -> p.toString()) // convert path to string
                        .filter(f -> f.endsWith(fileExtension))// check end with
                        .collect(Collectors.toList()); // collect all matched to a List
//                collect.forEach(System.out::println);
                listPathFile = Stream.concat(listPathFile.stream(),collect.stream()).toList();
//            System.out.println(listPathFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                        .filter(f -> f.endsWith(".mxc</Testcase>"))
                        .map(m -> m.substring(m.lastIndexOf("\\") + 1, m.lastIndexOf(extention)-1))//mxc
                        .collect(Collectors.toList());

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            String scenarioName = path.substring(path.lastIndexOf("\\") + 1,path.lastIndexOf(".mxs"));


            testScenario.put(scenarioName, result);


        }
//        testScenario.keySet().forEach(System.out::println);
        LinkedHashMap<String, List<String>> sortTestcases = testScenario.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
//        System.out.println(collect.size());

        return sortTestcases;
    }

    public static Map<String, String> readTagOfTestScenario(List<String> paths, String extention) {
        Map<String, String> testSenarioAndTag = new HashMap<>();

        LinkedHashMap<String, String> sortTestcases = null;
        for (String path : paths) {
            String tag;
            try (Stream<String> lines = Files.lines(Paths.get(path))) {

                tag = lines
                        .map(p -> p.replaceAll("\\s+", ""))
                        .filter(f -> f.endsWith("Group=\"Precondition\"/>"))
                        .map(m -> m.substring(m.indexOf("\"") + 1, m.lastIndexOf("Group") - 1))
                        .collect(Collectors.joining());

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            String scenarioName = path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf(".mxs"));

            testSenarioAndTag.put(scenarioName, tag);

            sortTestcases = testSenarioAndTag                                                  //Map<String, String>
                    .entrySet()                                                        //Set<Map.Entry<String, String>>
                    .stream()                                                          // Stream<Map.Entry<String, String>>
                    .sorted(Map.Entry.comparingByKey())                                //Stream<Map.Entry<String, String>>
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        }
        return sortTestcases;
    }

    public static Map<String,String> getProcedure(Map<String, List<String>> results) {

        Map<String,String> map = new HashMap<>();
        String procedure = "";
        for (Map.Entry<String, List<String>> entry : results.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
            List<String> list = entry.getValue();

            for (int i = 1; i < list.size(); i++) {
                procedure += i + ". " + list.get(i - 1) + " \n";
            }
            map.put(entry.getKey(), procedure);

        }
//        System.out.println(map);
        return map;
    }

}
