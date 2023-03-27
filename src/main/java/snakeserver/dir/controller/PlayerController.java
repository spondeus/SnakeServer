package snakeserver.dir.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import snakeserver.dir.authetication.PlayerService;
import snakeserver.dir.model.Player;
import snakeserver.dir.model.PlayerRepository;

@Controller
public class PlayerController {

    private PlayerService playerService;
    private final PlayerRepository playerRepository;

    public PlayerController(
            PlayerService pService,
            PlayerRepository pRepository) {
        this.playerService = pService;
        this.playerRepository = pRepository;
    }

    @GetMapping(path={"/", "", "/home"})
    public String homePage() {
        return "home";
    }

    @GetMapping(path={"/registration"})
    public String registrationPage(
            Model m
    ) {
        m.addAttribute("newuser", new RegistrationForm());
        return "registration-form";
    }

    @PostMapping(path={"/registration"})
    public String saveRegistration(
            @ModelAttribute("newuser")
            @Validated
            RegistrationForm registrationForm,
            BindingResult bind
    ) {
        if (bind.hasErrors()) {
            return "registration-form";
        }


        Player player = new Player();
        player.setName(registrationForm.getName());
        player.setEmail(registrationForm.getEmail());
        player.setPassword(BCrypt.hashpw(registrationForm.getPassword(), BCrypt.gensalt()));

        playerRepository.save(player);

        return "redirect:/home";
    }

    @GetMapping(path={"/login"})
    public String loginPage() {
        return "login";
    }
    @GetMapping(path={"/logout"})
    public String logoutPage() {
        return "redirect:/home";
    }
}
