package snakeserver.dir.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationForm {

    @Size(min = 6, max = 20, message = "The Player Name must be between 6 and 20 characters!")
    private String name;

    @Pattern(regexp = "^(?=.*?[a-z])+(?=.*?[A-Z])+(?=.*?[0-9]){2,}.{6,30}$",
            message = "The password is at least 6 characters long, must contain 1 lowercase letter, 1 uppercase letter and 2 numbers!")
    private String password;

    @Email
    @NotEmpty(message = "An e-mail address is required!")
    private String email;
}
