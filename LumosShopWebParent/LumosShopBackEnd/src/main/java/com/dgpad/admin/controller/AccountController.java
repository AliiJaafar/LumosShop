package com.dgpad.admin.controller;

import com.dgpad.admin.util.FileUploadUtil;
import com.dgpad.admin.security.LumosUserDetails;
import com.dgpad.admin.user.UserService;
import com.lumosshop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;


    @GetMapping("/account")
    public String viewAccountDetails(@AuthenticationPrincipal LumosUserDetails loggedUser,
                                     Model model) {
        String email = loggedUser.getUsername();
        User user = userService.getByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.roleList());

        return "users/account-details";
    }
    @PostMapping("/account/update")
    public String UpdateUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes,
                          @RequestParam("image") MultipartFile multipartFile,
                             @AuthenticationPrincipal LumosUserDetails loggedUser) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.updateAccount(user);


            String uploadDirectory = "user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDirectory);

            FileUploadUtil.saveFile(uploadDirectory, fileName, multipartFile);
        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.updateAccount(user);
        }


        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());

        redirectAttributes.addFlashAttribute("message", "We've updated your account details as requested.");
        return "redirect:/account";

    }
}
