package com.xiupeilian.carpart.session;

import com.xiupeilian.carpart.model.Menu;
import com.xiupeilian.carpart.model.SysUser;
import com.xiupeilian.carpart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class SessionInterceptor implements HandlerInterceptor  {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求路径
        String path=request.getRequestURI();
        //判断是否包含 login
        if(path.contains("login")){
            return  true;
        }else{
            HttpSession session=request.getSession(false);
            if(session==null){
                response.sendRedirect(request.getContextPath()+"/login/toLogin");
                return false;
            }else{
                if(session.getAttribute("user")==null){
                    response.sendRedirect(request.getContextPath()+"/login/toLogin");
                    return false;
                }else{
                    SysUser user=(SysUser) session.getAttribute("user");
                    //查询出该用户对应的权限
                    boolean check=false;
                    List<Menu> menuList=userService.findMenusByUserId(user.getId());

                    for (Menu menu:menuList){
                        if (path.contains(menu.getMenuKey())){
                            check=true;
                        }
                    }

                    if(check){
                        return  true;
                    }else{
                            response.sendRedirect(request.getContextPath()+"/login/noauth");
                            return false;
                    }

                }
            }
        }


    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        request.setAttribute("ctx",request.getContextPath());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
