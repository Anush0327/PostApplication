package com.prodapt.learningspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prodapt.learningspring.controller.binding.RegistrationForm;
import com.prodapt.learningspring.service.DomainUserService;


@Controller
@RequestMapping("/register")
public class RegistrationController {
    
    @Autowired
    private DomainUserService domainUserService;

    @GetMapping
    public String getRegistrationForm(Model model) {
        System.out.println("hello world");
        if (!model.containsAttribute("registrationForm")) {
            model.addAttribute("registrationForm", new RegistrationForm());
          }
        return "register";
    }

    @PostMapping
    public String register(@ModelAttribute("registrationForm") RegistrationForm registrationForm, 
    BindingResult bindingResult, 
    RedirectAttributes attr) {
        if (bindingResult.hasErrors()) {
        attr.addFlashAttribute("org.springframework.validation.BindingResult.registrationForm", bindingResult);
        attr.addFlashAttribute("registrationForm", registrationForm);
        return "redirect:/register";
        }
        if (!registrationForm.isValid()) {
        attr.addFlashAttribute("message", "Passwords must match");
        attr.addFlashAttribute("registrationForm", registrationForm);
        return "redirect:/register";
        }
        System.out.println(domainUserService.save(registrationForm.getName(), registrationForm.getPassword()));
        attr.addFlashAttribute("result", "Registration success!");
        return "redirect:/login";
    }


}
