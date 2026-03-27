package com.xc.java.aicode.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.xc.java.aicode.model.dto.role.RoleQueryRequest;
import com.xc.java.aicode.model.entity.Role;
import com.xc.java.aicode.model.vo.RoleVO;

/**
 * 角色服务接口
 */
public interface RoleService extends IService<Role> {

    /**
     * 获取角色视图对象
     *
     * @param role 角色对象
     * @return 角色视图对象
     */
    RoleVO getRoleVO(Role role);

    /**
     * 分页查询角色
     *
     * @param roleQueryRequest 查询请求
     * @return 分页结果
     */
    Page<RoleVO> listRoleByPage(RoleQueryRequest roleQueryRequest);
}
