package myinterop;

public class EnglishGreeting implements Greeting {
    
    @Override
    public String greet(String name) {
        return "Hello " + name;
    }
}
