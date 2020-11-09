class Main {
    public static void main(String[] args) {
        System.out.println(new PremuimUser().getMonthsLeft());
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
    int monthsLeft;

    public int getMonthsLeft(){
        int myAge;
        boolean myActive; 
        myAge = this.getAge();
        myActive = this.isActive();
        if(myActive){
            monthsLeft = myAge * 10;
        } else {
            monthsLeft = myAge - 5;
        }
        return monthsLeft;
    }

}

class Admin extends User {
    int salary;

    public int getSalary(int a, int b){
        salary = 1000;
        return salary - (a*b);
    }

}
