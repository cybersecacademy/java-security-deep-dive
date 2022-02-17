package edu.otib.lab_known_vuln.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Objects.nonNull;

@Controller
public class CalcController {

    private final String filename = "uploadedFile";

    @GetMapping("/")
    public String main(HttpServletRequest request, HttpServletResponse response, Model model) {

        Cookie salary = WebUtils.getCookie(request, "salary");
        if (nonNull(salary)) {
            model.addAttribute("salary", salary.getValue());
        }
        else {
            model.addAttribute("salary", 50000);
        }

        Cookie fee = WebUtils.getCookie(request, "fee");
        if (nonNull(fee)) {
            model.addAttribute("fee", fee.getValue());
        }
        else {
            model.addAttribute("fee", 13);
        }

        return "main";
    }


    @PostMapping("/")
    public String calc(@RequestParam String salary, @RequestParam String fee, Model model, HttpServletResponse response) {
        Double result = Double.parseDouble(salary);
        try {
            result *= (100 - Long.parseLong(fee)) / 100.0;
        }
        catch (Exception e) {
            return e.toString();
        }

        model.addAttribute("salary", salary);
        model.addAttribute("fee", fee);
        model.addAttribute("result", String.format("%.2f", result));

        response.addCookie(new Cookie("salary", salary));
        response.addCookie(new Cookie("fee", fee));
        return "main";
    }

}
