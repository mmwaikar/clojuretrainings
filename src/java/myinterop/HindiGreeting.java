package myinterop;

public class HindiGreeting implements Greeting {
    
    @Override
    public String greet(String name) {
        return "Namaste " + name;
    }
}
