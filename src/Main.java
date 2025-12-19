import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//C:\\Users\AlAZHdanov\Desktop\access.log
public class Main {
    public static void main(String[] args) throws IOException {
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
                int googleBots = 0;
                int yandexBots = 0;
                Statistics stats = new Statistics();
                boolean firstLinePrinted = false;

                while ((line = bufferedReader.readLine()) != null){
                    countStrings++;
                    int length = line.length();
                    if(length > 1024){
                        throw new StringLengthException("Строка больше 1024 символов");
                    }
                    LogEntry entry;
                    try{
                        entry = new LogEntry(line);
                    }catch(Exception e){
                        continue;
                    }

                    stats.addEntry(entry);
                    if(!firstLinePrinted){
                        System.out.println("Пример распаршенной строки:");
                        System.out.println(entry);
                        firstLinePrinted = true;
                    }

                    int lastQuote = line.lastIndexOf('"');
                    int lastQuote_1 = line.lastIndexOf('"', lastQuote - 1);
                    if(lastQuote == -1 || lastQuote_1 == -1){
                        continue;
                    }

                    String userAgent = line.substring(lastQuote_1 + 1, lastQuote);
                    UserAgent ua = entry.getUserAgent();

                    int index = 0;
                    while(true) {
                        int start = userAgent.indexOf('(', index);
                        if (start == -1)break;
                        int end = userAgent.indexOf(')',start);
                        if (end == -1)break;

                            String firstBrackets = userAgent.substring(start + 1, end);
                            String[] parts = firstBrackets.split(";");
                            for (int i = 0; i < parts.length; i++) {
                                parts[i] = parts[i].trim();
                            }
                            for (String fragment : parts) {
                                String[] fragments = fragment.split("/");
                                String bot = fragments[0].trim();
                                if (bot.equalsIgnoreCase("Googlebot")) {
                                    googleBots++;
                                    //googleBotLines.add(countStrings);
                                } else if (bot.equals("YandexBot")) {
                                    yandexBots++;
                                }
                            }
                            index = end+1;
                    }
                }
                System.out.println("Общее количество строк: " + countStrings);
                //System.out.println(googleBots);
                System.out.println("Доля запросов Гугл-ботов: " + (double)googleBots/countStrings);
                //System.out.println(" строки гугла : " + googleBotLines  );
                //System.out.println(yandexBots);
                System.out.println("Доля запросов Яндекс-ботов: " + (double)yandexBots/countStrings);
                System.out.println("Средний траффик за час: " + stats.getTrafficRate());
                //System.out.println("Существующие сайты: "+ stats.getWebSites());
                //System.out.println("Несуществующие сайты: "+ stats.getUnexistingWebSites());
                System.out.println("Доли ОС : " + stats.getOsFraction());
                System.out.println("Доли браузеров : " + stats.getBrowserFraction());
                System.out.println("Среднее количество посещений за час (не боты): " + stats.getAverageVisitation());
                System.out.println("Среднее количество ошибочных запросов за час: " + stats.getAverageMistakesPerHour());
                System.out.println("Среднее количество посещений одним пользователем: " + stats.getAverageVisitsByIP());
                System.out.println("Трафик за час: " + stats.getTrafficRate());
                System.out.println("Максимальное посещение по секундам: " + stats.getMaxVisitsPerSeconds());
                System.out.println("Максимальное посещение одним пользователем : " + stats.getMaxVisitByUser());

                break;
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

