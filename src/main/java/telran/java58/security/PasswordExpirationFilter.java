package telran.java58.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import telran.java58.accounting.dao.UserAccountRepository;
import telran.java58.accounting.model.UserAccount;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
@Order(10)
public class PasswordExpirationFilter implements Filter {

    private final UserAccountRepository repository;
    private static final long PASSWORD_EXPIRE_DAYS = 1;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String login = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;

        if (login != null) {
            UserAccount user = repository.findById(login).orElse(null);
            if (user != null && user.getPasswordUpdated() != null) {
                long days = ChronoUnit.DAYS.between(user.getPasswordUpdated(), LocalDate.now());
                if (days > PASSWORD_EXPIRE_DAYS) {
                    boolean isPasswordChangeRequest =
                            request.getMethod().equalsIgnoreCase("PATCH") &&
                                    request.getServletPath().equals("/account/password");

                    if (!isPasswordChangeRequest) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("Password expired. Only password change is allowed.");
                        return;
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }
}

