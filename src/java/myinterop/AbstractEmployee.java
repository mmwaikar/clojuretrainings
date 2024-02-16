package myinterop;

public abstract class AbstractEmployee {

    private String _name;
    private int _age;
    
    public AbstractEmployee(String name) {
        this(name, 10);
    }
    
    public AbstractEmployee(String name, int age) {
        _name = name;
        _age = age;
    }

    public String getName() {
        return _name;
    }

    public int getAge() {
        return _age;
    }

    public abstract String greet();

    @Override
    public String toString() {
        return String.format("AbstractEmployee(name=%s, age=%d)", _name, _age);
    }
}
