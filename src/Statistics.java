import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Statistics {
    int totalTraffic;
    LocalDateTime minTime;
    LocalDateTime maxTime;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    public Statistics(){
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;

    }

    public LocalDateTime getMaxTime() {
        return maxTime;
    }

    public void addEntry(LogEntry entry){
        totalTraffic += entry.getResponseSize();
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(entry.getDateTime(), formatter);
        LocalDateTime time = offsetDateTime.toLocalDateTime();
        if(minTime == null || time.isBefore(minTime)){
            minTime = time;
        }
        if(maxTime == null || time.isAfter(maxTime)){
            maxTime = time;
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
