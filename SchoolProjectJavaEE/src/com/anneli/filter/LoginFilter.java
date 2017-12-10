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

import com.anneli.controller.LoginController;
import com.anneli.user.User;


@WebFilter(filterName ="Filter", urlPatterns= {"/library.xhtml","/add-serie.xhtml","/update-serie.xhtml"})
public class LoginFilter implements Filter{
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		System.out.println("i filter dofilter");
				
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;		
		HttpSession session = req.getSession(false);
		Object loggedIn = session.getAttribute("user");
		if(session != null && loggedIn != null) {

			System.out.println("godkänd och inloggad " +loggedIn);
			filterChain.doFilter(request, response);
		}
		else {
			System.out.println("ej inloggad " +loggedIn);
			resp.sendRedirect("index.xhtml");
		}
		
		/*Object isLoggedIn = session.getAttribute("user");
		
		if(isLoggedIn != null && (boolean) isLoggedIn) {
			
			// pass the request along the filter chain
			filterChain.doFilter(request, response);

		}else {
			resp.sendRedirect("index");
		}*/
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {		
		
	}

	@Override
	public void destroy() {
		
	}

}
