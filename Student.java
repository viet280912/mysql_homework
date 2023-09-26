public class Student {
    int ID, age;
    String name, className;

    public Student() {
    }

    public Student(int ID, int age, String name, String aClass) {
        this.ID = ID;
        this.age = age;
        this.name = name;
        className = aClass;
    }

    public Student(int age, String name, String aClass) {
        this.age = age;
        this.name = name;
        className = aClass;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClass(String aClass) {
        this.className = aClass;
    }
}
