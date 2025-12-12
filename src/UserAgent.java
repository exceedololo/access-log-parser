public class UserAgent {
    final String OS;
    final String browser;

    public UserAgent(String UAstring) {
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

    @Override
    public String toString() {
        return "UserAgent{" + "browser=" + browser + ", OS=" + OS + '}';
    }
}
