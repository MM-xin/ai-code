/**
 * 用户状态管理 Store
 * 负责存储当前登录用户信息，提供登录、登出、获取用户信息等操作
 */
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getLoginUser } from '@/api/yonghuguanli'

export const useUserStore = defineStore('user', () => {
  // 当前登录用户信息，null 表示未登录
  const loginUser = ref<API.UserVO | null>(null)

  /**
   * 从后端获取当前登录用户信息并存入 store
   */
  async function fetchLoginUser() {
    const res = await getLoginUser()
    if (res.data.code === 0 && res.data.data) {
      loginUser.value = res.data.data
    } else {
      loginUser.value = null
    }
  }

  /**
   * 设置登录用户（登录成功后调用）
   */
  function setLoginUser(user: API.UserVO) {
    loginUser.value = user
  }

  /**
   * 清除登录用户（登出后调用）
   */
  function clearLoginUser() {
    loginUser.value = null
  }

  /**
   * 是否已登录
   */
  function isLoggedIn() {
    return loginUser.value !== null
  }

  /**
   * 是否为管理员（userRole <= 1，即超级管理员0或管理员1）
   */
  function isAdmin() {
    return loginUser.value !== null && (loginUser.value.userRole ?? 2) <= 1
  }

  return { loginUser, fetchLoginUser, setLoginUser, clearLoginUser, isLoggedIn, isAdmin }
})
