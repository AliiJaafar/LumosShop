package com.dgpad.admin.user;

import com.lumosshop.common.entity.Role;
import com.lumosshop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll() {
        return (List<Role>) roleRepository.findAll();
    }
}
