class Main {
    public static void main(String[] args) {
        System.out.println(new PremuimUser().getAge());
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
    Admin adminField;

    @Override
    public int getAge(){
        age = 20;
        return age;
    }

    @Override
    public boolean isActive(){
        return true;
    }

    public int getSalary(){
        int x;
        x = adminField.getSalary();
        x = this.getSalary();
        return 1000;
    }

}

class Admin extends User {
    int salary;
    PremuimUser puField;

    public int getSalary(){
        salary = 1000;
        return salary;
    }

    @Override
    public int getAge(){
        return 50;
    }

    public int test(PremuimUser formalPremuimUser, Admin formalAdmin){
        PremuimUser puLocal;
        Admin adminLocal;
        int x;
        puLocal = new PremuimUser(); 
        adminLocal = new Admin();
        x = puLocal.getSalary();
        x = adminLocal.getSalary();
        x = this.getSalary();
        x = formalPremuimUser.getSalary();
        x = formalAdmin.getSalary();
        x = puField.getSalary();
        return 50;
    }

}
