<script setup lang="ts">
/**
 * 用户登录页面
 */
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { userLogin } from '@/api/user'
import { useUserStore } from '@/stores/userStore'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)

const formState = reactive({
  userAccount: '',
  userPassword: '',
})

const handleSubmit = async () => {
  loading.value = true
  const res = await userLogin(formState)
  loading.value = false
  if (res.data.code === 0 && res.data.data) {
    userStore.setLoginUser(res.data.data)
    message.success('登录成功')
    const redirect = new URLSearchParams(window.location.search).get('redirect')
    router.push(redirect || '/')
  } else {
    message.error(res.data.message || '账号或密码错误')
  }
}
</script>

<template>
  <div class="auth-page">
    <div class="auth-box">
      <!-- 标题区域 -->
      <div class="auth-header">
        <h2 class="auth-title">欢迎回来</h2>
        <p class="auth-subtitle">登录你的账号以继续</p>
      </div>

      <a-form :model="formState" layout="vertical" @finish="handleSubmit" class="auth-form">
        <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号' }]">
          <a-input
            v-model:value="formState.userAccount"
            placeholder="请输入账号"
            size="large"
            allow-clear
          >
            <template #prefix><UserOutlined style="color: #bfbfbf" /></template>
          </a-input>
        </a-form-item>

        <a-form-item name="userPassword" :rules="[{ required: true, message: '请输入密码' }]">
          <a-input-password
            v-model:value="formState.userPassword"
            placeholder="请输入密码"
            size="large"
          >
            <template #prefix><LockOutlined style="color: #bfbfbf" /></template>
          </a-input-password>
        </a-form-item>

        <a-form-item style="margin-bottom: 12px">
          <a-button type="primary" html-type="submit" block size="large" :loading="loading">
            登 录
          </a-button>
        </a-form-item>

        <div class="auth-footer-link">
          没有账号？<RouterLink to="/user/register">立即注册</RouterLink>
        </div>
      </a-form>
    </div>
  </div>
</template>

<style scoped>
.auth-page {
  min-height: 80vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f0f4ff 0%, #fafafa 100%);
}

.auth-box {
  width: 420px;
  background: #fff;
  border-radius: 12px;
  padding: 40px 40px 32px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
}

.auth-header {
  text-align: center;
  margin-bottom: 32px;
}

.auth-title {
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0 0 8px;
}

.auth-subtitle {
  color: #8c8c8c;
  margin: 0;
  font-size: 14px;
}

.auth-form :deep(.ant-form-item) {
  margin-bottom: 20px;
}

.auth-footer-link {
  text-align: center;
  color: #8c8c8c;
  font-size: 14px;
}
</style>
