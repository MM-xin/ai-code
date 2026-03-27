package com.xc.java.aicode.controller;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.xc.java.aicode.annotation.AuthCheck;
import com.xc.java.aicode.common.BaseResponse;
import com.xc.java.aicode.common.DeleteRequest;
import com.xc.java.aicode.common.ResultUtils;
import com.xc.java.aicode.constant.UserConstant;
import com.xc.java.aicode.exception.BusinessException;
import com.xc.java.aicode.exception.ErrorCode;
import com.xc.java.aicode.model.dto.role.RoleAddRequest;
import com.xc.java.aicode.model.dto.role.RoleQueryRequest;
import com.xc.java.aicode.model.dto.role.RoleUpdateRequest;
import com.xc.java.aicode.model.entity.Role;
import com.xc.java.aicode.model.vo.RoleVO;
import com.xc.java.aicode.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/role")
@Slf4j
@Tag(name = "角色管理")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 添加角色（仅管理员）
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "添加角色")
    public BaseResponse<Long> addRole(@RequestBody RoleAddRequest roleAddRequest) {
        if (roleAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StrUtil.hasBlank(roleAddRequest.getRoleName(), roleAddRequest.getRoleCode())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "角色名称和编码不能为空");
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleAddRequest, role);
        boolean result = roleService.save(role);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(role.getId());
    }

    /**
     * 删除角色（仅管理员）
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "删除角色")
    public BaseResponse<Boolean> deleteRole(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = roleService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 更新角色（仅管理员）
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "更新角色")
    public BaseResponse<Boolean> updateRole(@RequestBody RoleUpdateRequest roleUpdateRequest) {
        if (roleUpdateRequest == null || roleUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleUpdateRequest, role);
        boolean result = roleService.updateById(role);
        return ResultUtils.success(result);
    }

    /**
     * 根据ID获取角色
     */
    @GetMapping("/get")
    @Operation(summary = "根据ID获取角色")
    public BaseResponse<RoleVO> getRoleById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Role role = roleService.getById(id);
        if (role == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(roleService.getRoleVO(role));
    }

    /**
     * 分页获取角色列表（仅管理员）
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "分页获取角色列表")
    public BaseResponse<Page<RoleVO>> listRoleByPage(@RequestBody RoleQueryRequest roleQueryRequest) {
        if (roleQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<RoleVO> roleVOPage = roleService.listRoleByPage(roleQueryRequest);
        return ResultUtils.success(roleVOPage);
    }
}
