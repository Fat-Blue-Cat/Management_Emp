package com.ncc.manage_emp.repository;

import com.ncc.manage_emp.entity.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(classes = {com.ncc.manage_emp.ManageEmpApplication.class})
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        Users users1 = Users.builder()
                .name("trung")
                .email("trungvu0002@gmail.com")
                .userName("trung")
                .password("$2a$10$OVYyHqp8Wtm3zNhleMi.le1v2Aq0tE8eBfC/Krg8cM3gUtgtpHTOq").build();
        userRepository.save(users1);

        Users users2 = Users.builder()
                .name("user2")
                .email("user2@gmail.com")
                .userName("user2")
                .password("$2y$10$/YwTHmvfSu.yKFjE9rfjoOJXJ9kC3FFcOvvEFKpuq6USTbchS4Ile").build();
        userRepository.save(users2);

        // Add more Users as needed
    }

    @Test
    public void UserRepository_FindById_ThenReturnData(){
        Optional<Users> u = userRepository.findById(1l);
        Assertions.assertThat(u.get()).isNotNull();
    }

    @Test void UserRepository_FindByName_ThenReturnData(){
        Sort sortByName = Sort.by("name");

        Pageable pageable = PageRequest.of(0, 3, sortByName);
        Page<Users> usersPage = userRepository.findByName("trung", pageable);

        Assertions.assertThat(usersPage.getContent()).isNotNull();
        Assertions.assertThat(usersPage.getContent().size()).isEqualTo(2);
    }

    @Test void UserRepository_existsByEmail_ThenReturnData(){

        Boolean email = userRepository.existsByEmail("trungvu0002@gmail.com");

        Assertions.assertThat(email).isEqualTo(true);
    }


    @Test void UserRepository_updateRoleUser_ThenReturnData(){

        Users users = userRepository.getUserById(1l);
        users.setRoleName("ADMIN");

        Users userSaved = userRepository.save(users);

        Assertions.assertThat(userSaved.getRoleName()).isEqualTo("ADMIN");
    }
}
