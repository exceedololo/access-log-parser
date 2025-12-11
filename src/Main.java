import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        //System.out.println("Введите текст и нажмите <Enter>:");
        //String text = new Scanner(System.in).nextLine();
        //System.out.println("Длина текста: " + text.length());



        //задание по теме "Циклы"
        int count = 0;
        while (true) {
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if(!fileExists){
                System.out.println("Файла не существует.");
                count++;
                continue;
            }
            if(isDirectory){
                System.out.println("Это папка.");
                count++;
                continue;
            }
            count++;
            System.out.println("Путь указан верно. Это файл номер " + count);

            try{
                FileReader fileReader = new FileReader(path);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                int countStrings = 0;
                int longestString = 0;
                int shortestString = Integer.MAX_VALUE;
                while ((line = bufferedReader.readLine()) != null){
                    int length = line.length();
                    if(length > 1024){
                        throw new StringLengthException("Строка больше 1024 символов");
                    }
                    countStrings++;
                    if (length > longestString){
                        longestString = length;
                    }
                    if (length < shortestString){
                        shortestString = length;
                    }
                }
                System.out.println("Общее количество строк: " + countStrings);
                System.out.println("Длина самой длинной строки: " + longestString);
                System.out.println("Длина самой короткой строки: " + shortestString);
                break;
            }catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }
}

class StringLengthException extends RuntimeException{
    public StringLengthException() {
        super();
    }
    public StringLengthException(String message){
        super(message);
    }
    public StringLengthException(String message,Throwable cause){
        super(message,cause);
    }
    public StringLengthException(Throwable cause){
        super(cause);
    }
    public  StringLengthException(String message,Throwable cause,boolean enableSuppression,boolean writableStackTrace){
        super(message,cause,enableSuppression,writableStackTrace);
    }
}