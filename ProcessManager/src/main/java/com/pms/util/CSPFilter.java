package com.pms.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CSPFilter implements Filter {

	private String cspPolicy;
	private String permissionsPolicy;
	private String referrerPolicy;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Initialization code, if needed
		cspPolicy = filterConfig.getInitParameter("cspPolicy");
		if (cspPolicy == null) {
			cspPolicy = "upgrade-insecure-requests; " + "block-all-mixed-content;";
		}

		permissionsPolicy = filterConfig.getInitParameter("permissionsPolicy");
		if (permissionsPolicy == null) {
			permissionsPolicy = "geolocation=(self), microphone=(none), camera=(none), fullscreen=(self), payment=(self)";
		}

		referrerPolicy = filterConfig.getInitParameter("referrerPolicy");
		if (referrerPolicy == null) {
			referrerPolicy = "no-referrer";
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (response instanceof HttpServletResponse) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setHeader("Content-Security-Policy", cspPolicy);
			httpResponse.setHeader("Permissions-Policy", permissionsPolicy);
			httpResponse.setHeader("Referrer-Policy", referrerPolicy);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// Cleanup code, if needed
		cspPolicy = null;
		permissionsPolicy = null;
		referrerPolicy = null;
	}
}
