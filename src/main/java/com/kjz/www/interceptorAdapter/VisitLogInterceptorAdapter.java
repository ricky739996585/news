package com.kjz.www.interceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;

public class VisitLogInterceptorAdapter extends HandlerInterceptorAdapter {

	private static final Log log = LogFactory.getLog("visitLog");

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		// System.out.println("action之前执行！！！");

		if(request == null) {
			log.info("VisitLogInterceptorAdapter|preHandle|request is null,false");
			return false;
		}
		
		StringBuffer buf = new StringBuffer();
		String parameter = request.getParameterMap() == null ? "" : JSON.toJSONString(request.getParameterMap());
		buf.append("|").append("X-Real-IP=").append(request.getHeader("X-Real-IP"));
		buf.append("|").append("RequestURI=").append(request.getRequestURI());
		buf.append("|").append("RequestURL=").append(request.getRequestURL());
		buf.append("|").append("referer=").append(request.getHeader("referer"));
		buf.append("|").append("RequestedSessionId=").append(request.getRequestedSessionId());
		buf.append("|").append("User-Agent=").append(request.getHeader("User-Agent"));
		buf.append("|").append("parameter=").append(parameter);
		buf.append("|").append("SessionId=").append(request.getSession().getId());
		buf.append("|").append("RemoteAddr=").append(request.getRemoteAddr());
		buf.append("|").append("RemoteHost=").append(request.getRemoteHost());
		buf.append("|").append("RemotePort=").append(request.getRemotePort());
		log.info(buf.toString());
		try {
			return super.preHandle(request, response, handler);
		} catch (Exception e) {
			log.info("VisitLogInterceptorAdapter|preHandle|" + e);
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// System.out.println("Action执行之后，生成视图之前执行！！");
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// System.out.println("最后执行！！！一般用于释放资源！！");
		super.afterCompletion(request, response, handler, ex);
	}
}
