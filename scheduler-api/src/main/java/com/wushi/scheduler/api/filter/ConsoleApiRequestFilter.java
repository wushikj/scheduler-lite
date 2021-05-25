package com.wushi.scheduler.api.filter;

import com.wushi.scheduler.common.autoconfigure.SchedulerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author yulianghua
 * @date 2021/3/24 5:30 PM
 * @description
 */
@Component
@WebFilter
public class ConsoleApiRequestFilter implements Filter {
    private final SchedulerConfiguration schedulerConfiguration;
    public ConsoleApiRequestFilter(SchedulerConfiguration schedulerConfiguration) {
        this.schedulerConfiguration = schedulerConfiguration;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (!schedulerConfiguration.getConsole().isEnabled()) {
            servletResponse.getWriter().println("Access denied, the console is disabled.");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
