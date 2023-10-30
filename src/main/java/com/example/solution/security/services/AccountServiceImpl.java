package com.example.solution.security.services;

import com.example.solution.Entites.Client;
import com.example.solution.security.entities.AppRole;
import com.example.solution.security.entities.AppUser;
import com.example.solution.security.repo.AppRoleRepository;
import com.example.solution.security.repo.AppUserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public AppUser addNewUser(String username, String password, String email, String confirmPassword, Client client) {
        AppUser appUser=appUserRepository.findByUsername(username);
        if(appUser!=null) throw new RuntimeException("this already Exist");
        if(!password.equals(confirmPassword)) throw new RuntimeException("Password not match");
        appUser= AppUser.builder()
                .userId(UUID.randomUUID().toString())
                .username(username)
                .password(passwordEncoder.encode(password)).
                email(email).
                client(client)
                .build();
        AppUser savedAppUser    = appUserRepository.save(appUser);
        return null;
    }

    @Override
    public AppUser addNewRole(String role) {
        AppRole appRole= appRoleRepository.findById(role).orElse(null);
        if(appRole!=null) throw new RuntimeException("This role already Exist");
        appRole=AppRole.builder()
                .role(role)
                .build();
        appRoleRepository.save(appRole);
        return null;
    }

    @Override
    public void addRoleToUser(String username, String role) {
        AppUser appUser=appUserRepository.findByUsername(username);
        AppRole appRole= appRoleRepository.findById(role).get();
        if (appUser.getRoles() == null) {
            appUser.setRoles(new ArrayList<>());
        }
        appUser.getRoles().add(appRole);
    }
    @Override
    public void removeRoleFromUser(String username, String role) {
        AppUser appUser=appUserRepository.findByUsername(username);
        AppRole appRole= appRoleRepository.findById(role).get();
        appUser.getRoles().remove(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }
}
