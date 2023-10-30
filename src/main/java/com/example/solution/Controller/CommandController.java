package com.example.solution.Controller;


import com.example.solution.Entites.Command;
import com.example.solution.OracleData.MyService;
import com.example.solution.repositories.CommandRepository;
import com.example.solution.security.repo.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class CommandController {
    CommandRepository commandRepository;
    private final MyService myService;
    private AppUserRepository appUserRepository;


    @GetMapping("/user/index")
    public String index(Model model
            , @RequestParam(name = "page",defaultValue = "0") int page
            , @RequestParam(name = "size",defaultValue = "6")int size
            , @RequestParam(name = "keyword",defaultValue = "") String kw ){




        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        Page<Command> pageCommands=commandRepository.findByUsernameAndDeclarationContains(username,kw,PageRequest.of(page,size));
        String  userId= appUserRepository.findUserIdByUsername(username) ;
        ArrayList<String> Declarations=commandRepository.findDeclarationsByUserId(userId);
        List<Map<String, String>> results= myService.GetDataFromOracle(Declarations);
        System.out.println(results);

        model.addAttribute("commandList",pageCommands.getContent());
        model.addAttribute("results",results);
        model.addAttribute("pages",new int[pageCommands.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",kw);
        return "commands";
    }
    @GetMapping("/hello")
    public String hello(){
        return "template1";
    }
}
