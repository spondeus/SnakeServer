package snakeserver.dir.controller;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewPasswordFrom {

    @Pattern(regexp = "^(?=.*?[a-z])+(?=.*?[A-Z])+(?=.*?[0-9]){2,}.{6,30}$",
            message = "The password is at least 6 characters long, must contain 1 lowercase letter, 1 uppercase letter and 2 numbers!")
    private String password;
}
