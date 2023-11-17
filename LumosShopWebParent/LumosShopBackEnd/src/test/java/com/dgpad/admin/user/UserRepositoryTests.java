package com.dgpad.admin.user;

import com.lumosshop.common.entity.Role;
import com.lumosshop.common.entity.User;
import org.hibernate.query.criteria.JpaCriteriaUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateUser() {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Role adminRole = testEntityManager.find(Role.class, 1);
        User user1 = new User("user1@admin.com", passwordEncoder.encode("10119149"), "First", "Admin");
        user1.addRole(adminRole);
        userRepository.save(user1);
    }
    @Test
    public void testCreateNewUserWithTwoRoles() {
        Role salesPersonRole = testEntityManager.find(Role.class, 2);
        Role editorRole = testEntityManager.find(Role.class, 3);
        Role ShipperRole = testEntityManager.find(Role.class, 4);
        Role AssistanceRole = testEntityManager.find(Role.class, 5);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User user2 = new User("Abasiii@outlook.com", passwordEncoder.encode("10119149"), "Abas", "Al-Mosawi");
        User user3 = new User("baqirii@outlook.com", passwordEncoder.encode("10119149"), "Abas", "Al-Mosawi");
        user2.addRole(ShipperRole);
        user2.addRole(AssistanceRole);
        userRepository.save(user2);
        userRepository.save(user3);


    }
    @Test
    public void testListAllUsers() {
        Iterable<User> listUsers = userRepository.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }
    @Test
    public void testGetUserById() {
        User userById = userRepository.findById(2).get();
        System.out.println(userById);
        assertThat(userById).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User userUpdateUserDetails = userRepository.findById(1).get();
        userUpdateUserDetails.setEnabled(true);
        userUpdateUserDetails.setFirstName("Qassim");

        userRepository.save(userUpdateUserDetails);
    }
    @Test
    public void testUpdateUserRoles() {
        User userUpdateUserRoles = userRepository.findById(2).get();
        Role roleAssistant = new Role(5);
        Role roleSalesperson = new Role(2);

        userUpdateUserRoles.getRoles().remove(roleSalesperson);
        userUpdateUserRoles.addRole(roleAssistant);

        userRepository.save(userUpdateUserRoles);
    }
    @Test
    public void testDeleteUser() {
        userRepository.deleteById(12);
        userRepository.deleteById(13);

    }

    @Test
    public void testGetUserByEmail() {
        String email = "LumosEnergy@outlook.com";
        User user00 = userRepository.getUserByEmail(email);
        System.out.println(user00);

        assertThat(user00).isNotNull();
    }

    @Test
    public void testCountById() {
        Integer id = 7;
        Long countById = userRepository.countById(id);

        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testEnableUser() {
        Integer id = 1;
        userRepository.updateEnableStatus(id, true);
    }
    @Test
    public void testDisableUser() {
        Integer id = 11;
        userRepository.updateEnableStatus(id, false);
    }
    @Test
    public void testListFirstPage() {
        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isEqualTo(pageSize);
    }
    @Test
    public void testSearchUser() {
        String keyWord = "Lumos";
        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(keyWord,pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isGreaterThan(0);
    }


}
