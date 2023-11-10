package com.dgpad.admin.customer;

import com.dgpad.admin.user.UserNotFoundException;
import com.dgpad.admin.user.UserService;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.Role;
import com.lumosshop.common.entity.User;
import com.lumosshop.common.entity.control.Nation;
import com.lumosshop.common.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public String listCustomersForFirstPage(Model model) {
        return listByPage(model, 1, "id", "asc", null);
    }

    @GetMapping("/customers/page/{pageNumber}")
    public String listByPage(Model model,
                             @PathVariable(name = "pageNumber") int pageNumber,
                             @Param("sortField") String sortField,
                             @Param("sortDirection") String sortDirection,
                             @Param("keyword") String keyword) {

        Page<Customer> page = customerService.listCustomerByPage(pageNumber, sortField, sortDirection, keyword);
        List<Customer> customerList = page.getContent();

        long startCount = (long) (pageNumber - 1) * CustomerService.CUSTOMER_PER_PAGE + 1;
        long endCount = startCount + CustomerService.CUSTOMER_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElement", page.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("customerList", customerList);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", reverseSortDirection);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moduleURL", "/customers");
        model.addAttribute("pageTitle", "Customers");

        return "customers/customers";
    }

    @GetMapping(value = "/customers/add")
    public String showAddCustomerForm(Model model) {

        Customer customer = new Customer();
        model.addAttribute("nationList", customerService.nationList());

        model.addAttribute("customer", customer);

        model.addAttribute("pageTitle", "Add New Customer");

        return "customers/customer-form";
    }

    @PostMapping("/customers/saving")
    public String saveCustomer(@ModelAttribute("customer") Customer customer,
                               RedirectAttributes redirectAttributes) throws IOException {
        customerService.save(customer);
        redirectAttributes.addFlashAttribute("message", "The customer has been successfully added to the system.");

        String fitchAndFocus = customer.getEmail().split("@")[0];
        return "redirect:/customers/page/1?sortField=id&sortDirection=asc&keyword=" + fitchAndFocus;
    }


    @GetMapping("/customers/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(name = "id") int theId,
                                    Model theModel, RedirectAttributes redirectAttributes) {
        try {
            Customer customer = customerService.findCustomerById(theId);
            List<Nation> nationList = customerService.nationList();
            theModel.addAttribute("nationList", nationList);
            theModel.addAttribute("customer", customer);
            theModel.addAttribute("pageTitle", "Edit Customer ID : ( " + theId + " )");

            return "customers/customer-form";
        } catch (CustomerNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());

            return "redirect:/customers";
        }
    }

    @GetMapping("/customers/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) throws UserNotFoundException {
        try {
            customerService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Customer ID " + id + " has been deleted from the system with success.");
        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        // redirect to
        return "redirect:/customers";
    }

    @GetMapping("/customers/{id}/enabled/{status}")
    public String UpdateEnableStatus(@PathVariable(name = "id") Integer id, @PathVariable(name = "status") boolean enabled, RedirectAttributes redirectAttributes) {

        customerService.UpdateTheCustomerStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "We've updated the status for customer ID " + id + " to " + status + ".";
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/customers";
    }

    @GetMapping("/customers/info/{id}")
    public String showDetails(@PathVariable("id") Integer id,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            Customer customer = customerService.findCustomerById(id);
            model.addAttribute("customer", customer);
            model.addAttribute("Title", "Customer Details ID : " + id);

            return "customers/customer-details";
        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/customers";

        }
    }
}
