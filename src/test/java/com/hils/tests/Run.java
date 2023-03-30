package com.hils.tests;

import com.hils.constants.FrameworkConstants;
import com.hils.utils.ExcelHandle;
import com.hils.utils.GetPathFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Run {

    public static void main(String[] args) throws IOException {


        // check new and deleted test cases and writes to excel

        //get old list tcs in Excel file in Mx-Suite
//        List<String> listOne = ExcelHandle.getListOldTcs(FrameworkConstants.EXCELPATH, FrameworkConstants.SHEETNAME, FrameworkConstants.COLUMN);
//        //get list tcs in Folder project
//        List<String> listTwo = GetPathFile.getPathFile(FrameworkConstants.PROJECTPATH, "mxc");
//
//
//
//        List<String> differences = listTwo.stream()
//                .filter(element -> !listOne.contains(element))
//                .collect(Collectors.toList());
//
//        differences.forEach(e -> System.out.println("New one added: "+e));
//
//        //detect difference test cases
//        List<String> differences2 = listOne.stream()
//                .filter(element -> !listTwo.contains(element))
//                .collect(Collectors.toList());
//
//        differences2.forEach(e -> System.out.println("Deleted Cases : "+e));
//
//        //highlight deleted tcs in excel sheet
//        ExcelHandle.writeToExcelDetectDeletedCase(differences2, FrameworkConstants.EXCELPATH, FrameworkConstants.SHEETNAME, FrameworkConstants.COLUMN);
//
//        //add new tcs into Excel sheet
//        ExcelHandle.writeToExcelAddNewCase(differences, FrameworkConstants.EXCELPATH, FrameworkConstants.SHEETNAME, FrameworkConstants.COLUMN);

        // get list scenario
        List<String> listScenario = GetPathFile.getPathFile(FrameworkConstants.PROJECTPATH, "mxs");
//        System.out.println(listScenario);

//         get list scenario and tcs
        Map<String, List<String>> listScenarioAndTcs = GetPathFile.readTcOfTestScenario(listScenario, "mxc");
//        System.out.println(listScenarioAndTcs);


        Map<String, String> scenarioAndTag = GetPathFile.readTagOfTestScenario(listScenario, "mxc");
//        System.out.println(scenarioAndTag);

//        Map<String, String> procedure = GetPathFile.getProcedure(listScenarioAndTcs);
//        Set<String> strings = procedure.keySet();
//        for (String string:strings) {
//            System.out.println(string);
//        }


//        write to Excel sheet
        ExcelHandle.writeToExcel(listScenarioAndTcs,FrameworkConstants.EXCELPATH);
        ExcelHandle.writeTagToExcel(scenarioAndTag,FrameworkConstants.EXCELPATH);
        ExcelHandle.writeTestCaseToExcel(listScenarioAndTcs,FrameworkConstants.EXCELPATH);

    }
}
