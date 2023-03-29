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

    @Size(min = 6, max = 20, message = "A Player Name méretnek 6 és 20 karakter között kell lennie!")
    private String name;

    @Pattern(regexp = "^(?=.*?[a-z])+(?=.*?[A-Z])+(?=.*?[0-9]){2,}.{6,30}$",
            message = "A jelszó minimum 6 karakter, tartalmaznia kell 1 kisbetűt, 1 nagybetűt és 2 számot!")
    private String password;

    @Email
    @NotEmpty(message = "E-mail címet kötelezző megadni!")
    private String email;
}
