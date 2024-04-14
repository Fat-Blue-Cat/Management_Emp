package com.ncc.manage_emp.testCascade;

import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.entity.WorkTime;
import com.ncc.manage_emp.entity_demo.Student;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CascadeTypesTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    @DirtiesContext
    public void testCascadePersist() {
        Users u = new Users();
        u.setRoleName("USER");
        u.setEmail("trung@gmail.com");
        u.setPassword("123");
        u.setName("trung");
        u.setUserName("trung");
        WorkTime w= new WorkTime();
        w.setUsers(u);

        entityManager.persist(w);
        entityManager.flush();
        assertNotNull(u.getId());
        assertNotNull(w.getId());


    }

    @Test
    @Transactional
    @DirtiesContext
    public void testCascadeMerge() {
        Users u = new Users();
        u.setRoleName("USER");
        u.setEmail("trung@gmail.com");
        u.setPassword("123");
        u.setName("trung");
        u.setUserName("trung");
        WorkTime w= new WorkTime();
        w.setUsers(u);

        entityManager.persist(w);
        entityManager.flush();

        u.setRoleName("EMPLOYEE");
        w.setUsers(u);

        entityManager.merge(w);
        entityManager.flush();

        assertEquals("EMPLOYEE", u.getRoleName());
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testCascadeRemove() {
        Users u = new Users();
        u.setRoleName("USER");
        u.setEmail("trung@gmail.com");
        u.setPassword("123");
        u.setName("trung");
        u.setUserName("trung");
        WorkTime w= new WorkTime();

        u.getWorkTimeList().add(w);

        entityManager.persist(u);
        entityManager.flush();
        entityManager.remove(u);
        entityManager.flush();

        assertNull(entityManager.find(Users.class, u.getId()));
        assertNull(entityManager.find(WorkTime.class, w.getId()));

    }

    @Test
    @Transactional
    @DirtiesContext
    public void testCascadeRefresh() {
        Users u = new Users();
        u.setRoleName("USER");
        u.setEmail("trung@gmail.com");
        u.setPassword("123");
        u.setName("trung");
        u.setUserName("trung");
        WorkTime w= new WorkTime();
        w.setUsers(u);

        entityManager.persist(w);
        entityManager.flush();

        u.setRoleName("EMPLOYEE");
        w.setUsers(u);

        entityManager.refresh(w);

        assertEquals("USER", u.getRoleName());
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testCascadeDetach() {
        Users u = new Users();
        u.setRoleName("USER");
        u.setEmail("trung@gmail.com");
        u.setPassword("123");
        u.setName("trung");
        u.setUserName("trung");
        WorkTime w= new WorkTime();
        w.setUsers(u);

        entityManager.persist(w);
        entityManager.flush();
        entityManager.detach(w);

        assertFalse(entityManager.contains(u));
        assertFalse(entityManager.contains(w));
    }


}
