package com.example.solution.Controller;

import com.example.solution.Entites.Client;
import com.example.solution.Entites.Mapping;
import com.example.solution.repositories.MappingRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class SettingCode {



    private static MappingRepository mappingRepository ;

    @Autowired
    public SettingCode(MappingRepository mappingRepository) {
        this.mappingRepository = mappingRepository;
    }

    public static List<Mapping> readExcelFile(InputStream inputStream, Client client) {
        List<Mapping> mappings = new ArrayList<>();
        int rowNum=0;

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                System.out.println(rowNum);
                if(rowNum++==0){
                    continue;
                }
                Cell codeDestCarre = row.getCell(2);
                Cell codeDestCell = row.getCell(1);
                Cell libelleDestCell = row.getCell(0);
                String codeDest = getCellValue(codeDestCell).toString();
                // Check if the cell contains an error formula
                String codeCarre = null;
                if (codeDestCarre != null && codeDestCarre.getCellType() == CellType.FORMULA
                        && codeDestCarre.getCellFormula().startsWith("VLOOKUP")) {
                    FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                    CellValue formulaResult = formulaEvaluator.evaluate(codeDestCarre);

                    if (formulaResult != null && formulaResult.getCellType() == CellType.ERROR) {
                        continue;
                    }
                    // Get the formula result as a string
                    codeCarre = formulaResult.formatAsString();
                } else if (codeDestCarre != null) {
                    if (codeDestCarre.getCellType() == CellType.STRING) {
                        codeCarre = codeDestCarre.getStringCellValue();
                    } else if (codeDestCarre.getCellType() == CellType.NUMERIC) {
                        codeDest = getCellValue(codeDestCell).toString();
                    }
                }
                System.out.println("code carre"+codeCarre);


                String libelleDest = getCellValue(libelleDestCell).toString();

                Mapping mapping = new Mapping();
                mapping.setCodeDest(codeDest);
                System.out.println(codeDest);
                mapping.setLibelleDest(libelleDest);
                mapping.setCodeDestCarre(codeCarre);
                mapping.setClient(client);
                mappingRepository.save(mapping);
                mappings.add(mapping);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

        return mappings;
    }
    public static Object getCellValue(Cell cell) {
        Object cellValue = null;

        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }else {
            // Check the cell type to handle different data types
            CellType cellType = cell.getCellType();

            switch (cellType) {
                case STRING:
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        cellValue = cell.getStringCellValue();
                    }
                    break;
                case NUMERIC:
                    // if (DateUtil.isCellDateFormatted(cell)) {
                    //   cellValue = cell.getDateCellValue();
                    //} else {
                    //   cellValue = cell.getNumericCellValue();
                    //}
                    double numericValue = cell.getNumericCellValue();
                    int intValue = (int) numericValue; // Cast the double to an int
                    cellValue = String.valueOf(intValue);
                    break;
                case BOOLEAN:
                    cellValue = cell.getBooleanCellValue();
                    break;
                case FORMULA:
                    // For formulas, we retrieve the string value if available
                    try {
                        cellValue = cell.getStringCellValue();
                    } catch (IllegalStateException e) {
                        // If getStringCellValue() throws an exception, it means the formula result is not a string
                        if (DateUtil.isCellDateFormatted(cell)) {
                            cellValue = cell.getDateCellValue();
                        } else {
                            cellValue = cell.getNumericCellValue();
                        }
                    }
                    break;
                default:
                    return null;
            }
        }
        return cellValue;
    }

}
