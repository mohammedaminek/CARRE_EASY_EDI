package com.example.solution.OracleData;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.solution.Controller.SettingCode.getCellValue;

@Controller
@AllArgsConstructor
public class UploadSearchFile {
    MyService myService;

    @GetMapping("/user/SerachState")
    public String uploadFile() {
        return "uploadFile";
    }
    @PostMapping("/user/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model,HttpServletResponse response) {
        XSSFWorkbook workbook;
        XSSFSheet sheet;
        ArrayList<String> Declartions=new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView();
        if (!file.isEmpty()) {
            try {
                workbook = new XSSFWorkbook(file.getInputStream());
                sheet = workbook.getSheetAt(0);
                int rowNum=0;
                for(Row row:sheet){
                    System.out.println("row"+rowNum);
                    int cellNum = 0;
                    if(rowNum>0){
                        Cell cell=row.getCell(0);
                        if (cell != null) {
                            if(cellNum==0){
                                String declaration= (String)getCellValue(cell);
                                Declartions.add(declaration);
                                cellNum++;
                            }
                        }

                    }
                    rowNum++;
                }
                System.out.println(Declartions);
                model.addAttribute("Declarations",Declartions);
                return performDatabaseOperation( response, Declartions);
            } catch (IOException e) {
                e.printStackTrace();
                modelAndView.addObject("error", "An error occurred while processing the file.");
            }
        } else {
            modelAndView.addObject("error", "Please select a file to upload.");
        }
        return "upload";
    }

        @GetMapping("/user/resultList")
        public String performDatabaseOperation(HttpServletResponse response, ArrayList<String> Declartions) throws IOException {
            List<Map<String, String>> dataList = new ArrayList<>();

       /*     // Create a sample data entry as a Map
            Map<String, String> dataEntry1 = new HashMap<>();
            dataEntry1.put("numDeclaration", "12345");
            dataEntry1.put("sens", "Outbound");
            dataEntry1.put("dateEnvoi", "2023-09-28");
            dataEntry1.put("dateLivr", "2023-09-30");
            dataEntry1.put("origine", "Source");
            dataEntry1.put("destenation", "Destination");
            dataEntry1.put("colis", "5");
            dataEntry1.put("poids", "10 kg");
            dataEntry1.put("volume", "0.5 m³");
            dataEntry1.put("valeurDeclaree", "1000 USD");
            dataEntry1.put("cc", "CC123");
            dataEntry1.put("cr", "CR456");
            dataEntry1.put("ct", "CT789");
            dataEntry1.put("raisonSocialeDestinataire", "Recipient Company");
            dataEntry1.put("retourBl", "Yes");
            dataEntry1.put("mntHt", "750 USD");
            dataEntry1.put("numFacture", "F7890");
            dataEntry1.put("status", "Delivered");
            dataEntry1.put("commentaireLivMobile", "Delivered on time");
            dataEntry1.put("dateCommentaireLivMobile", "2023-09-30");
            dataEntry1.put("nbrLad", "2");

            // Add the data entry to the list
            dataList.add(dataEntry1);            dataEntry1.put("numDeclaration", "12345");
            dataEntry1.put("sens", "Outbound");
            dataEntry1.put("dateEnvoi", "2023-09-28");
            dataEntry1.put("dateLivr", "2023-09-30");
            dataEntry1.put("origine", "Source");
            dataEntry1.put("destenation", "Destination");
            dataEntry1.put("colis", "5");
            dataEntry1.put("poids", "10 kg");
            dataEntry1.put("volume", "0.5 m³");
            dataEntry1.put("valeurDeclaree", "1000 USD");
            dataEntry1.put("cc", "CC123");
            dataEntry1.put("cr", "CR456");
            dataEntry1.put("ct", "CT789");
            dataEntry1.put("raisonSocialeDestinataire", "Recipient Company");
            dataEntry1.put("retourBl", "Yes");
            dataEntry1.put("mntHt", "750 USD");
            dataEntry1.put("numFacture", "F7890");
            dataEntry1.put("status", "Delivered");
            dataEntry1.put("commentaireLivMobile", "Delivered on time");
            dataEntry1.put("dateCommentaireLivMobile", "2023-09-30");
            dataEntry1.put("nbrLad", "2");

            // Add the data entry to the list
            dataList.add(dataEntry1);            dataEntry1.put("numDeclaration", "12345");
            dataEntry1.put("sens", "Outbound");
            dataEntry1.put("dateEnvoi", "2023-09-28");
            dataEntry1.put("dateLivr", "2023-09-30");
            dataEntry1.put("origine", "Source");
            dataEntry1.put("destenation", "Destination");
            dataEntry1.put("colis", "5");
            dataEntry1.put("poids", "10 kg");
            dataEntry1.put("volume", "0.5 m³");
            dataEntry1.put("valeurDeclaree", "1000 USD");
            dataEntry1.put("cc", "CC123");
            dataEntry1.put("cr", "CR456");
            dataEntry1.put("ct", "CT789");
            dataEntry1.put("raisonSocialeDestinataire", "Recipient Company");
            dataEntry1.put("retourBl", "Yes");
            dataEntry1.put("mntHt", "750 USD");
            dataEntry1.put("numFacture", "F7890");
            dataEntry1.put("status", "Delivered");
            dataEntry1.put("commentaireLivMobile", "Delivered on time");
            dataEntry1.put("dateCommentaireLivMobile", "2023-09-30");
            dataEntry1.put("nbrLad", "2");

            // Add the data entry to the list
            dataList.add(dataEntry1);            dataEntry1.put("numDeclaration", "12345");
            dataEntry1.put("sens", "Outbound");
            dataEntry1.put("dateEnvoi", "2023-09-28");
            dataEntry1.put("dateLivr", "2023-09-30");
            dataEntry1.put("origine", "Source");
            dataEntry1.put("destenation", "Destination");
            dataEntry1.put("colis", "5");
            dataEntry1.put("poids", "10 kg");
            dataEntry1.put("volume", "0.5 m³");
            dataEntry1.put("valeurDeclaree", "1000 USD");
            dataEntry1.put("cc", "CC123");
            dataEntry1.put("cr", "CR456");
            dataEntry1.put("ct", "CT789");
            dataEntry1.put("raisonSocialeDestinataire", "Recipient Company");
            dataEntry1.put("retourBl", "Yes");
            dataEntry1.put("mntHt", "750 USD");
            dataEntry1.put("numFacture", "F7890");
            dataEntry1.put("status", "Delivered");
            dataEntry1.put("commentaireLivMobile", "Delivered on time");
            dataEntry1.put("dateCommentaireLivMobile", "2023-09-30");
            dataEntry1.put("nbrLad", "2");

            // Add the data entry to the list
            dataList.add(dataEntry1);
            dataList.add(dataEntry1);
            dataList.add(dataEntry1);
            dataList.add(dataEntry1);
            dataList.add(dataEntry1);
            dataList.add(dataEntry1);
            dataList.add(dataEntry1);
            dataList.add(dataEntry1);
            dataList.add(dataEntry1);
            dataList.add(dataEntry1);
            dataList.add(dataEntry1);
            dataList.add(dataEntry1);
            dataList.add(dataEntry1);
            dataList.add(dataEntry1);
            dataList.add(dataEntry1);
            dataList.add(dataEntry1);
        // Perform your database operation to get results
            List<Map<String, String>> results =dataList;
                    //
        */
            List<Map<String, String>> results= myService.GetDataFromOracle(Declartions);
            // Create a new Excel workbook and sheet
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Results");

            // Create a header row with the specified column names
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "ORIGINE", "CC", "VOLUME", "DATE_LIVR", "DESTENATION",
                    "COMMENTAIRE_LIV_MOBILE", "CR", "NUM_FACTURE", "SENS",
                    "VALEUR_DECLAREE", "CT", "STATUS", "NBRLAD", "POIDS",
                    "RETOUR_BL", "DATE_ENVOI", "RAISON_SOCIALE_DESTINATAIRE",
                    "DATE_COMMENTAIRE_LIV_MOBILE", "MNTHT", "NUM_DECLARATION", "COLIS"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Populate the Excel sheet with data
            int rowNum = 1;
            for (Map<String, String> result : results) {
                Row row = sheet.createRow(rowNum++);
                int cellNum = 0;
                for (String header : headers) {
                    Cell cell = row.createCell(cellNum++);
                    cell.setCellValue(result.getOrDefault(header, ""));
                }
            }

            // Set response headers for Excel file download
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=results.xlsx");

            // Write the workbook content to the response output stream
            try (OutputStream outputStream = response.getOutputStream()) {
                workbook.write(outputStream);
            } catch (IOException e) {
                // Handle exceptions, e.g., by logging or sending an error response
                e.printStackTrace();
            }
            return "success";
        }
}
