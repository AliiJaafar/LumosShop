package com.dgpad.admin.Shipping;

import com.dgpad.admin.customer.CustomerService;
import com.dgpad.admin.user.UserNotFoundException;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.Shipping;
import com.lumosshop.common.entity.control.Nation;
import com.lumosshop.common.exception.CustomerNotFoundException;
import com.lumosshop.common.exception.ShippingFeeNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
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

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class ShippingController {

    @Autowired
    private ShippingService shippingService;


    @GetMapping("/shipping")
    public String displayShippingFees(Model model, RedirectAttributes redirectAttributes) {
        return listByPage(model, 1, "id", "asc", null);
    }
    @GetMapping("/shipping/page/{pageNumber}")
    public String listByPage(Model model,
                             @PathVariable(name = "pageNumber") int pageNumber,
                             @Param("sortField") String sortField,
                             @Param("sortDirection") String sortDirection,
                             @Param("keyword") String keyword) {

        Page<Shipping> page = shippingService.listFeesByPage(pageNumber, sortField, sortDirection, keyword);
        List<Shipping> shippingList = page.getContent();

        long startCount = (long) (pageNumber - 1) * ShippingService.FEE_PER_PAGE + 1;
        long endCount = startCount + ShippingService.FEE_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElement", page.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("shippingList", shippingList);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", reverseSortDirection);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moduleURL", "/shipping");

        model.addAttribute("PageTitle", "Shipping-Fees");
        model.addAttribute("Title", "Manage Shipping fees");

        return "shipping/feesList";
    }

    @GetMapping("/shipping/form")
    public String FeeForm(Model model) {
        List<Nation> nationList = shippingService.nationList();
        Shipping fee = new Shipping();
        model.addAttribute("PageTitle", "Fee - Form");
        model.addAttribute("Title", "Shipping Fee Form");

        model.addAttribute("nationList", nationList);
        model.addAttribute("Fee", fee);

        return "shipping/shipping-fee-form";
    }

    @PostMapping("/shipping/saveFee")
    public String enrollFee(@ModelAttribute("Fee") Shipping shipping,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        shippingService.save(shipping);
        redirectAttributes.addFlashAttribute("message", "The fee has been successfully added to the system.");




        return "redirect:/shipping";

    }

    @GetMapping("/shipping/edit/{id}")
    public String showFormForUpdate(@PathVariable(name = "id") int theId,
                                    Model theModel, RedirectAttributes redirectAttributes) {
        try {
            Shipping shippingFee = shippingService.findShippingFeeById(theId);
            List<Nation> nationList = shippingService.nationList();
            theModel.addAttribute("nationList", nationList);
            theModel.addAttribute("Fee", shippingFee);
            theModel.addAttribute("pageTitle", "Edit Shipping Fee ID : ( " + theId + " )");

            return "shipping/shipping-fee-form";

        } catch (ShippingFeeNotFoundException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());

            return "redirect:/shipping";
        }
    }

    @GetMapping("/shipping/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) throws UserNotFoundException {
        try {
            shippingService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Shipping fee ID " + id + " has been deleted from the system with success.");
        } catch (ShippingFeeNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        // redirect to
        return "redirect:/shipping";
    }

    @GetMapping("/shipping/{id}/able/{status}")
    public String UpdateEnableStatus(@PathVariable(name = "id") Integer id, @PathVariable(name = "status") boolean able, RedirectAttributes redirectAttributes) {

        try {
            shippingService.UpdateTheShippingCashOnDeliveryAbility(id, able);
            String status = able ? "able" : "unable";
            String message = "We've updated the status for Shipping Fee ID " + id + " to " + status + ".";
            redirectAttributes.addFlashAttribute("message", message);
        } catch (ShippingFeeNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/shipping";
    }
}
