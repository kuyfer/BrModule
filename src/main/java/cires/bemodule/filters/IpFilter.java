package cires.bemodule.filters;

/**
 * Code provided from:
 *  https://medium.com/@AlexanderObregon/client-ip-address-tracking-in-spring-boot-apis-358bde38296f
 *
 * might not work if there is a reverse proxy, firewall, gateway or loadbalancer in between
 */

import cires.bemodule.utilities.CurrentUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A Spring {@link OncePerRequestFilter} that extracts the client's real IP
 * address from the request and stores it in {@link CurrentUser#INSTANCE}.
 * <p>
 * The filter supports the {@code X-Forwarded-For} header to handle
 * deployments behind reverse proxies, load balancers, or gateways.
 * If the header is absent or blank, {@link HttpServletRequest#getRemoteAddr()}
 * is used as a fallback.
 * </p>
 * <p>
 * The extracted IP address is saved in the thread‑local
 * {@link CurrentUser} instance at the start of the request and
 * automatically cleared (via {@link CurrentUser#clearIpAddress()})
 * after the request completes, ensuring no cross‑request leakage.
 * </p>
 *
 * <p><b>Limitation:</b> This filter relies on the {@code X-Forwarded-For}
 * header being trustworthy. In environments where a malicious client can
 * forge this header, additional protection (e.g., a trusted proxy list)
 * should be implemented.</p>
 *
 * @see CurrentUser
 */
@Component
public class IpFilter extends OncePerRequestFilter {

    /**
     * Extracts the client IP address, stores it in {@link CurrentUser},
     * and then proceeds with the filter chain.
     * <p>
     * The IP is obtained as follows:
     * <ol>
     *   <li>If the {@code X-Forwarded-For} header is present and non‑blank,
     *       the first comma‑separated IP address is used.</li>
     *   <li>Otherwise, {@code request.getRemoteAddr()} is used.</li>
     * </ol>
     * The raw IP is also set as a request attribute
     * {@code "realClientIp"} for potential downstream use.
     * </p>
     *
     * @param request     current HTTP request
     * @param response    current HTTP response
     * @param filterChain the filter chain to continue processing
     * @throws ServletException if the request cannot be filtered
     * @throws IOException      if an I/O error occurs
     */
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