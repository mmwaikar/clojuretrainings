package myinterop;

public class EnglishGreeter implements Greeter {
    
    @Override
    public String greet(String name) {
        return "Hello " + name;
    }
}
