/**
 * 路由配置
 * meta.requireAuth: true  需要登录才能访问
 * meta.requireAdmin: true 需要管理员权限才能访问
 */
import { createRouter, createWebHistory } from 'vue-router'
import { message } from 'ant-design-vue'
import { useUserStore } from '@/stores/userStore'
import HomePage from '@/pages/HomePage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomePage,
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
      path: '/admin/user',
      name: 'userManage',
      component: () => import('@/pages/admin/UserManagePage.vue'),
      meta: { requireAdmin: true },
    },
    {
      path: '/admin/appManage',
      name: 'appManage',
      component: () => import('@/pages/admin/AppManagePage.vue'),
      meta: { requireAdmin: true },
    },
    {
      path: '/app/chat/:id',
      name: 'appChat',
      component: () => import('@/pages/app/AppChatPage.vue'),
    },
    {
      path: '/app/edit/:id',
      name: 'appEdit',
      component: () => import('@/pages/app/AppEditPage.vue'),
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
