package com.anneli.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Filter class that handles valid login and user session
 * 
 * @author Anneli
 * @version 1.0
 * @since 2017-12-13
 */
@WebFilter(filterName = "Filter", urlPatterns = { "/library.xhtml", "/add-serie.xhtml", "/update-serie.xhtml" })
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);
		Object loggedIn = session.getAttribute("user");

		if (session != null && loggedIn != null) {

			filterChain.doFilter(request, response);

		} else {

			resp.sendRedirect("index.xhtml");
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {

	}

}
