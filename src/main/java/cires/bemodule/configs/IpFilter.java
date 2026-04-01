package cires.bemodule.configs;
// Code provided from https://medium.com/@AlexanderObregon/client-ip-address-tracking-in-spring-boot-apis-358bde38296f

import cires.bemodule.utilities.CurrentUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
// TODO: Maybe switch to IPv4
// TODO: change it ContextFilter ?? add ProcessTime
// might not work if there is a reverse proxy, firewall, gateway or loadbalancer in between
@Component
public class IpFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("X-Forwarded-For");
        String clientIp = header != null && !header.isBlank()
                ? header.split(",")[0].trim()
                : request.getRemoteAddr();

        request.setAttribute("realClientIp", clientIp);
        try{
            CurrentUser.INSTANCE.setIpAddress(clientIp);
            filterChain.doFilter(request, response);
        } finally {
            CurrentUser.INSTANCE.clearIpAddress();
        }
    }
}