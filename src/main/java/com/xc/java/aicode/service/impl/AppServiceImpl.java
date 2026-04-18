package com.xc.java.aicode.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xc.java.aicode.exception.BusinessException;
import com.xc.java.aicode.exception.ErrorCode;
import com.xc.java.aicode.mapper.AppMapper;
import com.xc.java.aicode.model.dto.app.AppQueryRequest;
import com.xc.java.aicode.model.entity.App;
import com.xc.java.aicode.model.entity.User;
import com.xc.java.aicode.model.vo.AppVO;
import com.xc.java.aicode.model.vo.UserVO;
import com.xc.java.aicode.service.AppService;
import com.xc.java.aicode.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xc.java.aicode.model.entity.table.AppTableDef.APP;

/**
 * 应用服务实现类
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Resource
    private UserService userService;

    @Override
    public AppVO getAppVO(App app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = new AppVO();
        BeanUtil.copyProperties(app, appVO);
        // 关联查询用户信息
        Long userId = app.getUserId();
        if (userId != null) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            appVO.setUser(userVO);
        }
        return appVO;
    }

    @Override
    public List<AppVO> getAppVOList(List<App> appList) {
        if (CollUtil.isEmpty(appList)) {
            return new ArrayList<>();
        }
        // 批量获取用户信息，避免 N+1 查询问题
        Set<Long> userIds = appList.stream()
                .map(App::getUserId)
                .collect(Collectors.toSet());
        Map<Long, UserVO> userVOMap = userService.listByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, userService::getUserVO));
        return appList.stream().map(app -> {
            AppVO appVO = getAppVO(app);
            UserVO userVO = userVOMap.get(app.getUserId());
            appVO.setUser(userVO);
            return appVO;
        }).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest) {
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String cover = appQueryRequest.getCover();
        String initPrompt = appQueryRequest.getInitPrompt();
        String codeGenType = appQueryRequest.getCodeGenType();
        String deployKey = appQueryRequest.getDeployKey();
        Integer priority = appQueryRequest.getPriority();
        Long userId = appQueryRequest.getUserId();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(APP.ID.eq(id, id != null))
                .and(APP.APP_NAME.like(appName, StrUtil.isNotBlank(appName)))
                .and(APP.COVER.like(cover, StrUtil.isNotBlank(cover)))
                .and(APP.INIT_PROMPT.like(initPrompt, StrUtil.isNotBlank(initPrompt)))
                .and(APP.CODE_GEN_TYPE.eq(codeGenType, StrUtil.isNotBlank(codeGenType)))
                .and(APP.DEPLOY_KEY.eq(deployKey, StrUtil.isNotBlank(deployKey)))
                .and(APP.PRIORITY.eq(priority, priority != null))
                .and(APP.USER_ID.eq(userId, userId != null))
                .and(APP.IS_DELETE.eq(appQueryRequest.getIsDelete(), appQueryRequest.getIsDelete() != null));
        applyAppSort(queryWrapper, sortField, sortOrder);
        return queryWrapper;
    }

    /**
     * 将前端传入的 sortField（Java 属性风格）映射到表列，避免生成非法列名。
     */
    private static void applyAppSort(QueryWrapper queryWrapper, String sortField, String sortOrder) {
        boolean asc = "ascend".equals(sortOrder);
        QueryColumn sortColumn = switch (StrUtil.blankToDefault(sortField, "")) {
            case "updateTime" -> APP.UPDATE_TIME;
            case "editTime" -> APP.EDIT_TIME;
            case "priority" -> APP.PRIORITY;
            case "appName" -> APP.APP_NAME;
            case "deployedTime" -> APP.DEPLOYED_TIME;
            default -> APP.CREATE_TIME;
        };
        if (asc) {
            queryWrapper.orderBy(sortColumn.asc());
        } else {
            queryWrapper.orderBy(sortColumn.desc());
        }
    }

}
