package com.example.solution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SolutionApplication  extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SolutionApplication.class, args);
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
		return application. sources (SpringApplication.class);
	}
	public void run(String... args) throws Exception {

        /*
        AkzonobelData akzonobelData = new AkzonobelData("95757740","ALUMINIUM DU MAROC  SA","","","TANGER","MA0101",294.7);
        akzRepository.save(akzonobelData);
        // Use the DataMapper to map AkzonobelData to UnifiedData
        Command unifiedData = DataMapper.INSTANCE.map(akzonobelData);
        */
	}
	//@Bean
   /* CommandLineRunner commandLineRunnerUserDetais(AccountService accountService){
       Client client=new Client("1","Akzonobel","1");
       // Client client2=new Client("2","wafacach","3");
        clientRepository.save(client);
        clientRepository.save(client2);
        return args -> {
           accountService.addNewRole("USER");
           accountService.addNewRole("ADMIN");
           accountService.addNewUser("user1","1234","user1@gamil.com","1234",client);
           accountService.addNewUser("user2","1234","user2@gamil.com","1234",client2);
           accountService.addNewUser("admin","1234","user3@gamil.com","1234",null);

           accountService.addRoleToUser("user1","USER");
           accountService.addRoleToUser("user2","USER");
           accountService.addRoleToUser("admin","ADMIN");
        };
    }*/
	@RequestMapping(value="/hello")
	public String helloWorld(){
		return "Hello World, Peter";
	}
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

}
