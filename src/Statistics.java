import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

public class Statistics {
    int totalTraffic;
    LocalDateTime minTime;
    LocalDateTime maxTime;
    HashSet<String> webSites = new HashSet<>();
    HashMap<String,Integer> osFrequencies = new HashMap<>();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    public Statistics(){
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;

    }

    public int getTotalOSCount(){
        int totalOSCount = 0;
        for (int count : osFrequencies.values()){
            totalOSCount += count;
        }
        return totalOSCount;
    }

    public HashMap<String, Double> getOsFraction(){
        HashMap<String, Double> osFraction = new HashMap<>();
        int totalOSCount = getTotalOSCount();
        if (totalOSCount == 0){
            return osFraction;
        }
        for (var entry : osFrequencies.entrySet()){
            String OS = entry.getKey();
            int count = entry.getValue();
            double fraction = (double)count / totalOSCount;
            osFraction.put(OS, fraction);
        }
        return osFraction;
    }

    public LocalDateTime getMaxTime() {
        return maxTime;
    }

    public void addEntry(LogEntry entry){
        totalTraffic += entry.getResponseSize();

        if (entry.getResponse() == 200){
            webSites.add(entry.getUrl());
        }

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(entry.getDateTime(), formatter);
        LocalDateTime time = offsetDateTime.toLocalDateTime();
        if(minTime == null || time.isBefore(minTime)){
            minTime = time;
        }
        if(maxTime == null || time.isAfter(maxTime)){
            maxTime = time;
        }

        String OS = entry.getUserAgent().getOS();
        if(!osFrequencies.containsKey(OS)){
            osFrequencies.put(OS, 1);
        }else {
            osFrequencies.put(OS,osFrequencies.get(OS) + 1);
        }

    }

    public double  getTrafficRate(){
        if (minTime == null || maxTime == null){
            return 0;
        }
        long hours = java.time.Duration.between(minTime, maxTime).toHours();
        if(hours == 0){
            return totalTraffic;
        }
        return ((double) totalTraffic)/hours;
    }
}
