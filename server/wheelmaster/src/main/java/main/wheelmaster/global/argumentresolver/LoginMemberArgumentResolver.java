//package main.wheelmaster.global.argumentresolver;
//
//import lombok.extern.slf4j.Slf4j;
//import main.wheelmaster.member.entity.Member;
//import org.springframework.core.MethodParameter;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import static main.wheelmaster.member.SessionConst.LOGIN_MEMBER;
//
//@Slf4j
//public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
//
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        log.info("this is parameter = {}", parameter);
//
//        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
//        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
//        boolean hasMemberId = Long.class.isAssignableFrom(parameter.getParameterType());
//
//
//        log.info("interceptor support = {}", hasMemberType);
//
//        return hasLoginAnnotation && (hasMemberType || hasMemberId);
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//
//        HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
//        HttpSession session = request.getSession(false);
//
//        if(session == null){
//            return null;
//        }
//
//        Member member = (Member)session.getAttribute(LOGIN_MEMBER);
//
//        if(Long.class.equals(parameter.getParameterType())){
//            return member.getMemberId();
//        }
//
//        return session.getAttribute(LOGIN_MEMBER);
//    }
//}