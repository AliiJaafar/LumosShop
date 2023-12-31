package com.dgpad.admin.control;

import com.dgpad.admin.util.FileUploadUtil;
import com.lumosshop.common.constant.Constants;
import com.lumosshop.common.entity.Currency;
import com.lumosshop.common.entity.control.Control;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class ControlController {

    @Autowired
    private ControlService controlService;
    @Autowired
    private CurrencyRepository currencyRepository;


    @GetMapping("/controlCenter")
    public String listAll(Model model) {

        List<Control> controlList = controlService.ListAllControls();
        List<Currency> currencies = currencyRepository.findAllByOrderByNameAsc();

        model.addAttribute("currencies", currencies);
        model.addAttribute("B2_Path", Constants.B2_ADDRESS);

        for (Control control : controlList) {
            model.addAttribute(control.getKey(), control.getValue());
        }


        return "control-center/controls";
    }

    @PostMapping("/control-center/standard-update")
    public String standardControlsUpdate(HttpServletRequest httpServletRequest,
                                         RedirectAttributes redirectAttributes) throws IOException {

        StandardControlCenter standardControlCenter = controlService.getStandardControl();
        updateCurrency(httpServletRequest, standardControlCenter);

        updateControls(httpServletRequest, standardControlCenter.list());

        redirectAttributes.addFlashAttribute("message", "We've stored the standard controls as desired.");


        return "redirect:/controlCenter";
    }


    private void updateCurrency(HttpServletRequest httpServletRequest, StandardControlCenter standardControlCenter) {
        Integer currencyId = Integer.parseInt(httpServletRequest.getParameter("CURRENCY_ID"));
        Optional<Currency> currencyValue = currencyRepository.findById(currencyId);
        System.out.println("currency Value ---> " +currencyValue);


        if (currencyValue.isPresent()) {
            Currency currency = currencyValue.get();
            standardControlCenter.updateCurrencySymbol(currency.getSymbol());
            standardControlCenter.updateCurrencyFormat(currency.getCode());
        }
    }

    private void updateControls(HttpServletRequest httpServletRequest,
                               List<Control> controlList) {
        for (Control control : controlList) {
            String meta = httpServletRequest.getParameter(control.getKey());
            if (meta != null) {
                control.setValue(meta);
            }
        }
        controlService.saveAll(controlList);

    }

    @PostMapping("/control/update-mail-system")
    public String updateEmailSystems(HttpServletRequest httpServletRequest,
                                     RedirectAttributes redirectAttributes) {
        List<Control> emailSystems = controlService.getAllEmailSystems();
        updateControls(httpServletRequest, emailSystems);

        redirectAttributes.addFlashAttribute("message", "We've made some adjustments to the Email System Controls.");

        return "redirect:/controlCenter";
    }
    @PostMapping("/control/update-message-outline")
    public String updateMessageOutline(HttpServletRequest httpServletRequest,
                                     RedirectAttributes redirectAttributes) {
        List<Control> outline = controlService.getAllMessageOutlines();
        updateControls(httpServletRequest, outline);

        redirectAttributes.addFlashAttribute("message", "The message's outline has been updated");

        return "redirect:/controlCenter";
    }
    @PostMapping("/control/update-message-design")
    public String updateMessageDesign(HttpServletRequest httpServletRequest,
                                     RedirectAttributes redirectAttributes) {
        List<Control> design = controlService.getAllMessageOutlines();
        updateControls(httpServletRequest, design);

        redirectAttributes.addFlashAttribute("message", "The Design and structure has been updated");

        return "redirect:/controlCenter";
    }
    @PostMapping("/control/payment-update")
    public String update_payment(HttpServletRequest httpServletRequest,
                                     RedirectAttributes redirectAttributes) {
        List<Control> paymentControl = controlService.getAllPaymentControl();
        updateControls(httpServletRequest, paymentControl);

        redirectAttributes.addFlashAttribute("message", "The Payment data has been updated");

        return "redirect:/controlCenter";
    }
}

