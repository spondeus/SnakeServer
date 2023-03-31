package snakeserver.dir.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import snakeserver.dir.authetication.PlayerService;
import snakeserver.dir.emailsender.EmailSender;
import snakeserver.dir.emailsender.EmailTemplateBuilder;
import snakeserver.dir.model.Player;
import snakeserver.dir.model.PlayerRepository;


@Controller
public class PlayerController {

    private PlayerService playerService;
    private final PlayerRepository playerRepository;

    @Autowired
    private EmailSender sender;

    public PlayerController(
            PlayerService pService,
            PlayerRepository pRepository) {
        this.playerService = pService;
        this.playerRepository = pRepository;
    }

    @GetMapping(path = {"/", "", "/home"})
    public String homePage() {
        return "home";
    }

    @GetMapping(path = {"/registration"})
    public String registrationPage(
            Model m
    ) {
        m.addAttribute("newuser", new RegistrationForm());
        return "registration-form";
    }

    @PostMapping(path = {"/registration"})
    public String saveRegistration(
            @ModelAttribute("newuser")
            @Validated
            RegistrationForm registrationForm,
            BindingResult bind,
            EmailTemplateBuilder template,
            Model model
    ) {
        if (bind.hasErrors()) {
            return "registration-form";
        }


        Player player = new Player();
        player.setName(registrationForm.getName());
        player.setEmail(registrationForm.getEmail());
        player.setPassword(BCrypt.hashpw(registrationForm.getPassword(), BCrypt.gensalt()));


        try {
            playerRepository.save(player);
            template.setName(player.getName());
            sender.send(player.getEmail(), template.build());
            return "redirect:/login/inactive";
        } catch (DataAccessException e) {
            if (e.getMessage().contains("snake_player.name")) model.addAttribute("nameError", e.getMessage());
            if (e.getMessage().contains("snake_player.email")) model.addAttribute("emailError", e.getMessage());
            return "registration-form";
        }
    }


    @GetMapping(path = {"/login"})
    public String loginPage() {
        return "login";
    }

//    @PostMapping(path = {"/login"})
//    public String loginPagePost() {
//        return "game";
//    }

    @GetMapping(path = {"/game"})
    public String gamePage() {
        return "game";
    }


    @GetMapping(path = "/login/activation/{name}")
    public String activationRegistration(
            @PathVariable(value = "name") String name
    ) {
        playerRepository.updateEnableByName(name);
        return "redirect:/login/active";
    }

    @GetMapping(path = {"/login/{status}"})
    public String loginPageActive(
            @PathVariable(value = "status") String status,
            Model model
    ) {
        model.addAttribute("status", status);
        return "login";
    }

    @PostMapping(path = {"/logout"})
    public String logoutPage() {
        return "redirect:/home/logout";
    }

    @GetMapping(path = {"/home/{logout}"})
    public String homeLogoutPage(
            @PathVariable(value = "logout") String logoutText,
            Model model
    ) {
        model.addAttribute("logoutText", logoutText);
        return "redirect:/home";
    }


}
