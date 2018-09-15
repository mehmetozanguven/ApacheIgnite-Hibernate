package igniteWithHibernate;

public class Person {

    private int personID;
    private String name;
    private int age;
    private double salary;

    public Person(){}

    public Person(int personID, String name, int age, double salary){
        this.name = name;
        this.age = age;
        this.personID = personID;
        this.salary = salary;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String toString(){
        return "Person id= " + personID + " name= " + name + " age= " + age + " salary= " + salary;
    }
}
