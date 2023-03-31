package snakeserver.dir.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import snakeserver.dir.authetication.PlayerService;
import snakeserver.dir.emailsender.EmailSender;
import snakeserver.dir.model.Player;
import snakeserver.dir.model.PlayerRepository;



@Controller
public class PlayerController {

    private final PlayerService playerService;
    private final PasswordEncoder passwordEncoder;
    private final PlayerRepository playerRepository;


    public PlayerController(
            PlayerService pService,
            PlayerRepository pRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.playerService = pService;
        this.playerRepository = pRepository;
        this.passwordEncoder = passwordEncoder;
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
            Model model
    ) {
        if (bind.hasErrors()) {
            return "registration-form";
        }


        Player player = new Player();
        player.setName(registrationForm.getName());
        player.setEmail(registrationForm.getEmail());
        player.setPassword(passwordEncoder.encode(registrationForm.getPassword()));

        try {
            playerRepository.save(player);
            return "redirect:/login";
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

    @GetMapping(path = {"/game"})
    public String gamePage() {
        return "game";
    }



    @GetMapping(path={"/logout"})
    public String logoutPage() {
        return "redirect:/home";
    }



}
