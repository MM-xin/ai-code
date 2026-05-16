declare namespace API {
  type AppAddRequest = {
    /** 应用初始化 prompt */
    initPrompt?: string
  }

  type AppAdminUpdateRequest = {
    /** 应用 ID */
    id?: number
    /** 应用名称 */
    appName?: string
    /** 应用封面 */
    cover?: string
    /** 优先级 */
    priority?: number
  }

  type AppDeployRequest = {
    appId?: number
  }

  type AppQueryRequest = {
    pageNum?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    /** 应用 ID */
    id?: number
    /** 应用名称 */
    appName?: string
    /** 应用封面 */
    cover?: string
    /** 应用初始化 prompt */
    initPrompt?: string
    /** 代码生成类型 */
    codeGenType?: string
    /** 部署标识 */
    deployKey?: string
    /** 优先级 */
    priority?: number
    /** 创建用户 ID */
    userId?: number
    /** 是否删除 */
    isDelete?: number
  }

  type AppUserUpdateRequest = {
    /** 应用 ID */
    id?: number
    /** 应用名称 */
    appName?: string
  }

  type AppVO = {
    /** 应用 ID */
    id?: number
    /** 应用名称 */
    appName?: string
    /** 应用封面 */
    cover?: string
    /** 应用初始化 prompt */
    initPrompt?: string
    /** 代码生成类型 */
    codeGenType?: string
    /** 部署标识 */
    deployKey?: string
    /** 部署时间 */
    deployedTime?: string
    /** 优先级 */
    priority?: number
    /** 创建用户 ID */
    userId?: number
    /** 创建时间 */
    createTime?: string
    /** 更新时间 */
    updateTime?: string
    user?: UserVO
  }

  type BaseResponseAppVO = {
    code?: number
    data?: AppVO
    message?: string
  }

  type BaseResponseBoolean = {
    code?: number
    data?: boolean
    message?: string
  }

  type BaseResponseLong = {
    code?: number
    data?: number
    message?: string
  }

  type BaseResponsePageAppVO = {
    code?: number
    data?: PageAppVO
    message?: string
  }

  type BaseResponsePageRoleVO = {
    code?: number
    data?: PageRoleVO
    message?: string
  }

  type BaseResponsePageUserVO = {
    code?: number
    data?: PageUserVO
    message?: string
  }

  type BaseResponseRoleVO = {
    code?: number
    data?: RoleVO
    message?: string
  }

  type BaseResponseString = {
    code?: number
    data?: string
    message?: string
  }

  type BaseResponseUserVO = {
    code?: number
    data?: UserVO
    message?: string
  }

  type chatToGenCodeParams = {
    appId: number
    message: string
  }

  type DeleteRequest = {
    id?: number
  }

  type getAppVOByIdByAdminParams = {
    id: number
  }

  type getAppVOByIdParams = {
    id: number
  }

  type getRoleByIdParams = {
    id: number
  }

  type getUserByIdParams = {
    id: number
  }

  type PageAppVO = {
    records?: AppVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PageRoleVO = {
    records?: RoleVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PageUserVO = {
    records?: UserVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type RoleAddRequest = {
    /** 角色名称 */
    roleName?: string
    /** 角色编码 */
    roleCode?: string
    /** 角色描述 */
    description?: string
  }

  type RoleQueryRequest = {
    pageNum?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    /** 角色ID */
    id?: number
    /** 角色名称 */
    roleName?: string
    /** 角色编码 */
    roleCode?: string
  }

  type RoleUpdateRequest = {
    /** 角色ID */
    id?: number
    /** 角色名称 */
    roleName?: string
    /** 角色描述 */
    description?: string
  }

  type RoleVO = {
    /** 角色ID */
    id?: number
    /** 角色名称 */
    roleName?: string
    /** 角色编码 */
    roleCode?: string
    /** 角色描述 */
    description?: string
    /** 创建时间 */
    createTime?: string
  }

  type ServerSentEventString = true

  type serveStaticResourceParams = {
    deployKey: string
  }

  type UserAddRequest = {
    /** 账号 */
    userAccount?: string
    /** 密码 */
    userPassword?: string
    /** 用户昵称 */
    userName?: string
    /** 用户头像 */
    userAvatar?: string
    /** 用户简介 */
    userProfile?: string
    /** 角色 */
    userRole?: number
  }

  type UserLoginRequest = {
    /** 账号 */
    userAccount?: string
    /** 密码 */
    userPassword?: string
  }

  type UserQueryRequest = {
    pageNum?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    /** 用户ID */
    id?: string
    /** 账号 */
    userAccount?: string
    /** 用户昵称 */
    userName?: string
    /** 角色 */
    userRole?: number
  }

  type UserRegisterRequest = {
    /** 账号 */
    userAccount?: string
    /** 密码 */
    userPassword?: string
    /** 确认密码 */
    checkPassword?: string
    /** 用户昵称（可为空，默认无名） */
    userName?: string
  }

  type UserUpdateRequest = {
    /** 用户ID */
    id?: number
    /** 用户昵称 */
    userName?: string
    /** 用户头像 */
    userAvatar?: string
    /** 用户简介 */
    userProfile?: string
    /** 角色 */
    userRole?: number
  }

  type UserVO = {
    /** 用户ID */
    id?: number
    /** 账号 */
    userAccount?: string
    /** 用户昵称 */
    userName?: string
    /** 用户头像 */
    userAvatar?: string
    /** 用户简介 */
    userProfile?: string
    /** 角色 */
    userRole?: number
    /** 创建时间 */
    createTime?: string
  }
}
