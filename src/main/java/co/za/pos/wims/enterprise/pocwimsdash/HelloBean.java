package co.za.pos.wims.enterprise.pocwimsdash;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;

@Named
@RequestScoped
public class HelloBean {

    private String name;

    @PostConstruct
    public void init() {
        // Initialization logic
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String greet() { return "Hello, " + getName() + "!"; }
}
