package com.dgpad.admin.user;

import com.lumosshop.common.entity.Role;
import com.lumosshop.common.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional

public class UserService {
    public static final int USERS_PER_PAGE = 4;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EntityManager entityManager;


    public User getByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }



    public List<User> findAll() {

        return (List<User>) userRepository.findAll();
    }

    public Page<User> listByPage(int pageNum, String sortField, String sortDirection,String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);

        if (keyword != null) {
            return userRepository.findAll(keyword, pageable);
        }
        return userRepository.findAll(pageable);
    }

    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null);

        if (isUpdatingUser) {
            User existingUser = userRepository.findById(user.getId()).get();
            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else encodePassword(user);
        } else {
            encodePassword(user);
        }
        return userRepository.save(user);

    }

    public User updateAccount(User userInTheForm) {
        User userInTheDB = userRepository.findById(userInTheForm.getId()).get();

        if (!userInTheForm.getPassword().isEmpty()) {
            userInTheDB.setPassword(userInTheForm.getPassword());
            encodePassword(userInTheDB);

        }
        if (userInTheForm.getPhotos() != null) {
            userInTheDB.setPhotos(userInTheForm.getPhotos());
        }
        userInTheDB.setFirstName(userInTheForm.getFirstName());
        userInTheDB.setLastName(userInTheForm.getLastName());

        return userRepository.save(userInTheDB);
    }
    public void deleteById(int id) throws UserNotFoundException {

        Long countById = userRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new UserNotFoundException("could not found this user id - " + id);
        }
        userRepository.deleteById(id);

    }

    public List<Role> roleList() {
        return (List<Role>) roleRepository.findAll();
    }


    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email) {

        // Check if the email already exists in the database
        User userByEmail = userRepository.getUserByEmail(email);

        if (userByEmail == null) return true; // The email is unique, return true

        boolean isCreatingNew = (id == null); // Check if creating a new user or updating an existing one

        if (isCreatingNew) {
            // Creating a new user, but the email already exists, return false
            return false;
        } else {
            // Updating an existing user, check if the email belongs to the same user
            return userByEmail.getId().equals(id); // Return true if the same user, false otherwise
        }

    }


    public User findUserById(int id) throws UserNotFoundException {
        try {
            User theuser = userRepository.findById(id).get();
            return theuser;

        } catch (NoSuchElementException e) {
            throw new UserNotFoundException("Could not found this user id :  " + id);
        }

    }

    public void UpdateUserStatus(Integer id, boolean status) {
        userRepository.updateEnableStatus(id, status);
    }


}
