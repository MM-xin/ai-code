package com.xc.java.aicode.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xc.java.aicode.mapper.RoleMapper;
import com.xc.java.aicode.model.dto.role.RoleQueryRequest;
import com.xc.java.aicode.model.entity.Role;
import com.xc.java.aicode.model.vo.RoleVO;
import com.xc.java.aicode.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static com.xc.java.aicode.model.entity.table.RoleTableDef.ROLE;

/**
 * 角色服务实现类
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public RoleVO getRoleVO(Role role) {
        if (role == null) {
            return null;
        }
        RoleVO roleVO = new RoleVO();
        BeanUtils.copyProperties(role, roleVO);
        return roleVO;
    }

    @Override
    public Page<RoleVO> listRoleByPage(RoleQueryRequest roleQueryRequest) {
        long pageNum = roleQueryRequest.getPageNum();
        long pageSize = roleQueryRequest.getPageSize();

        // 构建查询条件
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(ROLE.ID.eq(roleQueryRequest.getId(), roleQueryRequest.getId() != null))
                .and(ROLE.ROLE_NAME.like(roleQueryRequest.getRoleName(), StrUtil.isNotBlank(roleQueryRequest.getRoleName())))
                .and(ROLE.ROLE_CODE.eq(roleQueryRequest.getRoleCode(), StrUtil.isNotBlank(roleQueryRequest.getRoleCode())))
                .orderBy(ROLE.CREATE_TIME.desc());

        // 分页查询
        Page<Role> rolePage = this.page(new Page<>(pageNum, pageSize), queryWrapper);

        // 转换为VO
        Page<RoleVO> roleVOPage = new Page<>(rolePage.getPageNumber(), rolePage.getPageSize(), rolePage.getTotalRow());
        roleVOPage.setRecords(rolePage.getRecords().stream().map(this::getRoleVO).toList());
        return roleVOPage;
    }
}
