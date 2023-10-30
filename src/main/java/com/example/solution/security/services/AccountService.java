package com.example.solution.security.services;

import com.example.solution.Entites.Client;
import com.example.solution.security.entities.AppUser;

public interface AccountService {
    AppUser addNewUser(String username, String password, String email, String confirmPassword, Client client);
    AppUser addNewRole(String role);
    void addRoleToUser(String username,String role);
    void removeRoleFromUser(String username,String role);
    AppUser loadUserByUsername(String username);
}
