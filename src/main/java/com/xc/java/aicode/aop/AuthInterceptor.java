package com.xc.java.aicode.aop;

import com.xc.java.aicode.annotation.AuthCheck;
import com.xc.java.aicode.exception.BusinessException;
import com.xc.java.aicode.exception.ErrorCode;
import com.xc.java.aicode.model.entity.User;
import com.xc.java.aicode.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 权限校验切面
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 执行权限校验
     *
     * @param joinPoint  切入点
     * @param authCheck  权限校验注解
     * @return 方法执行结果
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        int mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        
        // 校验用户角色（数值越小权限越高：0-超级管理员，1-管理员，2-普通用户）
        if (mustRole >= 0) {
            int userRole = loginUser.getUserRole();
            if (userRole > mustRole) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        
        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}
