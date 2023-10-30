
package com.example.solution.security.repo;
import com.example.solution.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AppRoleRepository extends JpaRepository<AppRole,String> {

}


