package cires.bemodule.utilities;

/**
 * Thread‑local holder for the currently logged‑in username and the client’s
 * IP address.
 * <p>
 * Intended to be used by
 * {@code CustomRevisionEntityListener} (Hibernate Envers) so that every
 * revision entry can record <em>who</em> made the change and <em>from which
 * IP</em> the request came. Because the data is stored in a
 * {@link ThreadLocal}, it is isolated per‑request and automatically cleared
 * when the thread terminates.
 * </p>
 * <p>
 * Code adapted from the official Hibernate ORM documentation:
 * <a href="https://docs.hibernate.org/orm/current/userguide/html_single/#envers-tracking-modified-entities-revchanges">
 * Envers – Tracking modified entities</a>
 * </p>
 *
 * @see cires.bemodule.entities.CustomRevisionEntity
 */
public class CurrentUser {

    /**
     * Singleton instance. Access via {@link CurrentUser#INSTANCE}.
     */
    public static final CurrentUser INSTANCE = new CurrentUser();

    /**
     * Thread‑local storage for the currently logged‑in username.
     */
    private static final ThreadLocal<String> Userstorage = new ThreadLocal<>();

    /**
     * Thread‑local storage for the client’s IP address.
     */
    private static final ThreadLocal<String> addrStorage = new ThreadLocal<>();

    /**
     * Stores the username of the currently authenticated user.
     * Should be called at the beginning of a request (e.g., in a filter or
     * interceptor) and cleared at the end.
     *
     * @param user the username (typically extracted from the JWT or
     *             {@code UserPrincipal})
     */
    public void logIn(String user) {Userstorage.set(user);}

    /**
     * Removes the currently stored username.
     * Invoke after the request has completed to prevent memory leaks.
     */
    public void logOut() {Userstorage.remove();}

    /**
     * Returns the username stored for the current thread, or {@code null} if
     * none has been set.
     *
     * @return the current username, or {@code null}
     */
    public String get() {return Userstorage.get();}

    /**
     * Associates the client’s IP address with the current thread.
     *
     * @param ip the IP address string (e.g., "192.168.1.42")
     */
    public void setIpAddress(String ip) { addrStorage.set(ip); }

    /**
     * Returns the IP address stored for the current thread, or {@code null}
     * if none has been set.
     *
     * @return the client IP address, or {@code null}
     */
    public String getIpAddress() { return addrStorage.get(); }

    /**
     * Removes the stored IP address. Usually called together with
     * {@link #logOut()} at the end of a request.
     */
    public void clearIpAddress() { addrStorage.remove(); }
}