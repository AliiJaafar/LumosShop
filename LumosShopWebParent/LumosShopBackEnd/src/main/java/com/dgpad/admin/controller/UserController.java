package com.dgpad.admin.controller;

import com.dgpad.admin.util.B2_Util;
import com.dgpad.admin.util.FileUploadUtil;
import com.dgpad.admin.user.UserNotFoundException;
import com.dgpad.admin.user.UserService;
import com.lumosshop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping(value = "/users")
    public String listFirstPage(Model model) {


        return listByPage(1, model, "id", "asc", null);
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum,
                             Model model,
                             @Param("sortField") String sortField,
                             @Param("sortDirection") String sortDirection,
                             @Param("keyword") String keyword ) {

        Page<User> page = userService.listByPage(pageNum, sortField, sortDirection,keyword);
        List<User> list = page.getContent();

        long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
        long endCount = startCount + UserService.USERS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElement", page.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("listUsers", list);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", reverseSortDirection);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moduleURL", "/users");


        return "users/users";


    }
    @GetMapping(value = "/users/add")
    public String showAddUserForm(Model model) {

        User user = new User();

        model.addAttribute("user", user);
        model.addAttribute("roles", userService.roleList());
        model.addAttribute("pageTitle", "Add New User");

        return "users/add-user";
    }

    @PostMapping("/users/saving")
    public String addUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes,
                          @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.save(user);


            String uploadDirectory = "user-photos/" + savedUser.getId();
/*
            FileUploadUtil.cleanDir(uploadDirectory);

            FileUploadUtil.saveFile(uploadDirectory, fileName, multipartFile);*/

            B2_Util.removeFolder(uploadDirectory);
            B2_Util.uploadFile(uploadDirectory, fileName, multipartFile.getInputStream());
        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.save(user);
        }


        redirectAttributes.addFlashAttribute("message", "The user has been successfully added to the system.");
        return getRedirectUrlToThetargetUser(user);
    }

    private static String getRedirectUrlToThetargetUser(User user) {
        String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDirection=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/users/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(name = "id") int theId,
                                    Model theModel, RedirectAttributes redirectAttributes) {
        try {
            User theUser = userService.findUserById(theId);
            // set user in the model to prepopulate the form
            theModel.addAttribute("user", theUser);
            theModel.addAttribute("roles", userService.roleList());
            theModel.addAttribute("pageTitle", "Edit User ID : ( " + theId + " )");
            // send over to our form
            return "users/add-user";
        } catch (UserNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());

            return "redirect:/users";
        }


    }

    @GetMapping("/users/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) throws UserNotFoundException {
        try {
            userService.deleteById(id);

            String ImageDir = "user-photos/" + id;
            B2_Util.removeFolder(ImageDir);

            redirectAttributes.addFlashAttribute("message", "User ID " + id + " has been deleted from the system with success.");
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        // redirect to
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String UpdateEnableStatus(@PathVariable(name = "id") Integer id, @PathVariable(name = "status") boolean enabled, RedirectAttributes redirectAttributes) {

        userService.UpdateUserStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "We've updated the status for user ID " + id + " to " + status + ".";
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/users";
    }
}
