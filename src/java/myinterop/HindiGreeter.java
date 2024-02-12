package myinterop;

public class HindiGreeter implements Greeter {
    
    @Override
    public String greet(String name) {
        return "Namaste " + name;
    }
}
