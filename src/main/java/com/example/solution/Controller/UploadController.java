package com.example.solution.Controller;
import com.example.solution.Entites.Client;
import com.example.solution.Entites.Mapping;
import com.example.solution.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class UploadController {

    @Autowired
    private static SettingCode settingCode;
    @Autowired
    private ClientRepository clientRepository; // Inject the repository

    public UploadController(SettingCode settingCode) {
        this.settingCode = settingCode;
    }

    @GetMapping("/admin/setting")
    public String uploadForm(Model model) {
        List<Client> clients = (List<Client>)clientRepository.findAll(); // Retrieve data from the database
        model.addAttribute("yourObjectList", clients);
        return "upload";
    }

    @PostMapping("/admin/upload")
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file,@RequestParam("clientName") String clientName) {
        System.out.println(clientName);
        Optional<Client> clientOptional = clientRepository.findById(clientName);
        Client client=clientOptional.get();

        ModelAndView modelAndView = new ModelAndView();
        if (!file.isEmpty()) {
            try {
                List<Mapping> mappingList = settingCode.readExcelFile(file.getInputStream(),client);
                modelAndView.addObject("destinationList", mappingList);
                modelAndView.setViewName("success");
            } catch (IOException e) {
                e.printStackTrace();
                modelAndView.addObject("error", "An error occurred while processing the file.");
                modelAndView.setViewName("upload");
            }
        } else {
            modelAndView.addObject("error", "Please select a file to upload.");
            modelAndView.setViewName("upload");
        }
        return modelAndView;
    }
}
