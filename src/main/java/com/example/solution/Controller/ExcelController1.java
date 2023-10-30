package com.example.solution.Controller;

import com.example.solution.Entites.Client;
import com.example.solution.Entites.ClientModel;
import com.example.solution.Entites.Command;
import com.example.solution.Entites.UploadedFile;
import com.example.solution.repositories.*;
import com.example.solution.security.entities.AppUser;
import com.example.solution.security.repo.AppUserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static com.example.solution.Controller.SettingCode.getCellValue;

@Controller
public class ExcelController1 {
    @Autowired
    private  UnifiedRepository destinationRepository ;
    @Autowired
    private UploadedFileRepository uploadedFileRepository ;
    @Autowired
    private ClientModelRepository clientModelRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private MappingRepository mappingRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    // Get the currently authenticated user's Authentication object

    XSSFWorkbook workbook;
    XSSFSheet sheet;
    Date currentDate = new Date();

    @GetMapping("/user/upload-form")
    public String uploadForm() {
        return "upload-form";
    }
    @GetMapping("/")
    public String home() {
        return "WelcomePage";
    }
    @GetMapping("/user/profile")
    public String viewProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Get the username from the Authentication object
        String username = authentication.getName();
        String numClient=appUserRepository.findCpkClientByUsername(username);
        System.out.println("profile");
        Optional<Client> clientOptional = clientRepository.findById(numClient);
        Client client =clientOptional.get();
        model.addAttribute("client", client);
        return "Profile";
    }
    @PostMapping("/user/read-excel")
    public String uploadFile(@RequestParam("file") MultipartFile file, HttpSession session, Model model) throws Exception {
        System.out.println("find the problem here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        UploadedFile uploadedFile = new UploadedFile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Get the username from the Authentication object
        String username = authentication.getName();

        AppUser user = appUserRepository.findByUsername(username);
        System.out.println(username);
        String numClient=appUserRepository.findCpkClientByUsername(username);

        Optional<Client> clientOptional = clientRepository.findById(numClient);
        Client client =clientOptional.get();
        int numDebut=Integer.parseInt(clientRepository.findNumDebutByCpk(numClient));
        String charFin=clientRepository.findcarctereFinByCpk(numClient);
        List<ClientModel> clientModels = clientModelRepository.findByClient_Cpk(numClient);
        Map<String, String> fieldsCarre = new HashMap<>();
        for (ClientModel clientModel:clientModels){
            String numCell = clientModel.getNumCell();
            String valueCarre =clientModel.getFieldCarreName();
            fieldsCarre.put(numCell,valueCarre);
            System.out.println(fieldsCarre);
        }
        List<String> arrayList = new ArrayList<>();
        int n=0;
        for (String key : fieldsCarre.keySet()) {
            String value = fieldsCarre.get((n+1)+"");
            System.out.println("//////  "+value);
            arrayList.add(n,value);
            n++;
        }
        System.out.println(arrayList);

        //String[] elmts ={"num_bl", "dest_libelle", "adresse", "telephone", "destination", "dest_code", "poids"};
        if (!file.isEmpty()){
            try {
                String fileExtension = getFileExtension(file.getOriginalFilename());
                uploadedFile.setFileName(file.getOriginalFilename());
                uploadedFile.setFileType(fileExtension);
                uploadedFile.setFileData(file.getBytes());
                uploadedFile.setRegistrationDate(currentDate);
                uploadedFile.setUser(user);
                System.out.println("hellooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
                uploadedFileRepository.save(uploadedFile);
                workbook = new XSSFWorkbook(file.getInputStream());
                sheet = workbook.getSheetAt(0);
                List<Command> commandList = new ArrayList<>();
                Command command=null;
                int numRow= 0;
                for (Row row : sheet) {
                    int cellnum = 0;
                    String first= (String) getCellValue(row.getCell(0));
                    if(charFin.equals(first)){
                        break;
                    }
                    System.out.println("$$$$$$$$$$$$");
                    System.out.println(first);
                    System.out.println(numRow);
                    System.out.println("$$$$$$$$$$$$");
                    if(numRow>=numDebut-1){
                        System.out.println("radeeeeeeeeeeeeeeeeeeeeeeeeeeeezzzzzzzzzzzzzzzzzzzzzzzzzzzt");
                        command = new Command();
                        command.setClient(client);
                        command.setUser(user);
                        for (Cell cell : row) {
                            String cellValue;
                            if (getCellValue(cell) == null) {
                                cellValue = "";
                            } else {
                                cellValue = getCellValue(cell).toString();
                            }
                            System.out.println("cellnum "+cellnum+"array lenght"+arrayList.toArray().length);
                            if (cellnum <= arrayList.toArray().length - 1) {
                                System.out.println("radeeeeeeeeeeeeeeeeeeeeeeeeeeeezzzzzzzzzzzzzzzzzzzzzzzzzzzt22222222222222222222é");

                                System.out.println(cellnum <= arrayList.toArray().length);
                                String fieldCarre = arrayList.get(cellnum);
                                System.out.println(fieldCarre);
                                int contSlash = countOccurrences(cellValue, '/');
                                boolean containsSlash = cellValue.contains("/");
                                switch (fieldCarre) {
                                    case "ramasseur":
                                        command.setRamasseur(cellValue);
                                        System.out.println("ramasseur: " + cellValue);
                                        break;
                                    case "declaration":
                                        command.setDeclaration(cellValue);
                                        System.out.println("declaration: " + cellValue);
                                        break;
                                    case "dest_code":
                                        if (cell.getCellType() == CellType.NUMERIC) {
                                            double numericValue = cell.getNumericCellValue();
                                            int intValue = (int) numericValue; // Cast the double to an int
                                            String codeDest = String.valueOf(intValue);
                                            command.setDest_code(codeDest);
                                        } else if(cellValue=="") {
                                            System.out.println("sdklfjfj lkjsd mlkjds flkj sdflkj fdslkj sdlkkj test ttest test taaaaaa");
                                            command.setDest_code("0");
                                        } else {
                                            command.setDest_code(cellValue);
                                        }
                                        System.out.println("dest_code: " + cellValue);
                                        try {
                                            String codeCarre = mappingRepository.findCodeDestCarreByCodeDestDestLike(cellValue);
                                            System.out.println("check herrrrrrrrrrrrrrr"+codeCarre);
                                            if (codeCarre==null){
                                                command.setDestination("0");

                                            }else{
                                                System.out.println("here is the prolem:*****" + codeCarre);
                                                command.setDestination(codeCarre);
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case "dest_libelle":
                                        command.setDest_libelle(cellValue);
                                        System.out.println("dest_libelle: " + cellValue);
                                        break;
                                    case "adresse":
                                        command.setAdresse(cellValue);
                                        System.out.println("adresse: " + cellValue);
                                        break;
                                    case "telephone":
                                        command.setTelephone(cellValue);
                                        System.out.println("telephone: " + cellValue);
                                        break;
                                    case "nature":
                                        command.setNature(cellValue);
                                        System.out.println("nature: " + cellValue);
                                        break;
                                    case "fond":
                                        if (cellValue == "0") {
                                            command.setNature("s");
                                        }
                                        command.setFond(cellValue);
                                        System.out.println("fond: " + cellValue);
                                        break;
                                    case "origin":
                                        command.setOrigin(cellValue);
                                        System.out.println("origin: " + cellValue);
                                        break;
                                    case "destination":
                                        //Mapper la destination du client avec carre
                                        //command.setDestination(cellValue);
                                        //System.out.println("destination: " + cellValue);
                                        break;
                                    case "colis":
                                        command.setColis(cellValue);
                                        System.out.println("colis: " + cellValue);
                                        break;
                                    case "poids":
                                        command.setPoids(cellValue);
                                        System.out.println("poids: " + cellValue);
                                        break;
                                    case "volume":
                                        command.setVolume(cellValue);
                                        System.out.println("volume: " + cellValue);
                                        break;
                                    case "valeur_dec":
                                        command.setValeur_dec(cellValue);
                                        System.out.println("valeur_dec: " + cellValue);
                                        break;
                                    case "livraison":
                                        command.setLivraison(cellValue);
                                        System.out.println("livraison: " + cellValue);
                                        break;
                                    case "port":
                                        command.setPort(cellValue);
                                        System.out.println("port: " + cellValue);
                                        break;
                                    case "bl":
                                        if (cellValue != null) {
                                            command.setBl("1");
                                            if (containsSlash) {
                                                command.setNum_bl(String.valueOf(contSlash + 1));
                                            } else {
                                                command.setNum_bl("1");
                                            }
                                        } else {
                                            command.setBl("0");
                                            command.setNum_bl("0");
                                        }
                                        System.out.println("num_bl: " + cellValue);
                                        break;
                                    case "num_bl":

                                        break;
                                    case "facture":

                                        if (cellValue != null) {
                                            command.setFacture("1");
                                            if (containsSlash) {
                                                command.setNum_facture(String.valueOf(contSlash + 1));
                                            } else {
                                                command.setNum_facture("1");
                                            }
                                        } else {
                                            command.setFacture("0");
                                            command.setNum_facture("0");
                                        }
                                        System.out.println("facture: " + cellValue);
                                        break;
                                    case "num_facture":
                                        command.setNum_facture(cellValue);
                                        System.out.println("num_facture: " + cellValue);
                                        break;
                                    case "catProduit":
                                        command.setCatProduit(cellValue);
                                        System.out.println("catProduit: " + cellValue);
                                        break;
                                    case "ps":
                                        command.setPs(cellValue);
                                        System.out.println("ps: " + cellValue);
                                        break;
                                    case "camion":
                                        command.setCamion(cellValue);
                                        System.out.println("camion: " + cellValue);
                                        break;
                                    case "numero":
                                        command.setNumero(cellValue);
                                        System.out.println("numero: " + cellValue);
                                        break;
                                    case "other":
                                        break;
                                    default:
                                        command.setNumero("null");
                                        break;
                                }
                                cellnum++;
                            }
                        }
                        if(command.getFond()=="0"){
                            command.setNature("S");
                        }
                        commandList.add(command);
                    }
                    numRow++;
                }
                destinationRepository.saveAll(commandList);
                workbook.close();
                // Add the Excel data to the model
                model.addAttribute("excelData", commandList);
                // Store data in session
                session.setAttribute("myDataObject", commandList);
                session.setAttribute("uploadedFile", uploadedFile);
                return "result";
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("message", "File upload failed!");
            }
        } else {
            model.addAttribute("message", "Please select a file to upload.");
        }
        return "redirect:/user/transfer";
    }
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return fileName.substring(lastDotIndex);
        }
        return ""; // If no extension found
    }
    public static int countOccurrences(String text, char target) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c == target) {
                count++;
            }
        }
        return count;
    }

    public static boolean rowMatchesClass(Row row, Class<?> targetClass) {
        Field[] fields = targetClass.getDeclaredFields();
        // System.out.println(row.getLastCellNum()+"////"+fields.length);
        // System.out.println(row);
        int fieldNum = 0;
        if (row == null ) {
            return false;
        } else if (row.getLastCellNum()<fields.length) {
            return false;
        }
        for (Cell cell : row) {
            if(fieldNum<fields.length){
                break;
            }
            String fieldName = fields[fieldNum].getName();
            Class<?> fieldType = fields[fieldNum].getType();
            CellType cellType = cell.getCellType();
            System.out.println(cellType+"//////"+fieldType+"   "+fieldName);
            String cellValue = cell.getStringCellValue();
            System.out.println("cell value"+cellValue);
            if (!isCellTypeMatching(cell, fieldType)) {
                // Cell type does not match field type
                return false;
            }
            fieldNum++;
        }
        // All cells in the row match the fields in the class
        return true;
    }
    private static boolean isCellTypeMatching(Cell cell, Class<?> fieldType) {
        CellType cellType = cell.getCellType();
        String cellValue = cell.getStringCellValue();

        if (cellType == CellType.FORMULA) {
            System.out.println(cellValue+"i pass this step");
            return true;
        } else if (cellType == CellType.BLANK) {
            System.out.println(cellValue+"i pass this step");
            return true;
        } else if (cellType == CellType.NUMERIC) {
            if (fieldType == double.class || fieldType == Double.class) {
                System.out.println(cellValue+"i pass this step");
                return true;
            } else if (fieldType == Integer.class || fieldType == int.class) {
                System.out.println(cellValue+"i pass this step");
                return true;
            } else if (fieldType == String.class) {
                // Try to parse the numeric cell as a String
                try {
                    double numericValue = cell.getNumericCellValue();
                    System.out.println(cellValue+"i pass this step");
                    return true; // Parsing successful
                } catch (IllegalStateException e) {
                    // Handle the parsing error, for example, set the field to a default value
                    return false;
                }
            }
        } else if (cellType == CellType.STRING) {
            if (fieldType == String.class) {
                System.out.println(cellValue+"i pass this step");

                return true;
            } else if (fieldType == double.class || fieldType == Double.class) {
                // Try to parse the string as a double
                try {
                    double parsedValue = Double.parseDouble(cellValue);
                    System.out.println(cellValue+"i pass this step");
                    return true; // Parsing successful
                } catch (NumberFormatException e) {
                    System.out.println(cellValue+"can't pass this step");
                    // Handle the parsing error, for example, set the field to a default value
                    return false;
                }
            } else if (fieldType == Integer.class || fieldType == int.class) {
                // Try to parse the string as an int
                try {
                    double numericValue = Double.parseDouble(cellValue);
                    int intValue = (int) numericValue;
                    System.out.println(cellValue+"i pass this step");
                    return true; // Parsing successful
                } catch (NumberFormatException e) {
                    // Handle the parsing error, for example, set the field to a default value
                    System.out.println(cellValue+"can't pass this step");
                    return false;
                }
            }
        }


        else if (cellType == CellType.BOOLEAN) {
            if (fieldType == boolean.class || fieldType == Boolean.class) {
                System.out.println(cellValue+"i pass this step");
                return true;
            }
        }
        System.out.println(cellValue+"can't pass this step");
        // Add more type mappings as needed
        return false;
    }

    private Object createDataObjectFromRow(Field[] fields, Row row) {
        Object dataObject = new Command(); // Replace with your actual data class
        int cellNum = 0;

        for (Field field : fields) {
            field.setAccessible(true);

            Cell cell = row.getCell(cellNum);
            Object cellValue = getCellValue(cell);

            try {

                if (cell == null || cell.getCellType() == CellType.BLANK) {
                    // Handle empty cells
                    if (field.getType() == String.class) {
                        field.set(dataObject, "");
                    } else {
                        field.set(dataObject, 0);
                    }
                } else {
                    if (field.getType() == String.class) {
                        field.set(dataObject, cellValue.toString());
                    } else if (field.getType() == Integer.class || field.getType() == int.class) {
                        if (cellValue instanceof Number) {
                            field.set(dataObject, ((Number) cellValue).intValue());
                        }
                    } else if (field.getType() == Double.class || field.getType() == double.class) {
                        if (cellValue instanceof Number) {
                            field.set(dataObject, ((Number) cellValue).doubleValue());
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            cellNum++;
        }
        return dataObject;
    }


    private Field[] setObjectDataInRow(Object obj, Row row) {
        Field[] fields = obj.getClass().getDeclaredFields();
        int cellNum = 0;
        for (Field field : fields) {
            String fieldName = field.getName();

            Class<?> fieldType = field.getType();

            System.out.println("Field Name: " + fieldName);
            System.out.println("Field Type: " + fieldType);
        }
        for (int i = 1; i < fields.length-3; i++) {
            Field field = fields[i];

            try {
                field.setAccessible(true);
                Object value = field.get(obj);

                Cell cell = row.createCell(cellNum++);

                if (value != null) {
                    if (field.getType() == int.class || field.getType() == Integer.class) {
                        cell.setCellValue((Integer) value);
                    } else if (field.getType() == double.class || field.getType() == Double.class) {
                        cell.setCellValue((Double) value);
                    } else if (field.getType() == char.class || field.getType() == Character.class) {
                        cell.setCellValue(value.toString());
                    } else {
                        cell.setCellValue(value.toString());
                    }
                } else {
                    cell.setCellValue("");
                }
            } catch (IllegalAccessException e) {

            }
        }

        return fields;
    }


    @GetMapping("/user/transfer")
    public void downloadExcel(HttpServletResponse response, HttpSession session) throws IOException {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Result");
        UploadedFile uploadedFile = (UploadedFile)session.getAttribute("uploadedFile");
        System.out.println("upload file name : "+uploadedFile.getFileName()+"   "+uploadedFile.getId());
        List<Command> commandList = (List<Command>)session.getAttribute("myDataObject");
        int rowNum = 0;
        Row row;
        for (Command obj : commandList) {
            row = sheet.createRow(rowNum++);
            setObjectDataInRow(obj, row);
        }
        row = sheet.createRow(0);
        int cellNum=0;
        // Create a List to represent the row
        String[] values = {"Ramasseur", "Declaration", "Dest_Code","Dest_libelle", "adresse", "telephone","Nature", "fond", "Origine","Destination","Colis", "Poids","Volume", "Valeur_Dec", "Livraison","Port", "BL", "Num_BL", "Facture","Num_Facture", "catProduit","PS","type", "Camion","numero"};
        // Populate the row with values from the array
        for (String value : values) {
            Cell cell = row.createCell(cellNum);
            cell.setCellValue(value);
            cellNum++;        }
        // Set response headers
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=canvea.xlsx");
        // Write the workbook content to the response
        workbook.write(response.getOutputStream());
        // Convert the workbook to a byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        byte[] fileBytes = byteArrayOutputStream.toByteArray();
        // Save the file to the database
        uploadedFile.setFileCarre(fileBytes);
        uploadedFileRepository.save(uploadedFile);
    }
}
