package cires.bemodule.utilities;

// Code provided from : https://docs.hibernate.org/orm/current/userguide/html_single/#envers-tracking-modified-entities-revchanges
public class CurrentUser {

    public static final CurrentUser INSTANCE = new CurrentUser();

    private static final ThreadLocal<String> storage = new ThreadLocal<>();

    private static final ThreadLocal<String> addrStorage = new ThreadLocal<>();
    public void logIn(String user) {
        storage.set(user);
    }

    public void logOut() {
        storage.remove();
    }

    public String get() {
        return storage.get();
    }

    public void setIpAddress(String ip) { addrStorage.set(ip); }

    public String getIpAddress() { return addrStorage.get(); }

    public void clearIpAddress() { addrStorage.remove(); }

    public void clear() {
        storage.remove();
        addrStorage.remove();

    }
}