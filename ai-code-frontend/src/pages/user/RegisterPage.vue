<script setup lang="ts">
/**
 * 用户注册页面
 */
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { UserOutlined, LockOutlined, SmileOutlined } from '@ant-design/icons-vue'
import { userRegister } from '@/api/user'

const router = useRouter()
const loading = ref(false)

const formState = reactive({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
  userName: '',
})

const handleSubmit = async () => {
  if (formState.userPassword !== formState.checkPassword) {
    message.error('两次输入的密码不一致')
    return
  }
  loading.value = true
  const res = await userRegister(formState)
  loading.value = false
  if (res.data.code === 0) {
    message.success('注册成功，请登录')
    router.push('/user/login')
  } else {
    message.error(res.data.message || '注册失败')
  }
}
</script>

<template>
  <div class="auth-page">
    <div class="auth-box">
      <div class="auth-header">
        <h2 class="auth-title">创建账号</h2>
        <p class="auth-subtitle">填写以下信息完成注册</p>
      </div>

      <a-form :model="formState" layout="vertical" @finish="handleSubmit" class="auth-form">
        <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号' }, { min: 4, message: '账号长度不能小于4位' }]">
          <a-input v-model:value="formState.userAccount" placeholder="账号（至少4位）" size="large" allow-clear>
            <template #prefix><UserOutlined style="color: #bfbfbf" /></template>
          </a-input>
        </a-form-item>

        <a-form-item name="userPassword" :rules="[{ required: true, message: '请输入密码' }, { min: 6, message: '密码长度不能小于6位' }]">
          <a-input-password v-model:value="formState.userPassword" placeholder="密码（至少6位）" size="large">
            <template #prefix><LockOutlined style="color: #bfbfbf" /></template>
          </a-input-password>
        </a-form-item>

        <a-form-item name="checkPassword" :rules="[{ required: true, message: '请确认密码' }]">
          <a-input-password v-model:value="formState.checkPassword" placeholder="确认密码" size="large">
            <template #prefix><LockOutlined style="color: #bfbfbf" /></template>
          </a-input-password>
        </a-form-item>

        <!-- 昵称可为空，后端默认"无名" -->
        <a-form-item name="userName">
          <a-input v-model:value="formState.userName" placeholder="昵称（可为空，默认无名）" size="large" allow-clear>
            <template #prefix><SmileOutlined style="color: #bfbfbf" /></template>
          </a-input>
        </a-form-item>

        <a-form-item style="margin-bottom: 12px">
          <a-button type="primary" html-type="submit" block size="large" :loading="loading">
            注 册
          </a-button>
        </a-form-item>

        <div class="auth-footer-link">
          已有账号？<RouterLink to="/user/login">立即登录</RouterLink>
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
