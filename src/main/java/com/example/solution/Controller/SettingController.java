package com.example.solution.Controller;

import com.example.solution.Entites.Client;
import com.example.solution.Entites.ClientModel;
import com.example.solution.repositories.ClientModelRepository;
import com.example.solution.repositories.ClientRepository;
import com.example.solution.security.entities.AppRole;
import com.example.solution.security.entities.AppUser;
import com.example.solution.security.repo.AppRoleRepository;
import com.example.solution.security.repo.AppUserRepository;
import com.example.solution.security.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@Controller
@AllArgsConstructor
public class SettingController {
    private ClientModelRepository clientModelRepository;
    private ClientRepository clientRepository;
    private AppRoleRepository appRoleRepository;
    private AppUserRepository appUserRepository;
    private AccountService accountService;


    @GetMapping ("/admin/formClient")
    public String formClient(Model model){
        model.addAttribute("client",new Client());
        return "formClient";
    }
    @GetMapping ("/admin/formUser")
    public String formUser(Model model){
        List<AppRole> roles = appRoleRepository.findAll();
        List<Client> clients =(List<Client>) clientRepository.findAll();

        model.addAttribute("roles", roles);
        model.addAttribute("clients", clients);

        model.addAttribute("user",new AppUser());
        model.addAttribute("role",new AppRole());
        return "formUser";
    }
    @PostMapping("/admin/clients")
    public String createClient(@ModelAttribute Client client) {
        clientRepository.save(client);
        System.out.println(client);
        return "formUser";
    }
    @PostMapping("/admin/users")
    public String createUser(@ModelAttribute AppUser user) {
        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        Client client = user.getClient();
        String numClient= client.getCpk();
        try {
            Optional<Client> clientOptional = clientRepository.findById(numClient);

            if (clientOptional.isPresent()) {
                 client = clientOptional.get();
                // Perform operations with the 'client' object
            } else {
                // Handle the case where the client is not found (e.g., return an error message).
                System.out.println("Client with ID " + numClient + " not found.");
            }
        } catch (Exception e) {
            // Handle any exceptions that might occur during the database operation.
            System.err.println("An error occurred: " + e.getMessage());
        }
        System.out.println(client);
        List<AppRole> roles = user.getRoles();
        accountService.addNewUser(username,password,email,password,client);
        for (AppRole role : roles) {
            System.out.println(role.getRole());
            accountService.addRoleToUser(username,role.getRole());
        }
        return "success";
    }


    @GetMapping("/admin/Setting")
    public String uploadSetting(Model model) {
        List<Client> clients = (List<Client>)clientRepository.findAll(); // Retrieve data from the database

        model.addAttribute("yourObjectList", clients);
        return "Setting";
    }

    @PostMapping("/admin/processForm")
    public String processForm(@RequestParam Map<String, String> formData) {
        String clientKeyAndName = formData.get("clientName");
        formData.remove("clientName");
        System.out.println(clientKeyAndName);
        String[] elements = clientKeyAndName
                .substring(0, clientKeyAndName.length() ) // Remove square brackets
                .split(":"); // Split by comma and space
        String clientKey=elements[0];
        String clientName=elements[1];
        Optional<Client> clientOptional=clientRepository.findById(clientKey);
        Client client =clientOptional.get();
        // Create a new ClientModel instance
        System.out.println(clientKey);
        //list of Client Model
        List<ClientModel> clientModelList=new ArrayList<>();
        // Create a new ClientModel instance

        formData.forEach((key, value) -> {
                System.out.println(key + "" + value);
                ClientModel clientModel = new ClientModel();
                clientModel.setFieldName(value);
                clientModel.setNumCell(key);
                clientModel.setClient(client);
                // Save the ClientModel in the database
                clientModelRepository.save(clientModel);
        });

        // Redirect or return a response as needed
        return "redirect:/admin/field/"+clientKey;
    }

    @GetMapping("/admin/field/{clientKey}")
    public String getField(@PathVariable String clientKey, Model model) {

        List<ClientModel>  clientModels= clientModelRepository.findByClient_Cpk(clientKey);
        Map<Long,String> modelClient= new TreeMap<>();
        for (ClientModel clientModel:clientModels) {
            Client client=clientModel.getClient();
            String fieldName=clientModel.getFieldName();
            Long clientModelPk=clientModel.getClientModelPk();
            modelClient.put(clientModelPk,fieldName);
        }
        List<String> namesList = Arrays.asList(
                "ramasseur", "declaration", "dest_code", "dest_libelle", "adresse",
                "telephone", "nature", "fond", "origin", "destination", "colis",
                "poids", "volume", "valeur_dec", "livraison", "port", "bl", "num_bl",
                "facture", "num_facture", "catProduit", "ps", "camion", "numero","other"
        );
        model.addAttribute("clientKey", clientKey);
        model.addAttribute("clientModels", clientModels);
        model.addAttribute("modelClient", modelClient);
        model.addAttribute("namesList", namesList);
        return "field-list";
    }


    @PostMapping("/admin/process-names")
    public String processNames(@RequestParam("selectedNames") String[] selectedNames, @RequestParam("clientKey") String clientKey) {
        System.out.println("hello :"+clientKey);
        List<ClientModel>  clientModels = clientModelRepository.findByClient_Cpk(clientKey);
        int i=0;
        for (ClientModel clientModel:clientModels) {
            System.out.println(selectedNames[i]);
            clientModel.setFieldCarreName(selectedNames[i]);
            clientModelRepository.save(clientModel);
            i++;
        }
        return "redirect:/admin/Setting";
    }
}
