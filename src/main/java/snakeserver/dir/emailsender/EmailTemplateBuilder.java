package snakeserver.dir.emailsender;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EmailTemplateBuilder {

    private String name = "Player";
    private final String mailHtml = """
            <span style="font-size:30px"><strong>Üdv %name%!</strong></span>
            <br>
            <p><strong>Kattints a követekező linkre a felhasználód aktiválásához:</strong></p>
            <a href="http://localhost:8081/login/activation/%name%">Activation<a>
            """;

    public void setName(String name) {
        this.name = name;
    }

    public String build() {
        return mailHtml.replace("%name%", this.name);
    }
}
