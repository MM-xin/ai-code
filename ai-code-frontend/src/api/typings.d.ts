declare namespace API {
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

  type BaseResponseUserVO = {
    code?: number
    data?: UserVO
    message?: string
  }

  type DeleteRequest = {
    id?: number
  }

  type getRoleByIdParams = {
    id: number
  }

  type getUserByIdParams = {
    id: number
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
