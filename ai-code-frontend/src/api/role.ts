// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 添加角色 POST /role/add */
export async function addRole(body: API.RoleAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/role/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除角色 POST /role/delete */
export async function deleteRole(body: API.DeleteRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/role/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 根据ID获取角色 GET /role/get */
export async function getRoleById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getRoleByIdParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseRoleVO>('/role/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 分页获取角色列表 POST /role/list/page */
export async function listRoleByPage(body: API.RoleQueryRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponsePageRoleVO>('/role/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 更新角色 POST /role/update */
export async function updateRole(body: API.RoleUpdateRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/role/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
