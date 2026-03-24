// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 GET /api/hello */
export async function sayHello(options?: { [key: string]: any }) {
  return request<string>('/api/hello', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /api/hello */
export async function sayHello3(options?: { [key: string]: any }) {
  return request<string>('/api/hello', {
    method: 'PUT',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /api/hello */
export async function sayHello2(options?: { [key: string]: any }) {
  return request<string>('/api/hello', {
    method: 'POST',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 DELETE /api/hello */
export async function sayHello5(options?: { [key: string]: any }) {
  return request<string>('/api/hello', {
    method: 'DELETE',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PATCH /api/hello */
export async function sayHello4(options?: { [key: string]: any }) {
  return request<string>('/api/hello', {
    method: 'PATCH',
    ...(options || {}),
  })
}
