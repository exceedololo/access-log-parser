import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите первое число :");
        int number1 = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число :");
        int number2 = new Scanner(System.in).nextInt();
        var sum =   number1 + number2;
        System.out.println("Сумма равна : " + sum);
        var diff = number1 - number2;
        System.out.println("Разность равна :  " +  diff);
        var product =  number1 * number2;
        System.out.println("Произведение равно : " + product);
        var quotient = (double) number1 / number2;
        System.out.println("Частное равно : " + quotient);
    }
}
