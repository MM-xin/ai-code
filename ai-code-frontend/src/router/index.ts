/**
 * 路由配置
 * meta.requireAuth: true  需要登录才能访问
 * meta.requireAdmin: true 需要管理员权限才能访问
 */
import { createRouter, createWebHistory } from 'vue-router'
import { message } from 'ant-design-vue'
import { useUserStore } from '@/stores/userStore'
import HomeView from '@/pages/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/user/login',
      name: 'login',
      component: () => import('@/pages/user/LoginPage.vue'),
    },
    {
      path: '/user/register',
      name: 'register',
      component: () => import('@/pages/user/RegisterPage.vue'),
    },
    {
      // 用户管理页面，仅管理员可访问
      path: '/admin/user',
      name: 'userManage',
      component: () => import('@/pages/admin/UserManagePage.vue'),
      meta: { requireAdmin: true },
    },
  ],
})

/**
 * 全局路由守卫
 * 处理登录校验和权限控制
 */
router.beforeEach(async (to) => {
  const userStore = useUserStore()

  // 如果 store 中没有用户信息，尝试从后端获取（刷新页面后恢复登录态）
  if (!userStore.loginUser) {
    await userStore.fetchLoginUser()
  }

  // 需要管理员权限的页面
  if (to.meta.requireAdmin) {
    if (!userStore.isLoggedIn()) {
      message.warning('请先登录')
      return `/user/login?redirect=${to.fullPath}`
    }
    if (!userStore.isAdmin()) {
      message.error('无权限访问')
      return '/'
    }
  }

  // 需要登录的页面
  if (to.meta.requireAuth && !userStore.isLoggedIn()) {
    message.warning('请先登录')
    return `/user/login?redirect=${to.fullPath}`
  }
})

export default router
