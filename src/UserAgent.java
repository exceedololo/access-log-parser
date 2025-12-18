public class UserAgent {
    final String OS;
    final String browser;
    final Boolean isBot;

    public UserAgent(String UAstring) {
        this.isBot = UAstring.toLowerCase().contains("bot");
        int slash = UAstring.indexOf('/');
        this.browser = UAstring.substring(0, slash);
        int brace = UAstring.indexOf('(');
        int semicolon =  UAstring.indexOf(';');
        this.OS = UAstring.substring(brace + 1, semicolon);
    }


    public String getBrowser() {
        return browser;
    }
    public String getOS() {
        return OS;
    }
    public boolean isBot() {
        return isBot;
    }

    @Override
    public String toString() {
        return "UserAgent{" + "browser=" + browser + ", OS=" + OS + ", BOT=" + isBot +'}';
    }
}
