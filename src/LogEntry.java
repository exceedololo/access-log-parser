public class LogEntry {
    final String ip;
    final String dateTime;
    final HttpMethod method;
    final String url;
    final int response;
    final int responseSize;
    final String referer;
    final UserAgent userAgent;

    public LogEntry(String logLine) {
        String[] parts = logLine.split(" ");
        this.ip = parts[0];

        int dateStart = logLine.indexOf("[");
        int dateEnd = logLine.indexOf("]");
        this.dateTime = logLine.substring(dateStart + 1, dateEnd);

        int firstQuote = logLine.indexOf("\"");
        int lastQuote = logLine.indexOf("\"", firstQuote + 1);
        String request = logLine.substring(firstQuote + 1, lastQuote);
        String[] requestParts = request.split(" ");
        this.method = HttpMethod.valueOf(requestParts[0]);
        this.url = requestParts[1];

        String afterRequest = logLine.substring(lastQuote + 1).trim();
        String[] afterRequestParts = afterRequest.split(" ");
        this.response = Integer.parseInt(afterRequestParts[0]);
        this.responseSize = Integer.parseInt(afterRequestParts[1]);

        int refererStart = afterRequest.indexOf("\"");
        int refererEnd = afterRequest.indexOf("\"", refererStart + 1);
        this.referer = afterRequest.substring(refererStart + 1, refererEnd);

        int uaStart = afterRequest.indexOf("\"", refererEnd + 1);
        int uaEnd = afterRequest.indexOf("\"", uaStart + 1);
        String uaString = afterRequest.substring(uaStart + 1, uaEnd);
        this.userAgent = new UserAgent(uaString);
    }

    public String getIp() {
        return ip;
    }
    public String getDateTime() {
        return dateTime;
    }
    public HttpMethod getMethod() {
        return method;
    }
    public String getUrl() {
        return url;
    }
    public int getResponse() {
        return response;
    }
    public int getResponseSize() {
        return responseSize;
    }
    public String getReferer() {
        return referer;
    }
    public UserAgent getUserAgent() {
        return userAgent;
    }

    @Override
    public String toString() {
        return "LogEntry{" + "ip=" + ip + " ||, dateTime=" + dateTime + " ||, method=" + method + " ||, url=" + url + " ||, response=" +
                response + " ||,responseSize=" + responseSize + " ||, referer=" + referer + " !!!, userAgent=" + userAgent + '}';
    }
}



