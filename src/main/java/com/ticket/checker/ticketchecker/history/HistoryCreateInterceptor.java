package com.ticket.checker.ticketchecker.history;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ticket.checker.ticketchecker.security.HistoryRepository;
import com.ticket.checker.ticketchecker.users.UserUtil;
 
public class HistoryCreateInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	private HistoryRepository historyRepository;
	
	@Autowired 
	private UserUtil userUtil;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String authorization = request.getHeader("Authorization");
		
		String username = userUtil.getUsernameFromAuthorization(authorization);
		String requestURI = request.getRequestURI();
		String requestMethod = request.getMethod();
		int responseStatus = response.getStatus();
		
		History history = new History(new Date(), username, requestURI, requestMethod, responseStatus);
		historyRepository.save(history);
	}
}
