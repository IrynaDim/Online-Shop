package com.internet.shop.web.filters;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Role;
import com.internet.shop.model.User;
import com.internet.shop.service.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorizationFilter implements Filter {
    public static final Injector injector = Injector.getInstance("com.internet.shop");
    private static final String USER_ID = "user_id";
    private Map<String, List<Role.RoleName>> protectedUrl = new HashMap<>();
    private UserService userService = (UserService) injector.getInstance(UserService.class);

    @Override
    public void init(FilterConfig filterConfig) {
        protectedUrl.put("/products/add", List.of(Role.RoleName.ADMIN));
        protectedUrl.put("/users/delete", List.of(Role.RoleName.ADMIN));
        protectedUrl.put("/users/all", List.of(Role.RoleName.ADMIN));
        protectedUrl.put("/products/manage", List.of(Role.RoleName.ADMIN));
        protectedUrl.put("/products/delete", List.of(Role.RoleName.ADMIN));
        protectedUrl.put("/orders/manage", List.of(Role.RoleName.ADMIN));
        protectedUrl.put("/orders/delete", List.of(Role.RoleName.ADMIN));
        protectedUrl.put("/orders/admin/details", List.of(Role.RoleName.ADMIN));
        protectedUrl.put("/shopping-cart/products", List.of(Role.RoleName.USER));
        protectedUrl.put("/shopping-cart/products/add", List.of(Role.RoleName.USER));
        protectedUrl.put("/shopping-cart/product/delete", List.of(Role.RoleName.USER));
        protectedUrl.put("/orders/complete", List.of(Role.RoleName.USER));
        protectedUrl.put("/orders/by-user", List.of(Role.RoleName.USER));
        protectedUrl.put("/orders/details", List.of(Role.RoleName.USER));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String url = req.getServletPath();
        if (protectedUrl.get(url) == null) {
            filterChain.doFilter(req, resp);
            return;
        }
        Long userId = (Long) req.getSession().getAttribute(USER_ID);
        User user = userService.get(userId);

        if (isAuthorized(user, protectedUrl.get(url))) {
            filterChain.doFilter(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/accessDenied.jsp").forward(req, resp);
        }
    }

    @Override
    public void destroy() {
    }

    private boolean isAuthorized(User user, List<Role.RoleName> authorizedRoles) {
        for (Role.RoleName authorizedRole : authorizedRoles) {
            for (Role userRole : user.getRoles()) {
                if (authorizedRole.equals(userRole.getRoleName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
