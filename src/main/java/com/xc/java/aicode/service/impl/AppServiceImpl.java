package com.xc.java.aicode.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xc.java.aicode.model.entity.App;
import com.xc.java.aicode.mapper.AppMapper;
import com.xc.java.aicode.service.AppService;
import org.springframework.stereotype.Service;

/**
 * 应用 服务层实现。
 *
 * @author Administrator
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>  implements AppService{

}
