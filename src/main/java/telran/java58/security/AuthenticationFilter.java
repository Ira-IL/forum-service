package telran.java58.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import telran.java58.accounting.dao.UserAccountRepository;
import telran.java58.accounting.model.UserAccount;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Order(10)
public class AuthenticationFilter implements Filter {
    private final UserAccountRepository repository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (checkEndPoint(request.getMethod(), request.getServletPath())) {
            try {
                String[] credentials = getCredentials(request.getHeader("Authorization"));
                UserAccount user = repository.findById(credentials[0]).orElseThrow(RuntimeException::new);
                if (!BCrypt.checkpw(credentials[1], user.getPassword())) {
                    throw new RuntimeException();
                }
                request = new WrappedRequest(request, user.getLogin());
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean checkEndPoint(String method, String servletPath) {
        return !(HttpMethod.POST.matches(method) && servletPath.matches("/account/register"));
    }

    private String[] getCredentials(String header) {
        String token = header.split(" ")[1];
        token = new String(Base64.getDecoder().decode(token));
        return token.split(":");
    }

    private static class WrappedRequest extends HttpServletRequestWrapper {
        private final String login;

        public WrappedRequest(HttpServletRequest request, String login) {
            super(request);
            this.login = login;
        }

        @Override
        public Principal getUserPrincipal() {
            return () -> login;
        }

    }

}
