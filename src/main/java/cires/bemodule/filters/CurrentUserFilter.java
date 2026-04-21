package cires.bemodule.filters;

import cires.bemodule.utilities.CurrentUser;
import jakarta.servlet.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class CurrentUserFilter implements Filter {

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
