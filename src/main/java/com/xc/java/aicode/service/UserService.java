package com.xc.java.aicode.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.xc.java.aicode.model.dto.user.UserQueryRequest;
import com.xc.java.aicode.model.entity.User;
import com.xc.java.aicode.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   账号
     * @param userPassword  密码
     * @param checkPassword 确认密码
     * @return 新用户ID
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  账号
     * @param userPassword 密码
     * @param request      请求对象
     * @return 脱敏后的用户信息
     */
    UserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request 请求对象
     * @return 当前登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户登出
     *
     * @param request 请求对象
     * @return 是否成功
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的用户信息
     *
     * @param user 用户对象
     * @return 脱敏后的用户信息
     */
    UserVO getUserVO(User user);

    /**
     * 分页查询用户
     *
     * @param userQueryRequest 查询请求
     * @return 分页结果
     */
    Page<UserVO> listUserByPage(UserQueryRequest userQueryRequest);

    /**
     * 是否为管理员
     *
     * @param request 请求对象
     * @return 是否为管理员
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user 用户对象
     * @return 是否为管理员
     */
    boolean isAdmin(User user);
}
