package com.example.solution.OracleData;

import com.example.solution.repositories.CommandRepository;
import com.example.solution.security.repo.AppUserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class OracleController {
    private final MyService myService;
    private CommandRepository commandRepository;
    private AppUserRepository appUserRepository;

    @PostMapping("/user/oracle-operation/{numDeclaration}")
    public void performDatabaseOperation(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Get the username from the Authentication object
        String username = authentication.getName();
        String  userId= appUserRepository.findUserIdByUsername(username) ;
        ArrayList<String> Declarations=commandRepository.findDeclarationsByUserId(userId);
        List<Map<String, String>> results= myService.GetDataFromOracle(Declarations);
        System.out.println(results);
        session.setAttribute("myDataObject", results);
    }
}

