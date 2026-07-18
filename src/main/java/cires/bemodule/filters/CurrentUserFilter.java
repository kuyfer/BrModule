package cires.bemodule.filters;

import cires.bemodule.utilities.CurrentUser;
import jakarta.servlet.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * A Servlet {@link Filter} that extracts the authenticated user’s username
 * from Spring Security’s {@link SecurityContextHolder} and stores it in the
 * thread‑local {@link CurrentUser#INSTANCE} for the duration of the request.
 * <p>
 * This filter is intended to be placed after the authentication filter
 * (e.g., a JWT filter) so that the security context is already populated.
 * If no authentication is available, a {@link NullPointerException} will be
 * thrown – it is assumed that this filter runs only on secured endpoints.
 * </p>
 * <p>
 * The stored username is automatically cleared in a {@code finally} block,
 * preventing cross‑request leakage of the thread‑local value.
 * </p>
 *
 * @see CurrentUser
 * @see SecurityContextHolder
 */
@Component
public class CurrentUserFilter implements Filter {

    /**
     * Stores the authenticated username in {@link CurrentUser#INSTANCE},
     * then proceeds with the filter chain.  After the chain completes
     * (whether normally or exceptionally), the username is removed from
     * the thread‑local storage.
     *
     * @param request  the incoming servlet request
     * @param response the outgoing servlet response
     * @param chain    the filter chain to invoke
     * @throws IOException      if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     * @throws NullPointerException if no authentication is present in
     *                              the security context
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
            CurrentUser.INSTANCE.logIn(username);
            chain.doFilter(request, response);
        } finally {
            CurrentUser.INSTANCE.logOut();
        }
    }
}