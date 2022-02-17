package edu.otib.lab_deserialization.controller;

import edu.otib.lab_deserialization.data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.util.Base64;
import java.util.Random;

@Controller
public class HelloController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value={"", "/", "hello"})
    public String hello(
            @RequestParam(value = "name", required = false, defaultValue = "test") String name,
            Model model
    ) {
        model.addAttribute("name", name);
        int id = new Random().nextInt();
        User webUser = new User(id, name);
        // Serialize Object
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(webUser);
            oos.close();
            String webUserOISB64 = Base64.getEncoder().encodeToString(baos.toByteArray());
            model.addAttribute("b64session", webUserOISB64);
        } catch (IOException ex) {
            log.error("IOException was thrown: " + ex.getMessage());
        }
        return "hello.html";
    }

    @GetMapping("/ois")
    public String ois(
            @RequestParam(value = "session") String session,
            Model model
    ) {
        try {
            byte[] byteSession = Base64.getDecoder().decode(session);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(byteSession));
            User webUser = (User) ois.readObject();
            ois.close();
            model.addAttribute("user", webUser);
        } catch (IOException ex) {
            log.error("IOException was thrown: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            log.error("ClassNotFoundException was thrown: " + ex.getMessage());
        }
        return "look_at_yourself.html";
    }
}
