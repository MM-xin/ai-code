<script setup lang="ts">
/**
 * 全局顶部导航栏
 * 根据用户角色动态显示菜单项，管理员才显示用户管理入口
 */
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import type { MenuProps } from 'ant-design-vue'
import { userLogout } from '@/api/user'
import { useUserStore } from '@/stores/userStore'

const router = useRouter()
const userStore = useUserStore()

// 当前选中菜单
const selectedKeys = ref<string[]>(['/'])
router.afterEach((to) => {
  selectedKeys.value = [to.path]
})

// 根据权限动态生成菜单
const menuItems = computed(() => {
  const items: MenuProps['items'] = [
    { key: '/', label: '首页', title: '首页' },
  ]
  // 管理员才显示用户管理菜单
  if (userStore.isAdmin()) {
    items.push({ key: '/admin/user', label: '用户管理', title: '用户管理' })
  }
  return items
})

// 处理菜单点击
const handleMenuClick: MenuProps['onClick'] = (e) => {
  router.push(e.key as string)
}

// 登出
const handleLogout = async () => {
  const res = await userLogout()
  if (res.data.code === 0) {
    userStore.clearLoginUser()
    message.success('已退出登录')
    router.push('/user/login')
  } else {
    message.error('登出失败')
  }
}
</script>

<template>
  <a-layout-header class="header">
    <a-row :wrap="false" align="middle">
      <!-- 左侧：Logo 和标题 -->
      <a-col flex="200px">
        <RouterLink to="/">
          <div class="header-left">
            <img class="logo" src="@/assets/logo.png" alt="Logo" />
            <h1 class="site-title">AI 应用生成平台</h1>
          </div>
        </RouterLink>
      </a-col>
      <!-- 中间：导航菜单 -->
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="selectedKeys"
          mode="horizontal"
          :items="menuItems"
          @click="handleMenuClick"
        />
      </a-col>
      <!-- 右侧：用户操作区域 -->
      <a-col>
        <!-- 已登录：头像悬停显示下拉菜单 -->
        <template v-if="userStore.isLoggedIn()">
          <a-dropdown placement="bottomRight">
            <div class="user-avatar-wrap">
              <a-avatar
                v-if="userStore.loginUser?.userAvatar"
                :src="userStore.loginUser.userAvatar"
                :size="36"
                style="cursor: pointer"
              />
              <a-avatar
                v-else
                :size="36"
                style="background-color: #1890ff; cursor: pointer"
              >
                {{ (userStore.loginUser?.userName || userStore.loginUser?.userAccount || '?')[0].toUpperCase() }}
              </a-avatar>
              <span class="username">{{ userStore.loginUser?.userName || userStore.loginUser?.userAccount }}</span>
            </div>
            <template #overlay>
              <a-menu>
                <a-menu-item key="logout" @click="handleLogout">退出登录</a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </template>
        <!-- 未登录：显示登录按钮 -->
        <template v-else>
          <a-button type="primary" @click="router.push('/user/login')">登录</a-button>
        </template>
      </a-col>
    </a-row>
  </a-layout-header>
</template>

<style scoped>
.header {
  background: #fff;
  padding: 0 24px;
}
.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}
.logo {
  height: 48px;
  width: 48px;
}
.site-title {
  margin: 0;
  font-size: 18px;
  color: #1890ff;
  white-space: nowrap;
}
.username {
  color: #333;
  cursor: pointer;
  font-size: 14px;
}
.username:hover {
  color: #1890ff;
}
.user-avatar-wrap {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 6px;
  transition: background 0.2s;
}
.user-avatar-wrap:hover {
  background: #f5f5f5;
}
</style>
