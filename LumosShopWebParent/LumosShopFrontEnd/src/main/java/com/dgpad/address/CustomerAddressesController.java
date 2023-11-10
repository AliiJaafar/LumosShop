package com.dgpad.address;

import com.dgpad.Utility;
import com.dgpad.customer.CustomerService;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.CustomerAddresses;
import com.lumosshop.common.entity.control.Nation;
import com.lumosshop.common.exception.CustomerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CustomerAddressesController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerAddressesService addressService;

    private Customer isTheCustomerAuthenticate(HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        String email = Utility.fetchCustomerEmailFromAuthSource(httpServletRequest);
        if (email == null) {
            throw new CustomerNotFoundException("Visitor");
        }
        return customerService.getCustomerByEmail(email);
    }

    @GetMapping("/addresses/edit/{id}")
    public String editAddress(@PathVariable("id") Integer addressId, Model model,
                              HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        Customer customer = isTheCustomerAuthenticate(httpServletRequest);
        List<Nation> nationList = customerService.nationList();

        CustomerAddresses address = addressService.retrieveAddress(customer.getId(), addressId);

        model.addAttribute("address", address);
        model.addAttribute("nationList", nationList);
        model.addAttribute("pageTitle", "Manage Address - ID: " + addressId + " -");
        model.addAttribute("title", "Manage - ID: " + addressId + " -");

        return "addresses/form";
    }

    @GetMapping("/addresses")
    public String showAddressBook(Model model, HttpServletRequest httpServletRequest) throws CustomerNotFoundException {


        Customer customer = isTheCustomerAuthenticate(httpServletRequest);
        List<CustomerAddresses> ALlAddresses = addressService.displayAllAddresses(customer);
        boolean letTheMainAddressBePrimary = true;
        for (CustomerAddresses address : ALlAddresses) {
            if (address.isPrimary()) {
                letTheMainAddressBePrimary = false;
                break;
            }
        }
        model.addAttribute("ALlAddresses", ALlAddresses);
        model.addAttribute("customer", customer);
        model.addAttribute("letTheMainAddressBePrimary", letTheMainAddressBePrimary);
        model.addAttribute("pageTitle", "All Addresses");


        return "addresses/list";
    }

    @GetMapping("/addresses/delete/{id}")
    public String deleteAddress(@PathVariable("id") Integer addressId, RedirectAttributes redirectAttributes,
                                HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        Customer customer = isTheCustomerAuthenticate(httpServletRequest);
        addressService.delete(addressId, customer.getId());
        redirectAttributes.addFlashAttribute("message", "Action: Address ID " + addressId + " deleted!");

        return "redirect:/addresses";
    }

    @GetMapping("/addresses/primary/{id}")
    public String setPrimary(@PathVariable("id") Integer addressId,
                             HttpServletRequest httpServletRequest,
                             RedirectAttributes redirectAttributes) throws CustomerNotFoundException {
        Customer customer = isTheCustomerAuthenticate(httpServletRequest);

        addressService.setPrimary(addressId, customer.getId());
        redirectAttributes.addFlashAttribute("message", "You have changed the Address " + addressId + " to primary");


        return "redirect:/addresses";
    }

    @GetMapping("/addresses/add")
    public String addAddress(Model model) {
        CustomerAddresses newAddress = new CustomerAddresses();
        List<Nation> nationList = customerService.nationList();

        model.addAttribute("nationList", nationList);
        model.addAttribute("address", newAddress);
        model.addAttribute("pageTitle", "Add Address");
        model.addAttribute("title", "Add new Address");


        return "addresses/form";
    }

    @PostMapping("/addresses/save")
    public String saveAddress(CustomerAddresses address, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) throws CustomerNotFoundException {
        Customer customer = isTheCustomerAuthenticate(httpServletRequest);

        address.setCustomer(customer);
        addressService.save(address);
        redirectAttributes.addFlashAttribute("message", "The address has been saved successfully.");
        /*String redirectOption = httpServletRequest.getParameter("redirect");
        String redirectURL = "redirect:/address_book";

        if ("checkout".equals(redirectOption)) {
            redirectURL += "?redirect=checkout";
        }
*/

        return "redirect:/addresses";
    }

}
