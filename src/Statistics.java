import java.time.Duration;
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
    HashSet<String> unexistingWebSites = new HashSet<>();
    HashMap<String,Integer> osFrequencies = new HashMap<>();
    HashMap<String, Integer> browserFrequencies = new HashMap<>();
    int notBotVisits;
    int mistakeRequests;
    HashSet<String> nonBotIPs = new HashSet<>();
    HashMap<Integer,Integer> visitsPerSecond = new HashMap<>();
    HashSet<String> domains = new HashSet<>();
    HashMap<String, Integer> visitsPerUser = new HashMap<>();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    public Statistics(){
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
        this.notBotVisits = 0;
    }

    private long getHours(){
        if (minTime == null ||  maxTime == null){
            return 0;
        }
        return java.time.Duration.between(minTime,maxTime).toHours();
    }

    public HashSet<String> getDomains() {
        return domains;
    }

    //максимальное число запросов в секунду
    public int getMaxVisitsPerSeconds(){
        int max = 0;
        for (int value : visitsPerSecond.values()){
            if(value > max){
                max = value;
            }
        }
        return max;
    }

    public int getMaxVisitByUser(){
        if(visitsPerUser.isEmpty()){
            return 0;
        }
        return visitsPerUser.values().stream().max(Integer::compareTo).get();
    }

    public double getAverageMistakesPerHour(){

        long hours = getHours();
        if(hours == 0){
            return mistakeRequests;
        }
        return (double) mistakeRequests/hours;
    }

    public double getAverageVisitation(){
        long hours = getHours();
        if(hours == 0){
            return notBotVisits;
        }
        return (double)notBotVisits/hours;
    }

    public double getAverageVisitsByIP(){
        if(nonBotIPs.isEmpty()){
            return 0;
        }
        return (double)notBotVisits/nonBotIPs.size();
    }

    public void addEntry(LogEntry entry){
        totalTraffic += entry.getResponseSize();

        if (entry.getResponse() == 200){
            webSites.add(entry.getUrl());
        }
        if (entry.getResponse() == 404){
            unexistingWebSites.add(entry.getUrl());
        }

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(entry.getDateTime(), formatter);
        LocalDateTime time = offsetDateTime.toLocalDateTime();
        if(minTime == null || time.isBefore(minTime)){
            minTime = time;
        }
        if(maxTime == null || time.isAfter(maxTime)){
            maxTime = time;
        }

        UserAgent userAgent = entry.getUserAgent();
        if (!userAgent.isBot()){
            notBotVisits++;
            nonBotIPs.add(entry.getIp());
            //dlya добавления записей в visitsPerSecond
            int second = (int) Duration.between(minTime, time).getSeconds();
            visitsPerSecond.merge(second, 1, Integer::sum);
            //
            visitsPerUser.merge(entry.getIp(), 1, Integer::sum);
        }

        String OS = entry.getUserAgent().getOS();
        if(!osFrequencies.containsKey(OS)){
            osFrequencies.put(OS, 1);
        }else {
            osFrequencies.put(OS,osFrequencies.get(OS) + 1);
        }

        //домены
        String referer = entry.getReferer();
        if(referer != null && !referer.isEmpty() && !"-".equals(referer)){
            int schemeEnd = referer.indexOf("://");
            if(schemeEnd != -1) {
                int domainStart = schemeEnd + 3;
                int slash = referer.indexOf('/', domainStart);
                String domain;
                if(slash != -1){
                    domain = referer.substring(domainStart, slash);
                }else{
                    domain = referer.substring(domainStart);
                }
                domains.add(domain);
            }
        }

        String browser = entry.getUserAgent().getBrowser();
        if(!browserFrequencies.containsKey(browser)){
            browserFrequencies.put(browser, 1);
        }else {
            browserFrequencies.put(browser, browserFrequencies.get(browser) + 1);
        }
        if(entry.getResponse() >= 400 &&  entry.getResponse() < 600){
            mistakeRequests++;
        }
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

    public int getTotalBrowserCount(){
        int totalBrowserCount = 0;
        for (int count : browserFrequencies.values()){
            totalBrowserCount += count;
        }
        return totalBrowserCount;
    }

    public HashMap<String, Double> getBrowserFraction(){
        HashMap<String, Double> browserFraction = new HashMap<>();
        int totalBrowserCount = getTotalBrowserCount();
        if (totalBrowserCount == 0){
            return browserFraction;
        }
        for (var entry : browserFrequencies.entrySet()){
            String browser = entry.getKey();
            int count = entry.getValue();
            double fraction = (double)count / totalBrowserCount;
            browserFraction.put(browser, fraction);
        }
        return browserFraction;
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

    public HashSet<String> getWebSites() {
        return webSites;
    }
    public HashSet<String> getUnexistingWebSites() {
        return unexistingWebSites;
    }
    public LocalDateTime getMaxTime() {
        return maxTime;
    }
}
