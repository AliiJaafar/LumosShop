package com.dgpad.admin.user;

import com.lumosshop.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository roleRepository;



    @Test
    public void testCreateFirstRole() {
        Role roleAdmin = new Role("Admin", "manage everything");
        Role savedRole = roleRepository.save(roleAdmin);
        assertThat(savedRole.getId()).isGreaterThan(0);

    }

    @Test
    public void testCreateRestRoles() {
        List<Role> roleList = new ArrayList<>();
        Role roleSalesperson = new Role("Salesperson", "Handles product pricing, customers, orders, and sales reports.");
        Role roleEditor = new Role("Editor", "Manages categories, brands, products, articles, and menus.");
        Role roleShipper = new Role("Shipper", "Views products, orders, and updates order status.");
        Role roleAssistant = new Role("Assistant", "Oversees questions and reviews");

        roleList.add(roleSalesperson);
        roleList.add(roleEditor);
        roleList.add(roleShipper);
        roleList.add(roleAssistant);
        roleRepository.saveAll(roleList);
    }

}
