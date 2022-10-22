//package main.wheelmaster.global.interceptor;
//
//import lombok.extern.slf4j.Slf4j;
//import main.wheelmaster.member.SessionConst;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//@Slf4j
//public class LoginCheckInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        String requestURI = request.getRequestURI();
//        log.info("요청 경로={}", requestURI);
//
//        HttpSession session = request.getSession(false);
//
//        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
//            log.info("미인증 사용자 요청");
//
//            response.sendRedirect("members/login?redirectURL=" + requestURI);
//            return false;
//        }
//
//        return true;
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        if(ex != null){
//            log.info("requestURI=[{}], Exception cause = [{}], message = [{}]", request.getRequestURI() ,ex.getCause(), ex.getMessage());
//        }
//    }
//}