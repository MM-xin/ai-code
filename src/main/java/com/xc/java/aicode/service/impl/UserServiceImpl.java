package com.xc.java.aicode.service.impl;

import com.xc.java.aicode.utils.SnowflakeUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xc.java.aicode.constant.UserConstant;
import com.xc.java.aicode.exception.BusinessException;
import com.xc.java.aicode.exception.ErrorCode;
import com.xc.java.aicode.mapper.UserMapper;
import com.xc.java.aicode.model.dto.user.UserQueryRequest;
import com.xc.java.aicode.model.entity.User;
import com.xc.java.aicode.model.vo.UserVO;
import com.xc.java.aicode.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static com.xc.java.aicode.model.entity.table.UserTableDef.USER;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 盐值，用于密码加密
     */
    private static final String SALT = "xc_aicode";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验参数
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度不能小于4位");
        }
        if (userPassword.length() < 6 || checkPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不能小于6位");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }

        // 2. 检查账号是否重复
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(USER.USER_ACCOUNT.eq(userAccount));
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }

        // 3. 加密密码
        String encryptPassword = DigestUtil.md5Hex(SALT + userPassword);

        // 4. 插入数据
        User user = new User();
        user.setId(SnowflakeUtils.nextId());
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserRole(UserConstant.USER_ROLE); // 默认普通用户，值为2
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败");
        }
        return user.getId();
    }

    @Override
    public UserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验参数
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度不能小于4位");
        }
        if (userPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不能小于6位");
        }

        // 2. 加密密码
        String encryptPassword = DigestUtil.md5Hex(SALT + userPassword);

        // 3. 查询用户是否存在
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(USER.USER_ACCOUNT.eq(userAccount))
                .and(USER.USER_PASSWORD.eq(encryptPassword));
        User user = this.getOne(queryWrapper);
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }

        // 4. 记录用户的登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);

        return this.getUserVO(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库重新查询，确保用户信息最新且账号仍然有效
        long userId = currentUser.getId();
        String userPassword = currentUser.getUserPassword();
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(USER.ID.eq(userId))
                .and(USER.USER_PASSWORD.eq(userPassword));
        currentUser = this.getOne(queryWrapper);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public Page<UserVO> listUserByPage(UserQueryRequest userQueryRequest) {
        long pageNum = userQueryRequest.getPageNum();
        long pageSize = userQueryRequest.getPageSize();

        // 构建查询条件
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(USER.ID.eq(userQueryRequest.getId(), userQueryRequest.getId() != null))
                .and(USER.USER_ACCOUNT.like(userQueryRequest.getUserAccount(), StrUtil.isNotBlank(userQueryRequest.getUserAccount())))
                .and(USER.USER_NAME.like(userQueryRequest.getUserName(), StrUtil.isNotBlank(userQueryRequest.getUserName())))
                .and(USER.USER_ROLE.eq(userQueryRequest.getUserRole(), userQueryRequest.getUserRole() != null))
                .orderBy(USER.CREATE_TIME.desc());

        // 分页查询
        Page<User> userPage = this.page(new Page<>(pageNum, pageSize), queryWrapper);

        // 转换为VO
        Page<UserVO> userVOPage = new Page<>(userPage.getPageNumber(), userPage.getPageSize(), userPage.getTotalRow());
        userVOPage.setRecords(userPage.getRecords().stream().map(this::getUserVO).toList());
        return userVOPage;
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        // 0-超级管理员，1-管理员，均视为管理员
        return user != null && user.getUserRole() <= UserConstant.ADMIN_ROLE;
    }
}
