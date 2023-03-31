package snakeserver.dir.emailsender;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EmailTemplateBuilder {

    private String name = "Player";
    private final String activationMail = """
            <span style="font-size:30px"><strong>Hello %name%!</strong></span>
            <br>
            <p><strong>Click on the following link to activate your user:</strong></p>
            <a href="http://localhost:8081/login/activation/%name%">Activation<a>
            """;

    public void setName(String name) {
        this.name = name;
    }

    public String build() {
        return activationMail.replace("%name%", this.name);
    }
}
