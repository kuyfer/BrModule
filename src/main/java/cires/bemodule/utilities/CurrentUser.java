package cires.bemodule.utilities;

// Code provided from: https://docs.hibernate.org/orm/current/userguide/html_single/#envers-tracking-modified-entities-revchanges
// Current User is used in the CustomRevisionEntityListener to get the user who made the change
public class CurrentUser {

    public static final CurrentUser INSTANCE = new CurrentUser();

    private static final ThreadLocal<String> Userstorage = new ThreadLocal<>();

    private static final ThreadLocal<String> addrStorage = new ThreadLocal<>();

    public void logIn(String user) {Userstorage.set(user);}

    public void logOut() {Userstorage.remove();}

    public String get() {return Userstorage.get();}

    public void setIpAddress(String ip) { addrStorage.set(ip); }

    public String getIpAddress() { return addrStorage.get(); }

    public void clearIpAddress() { addrStorage.remove(); }

    public void clear() {
        Userstorage.remove();
        addrStorage.remove();
    }
}