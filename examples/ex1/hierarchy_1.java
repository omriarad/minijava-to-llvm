class Main {
    public static void main(String[] args) {
        System.out.println(new PremuimUser().getName());
    }
} 

class User {
    boolean active;
    int age;

    public int getAge(){
        age = 10;
        return age;
    }

    public boolean isActive(){
        return false;
    }

}

class PremuimUser extends User {
    @Override
    public int getAge(){
        age = 20;
        return age;
    }

    @Override
    public boolean isActive(){
        return true;
    }

}

class Admin extends User {
    int salary;

    public int getSalary(){
        salary = 1000;
        return salary;
    }

}