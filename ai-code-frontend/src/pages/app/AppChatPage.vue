<template>
  <div id="appChatPage">
    <div class="header-bar">
      <div class="header-left">
        <h1 class="app-name">{{ appInfo?.appName || '网站生成器' }}</h1>
      </div>
      <div class="header-right">
        <a-button type="default" @click="showAppDetail">
          <template #icon><InfoCircleOutlined /></template>
          应用详情
        </a-button>
        <a-button type="primary" @click="handleDeploy" :loading="deploying">
          <template #icon><CloudUploadOutlined /></template>
          部署
        </a-button>
      </div>
    </div>

    <div class="main-content">
      <!-- 左侧对话区域 -->
      <div class="chat-section">
        <div class="messages-container" ref="messagesContainer">
          <div v-for="(msg, index) in messages" :key="index" class="message-item">
            <div v-if="msg.type === 'user'" class="user-message">
              <div class="message-content">{{ msg.content }}</div>
              <div class="message-avatar">
                <a-avatar :src="userStore.loginUser?.userAvatar" />
              </div>
            </div>
            <div v-else class="ai-message">
              <div class="message-avatar">
                <a-avatar style="background-color: #1890ff">AI</a-avatar>
              </div>
              <div class="message-content">
                <MarkdownRenderer v-if="msg.content" :content="msg.content" />
                <div v-if="msg.loading" class="loading-indicator">
                  <a-spin size="small" /><span>AI 正在思考...</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="input-container">
          <div class="input-wrapper">
            <a-tooltip v-if="!isOwner" title="无法在别人的作品下对话哦~" placement="top">
              <a-textarea v-model:value="userInput" placeholder="请描述你想生成的网站" :rows="4" :maxlength="1000" @keydown.enter.prevent="sendMessage" :disabled="isGenerating || !isOwner" />
            </a-tooltip>
            <a-textarea v-else v-model:value="userInput" placeholder="请描述你想生成的网站" :rows="4" :maxlength="1000" @keydown.enter.prevent="sendMessage" :disabled="isGenerating" />
            <div class="input-actions">
              <a-button type="primary" @click="sendMessage" :loading="isGenerating" :disabled="!isOwner">
                <template #icon><SendOutlined /></template>
              </a-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧预览区域 -->
      <div class="preview-section">
        <div class="preview-header">
          <h3>生成后的网页展示</h3>
          <div class="preview-actions">
            <a-button v-if="previewUrl" type="link" @click="openInNewTab">
              <template #icon><ExportOutlined /></template>
              新窗口打开
            </a-button>
          </div>
        </div>
        <div class="preview-content">
          <div v-if="!previewUrl && !isGenerating" class="preview-placeholder">
            <div class="placeholder-icon">🌐</div>
            <p>网站文件生成完成后将在这里展示</p>
          </div>
          <div v-else-if="isGenerating" class="preview-loading">
            <a-spin size="large" /><p>正在生成网站...</p>
          </div>
          <iframe v-else :src="previewUrl" class="preview-iframe" frameborder="0"></iframe>
        </div>
      </div>
    </div>

    <AppDetailModal v-model:open="appDetailVisible" :app="appInfo" :show-actions="isOwner || isAdmin" @edit="editApp" @delete="deleteCurrentApp" />
    <DeploySuccessModal v-model:open="deployModalVisible" :deploy-url="deployUrl" @open-site="openDeployedSite" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { CloudUploadOutlined, SendOutlined, ExportOutlined, InfoCircleOutlined } from '@ant-design/icons-vue'
import { useUserStore } from '@/stores/userStore'
import { getAppVoById, deployApp as deployAppApi, deleteApp as deleteAppApi } from '@/api/app'
import { CodeGenTypeEnum } from '@/utils/codeGenTypes'
import { API_BASE_URL, getStaticPreviewUrl } from '@/config/env'
import MarkdownRenderer from '@/components/MarkdownRenderer.vue'
import AppDetailModal from '@/components/AppDetailModal.vue'
import DeploySuccessModal from '@/components/DeploySuccessModal.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const appInfo = ref<API.AppVO>()
const appId = ref<string>()

interface Message { type: 'user' | 'ai'; content: string; loading?: boolean }
const messages = ref<Message[]>([])
const userInput = ref('')
const isGenerating = ref(false)
const messagesContainer = ref<HTMLElement>()
const hasInitialConversation = ref(false)

const previewUrl = ref('')
const deploying = ref(false)
const deployModalVisible = ref(false)
const deployUrl = ref('')
const appDetailVisible = ref(false)

const isOwner = computed(() => appInfo.value?.userId === userStore.loginUser?.id)
const isAdmin = computed(() => userStore.isAdmin())

const showAppDetail = () => { appDetailVisible.value = true }

const fetchAppInfo = async () => {
  const id = route.params.id as string
  if (!id) { message.error('应用ID不存在'); router.push('/'); return }
  appId.value = id
  try {
    const res = await getAppVoById({ id: id as unknown as number })
    if (res.data.code === 0 && res.data.data) {
      appInfo.value = res.data.data
      const isViewMode = route.query.view === '1'
      if (appInfo.value.initPrompt && !isViewMode && !hasInitialConversation.value) {
        hasInitialConversation.value = true
        await sendInitialMessage(appInfo.value.initPrompt)
      }
    } else {
      message.error('获取应用信息失败'); router.push('/')
    }
  } catch (e) { message.error('获取应用信息失败'); router.push('/') }
}

const sendInitialMessage = async (prompt: string) => {
  messages.value.push({ type: 'user', content: prompt })
  const aiIdx = messages.value.length
  messages.value.push({ type: 'ai', content: '', loading: true })
  await nextTick(); scrollToBottom()
  isGenerating.value = true
  await generateCode(prompt, aiIdx)
}

const sendMessage = async () => {
  if (!userInput.value.trim() || isGenerating.value) return
  const msg = userInput.value.trim()
  userInput.value = ''
  messages.value.push({ type: 'user', content: msg })
  const aiIdx = messages.value.length
  messages.value.push({ type: 'ai', content: '', loading: true })
  await nextTick(); scrollToBottom()
  isGenerating.value = true
  await generateCode(msg, aiIdx)
}

const generateCode = async (userMessage: string, aiMessageIndex: number) => {
  let streamCompleted = false
  try {
    const baseURL = API_BASE_URL
    const params = new URLSearchParams({ appId: appId.value || '', message: userMessage })
    const url = `${baseURL}/app/chat/gen/code?${params}`
    const eventSource = new EventSource(url, { withCredentials: true })
    let fullContent = ''

    eventSource.onmessage = (event) => {
      if (streamCompleted) return
      try {
        const parsed = JSON.parse(event.data)
        const content = parsed.d
        if (content !== undefined && content !== null) {
          fullContent += content
          messages.value[aiMessageIndex].content = fullContent
          messages.value[aiMessageIndex].loading = false
          scrollToBottom()
        }
      } catch (e) { handleError(e, aiMessageIndex) }
    }

    eventSource.addEventListener('done', () => {
      if (streamCompleted) return
      streamCompleted = true; isGenerating.value = false; eventSource.close()
      setTimeout(async () => { await fetchAppInfo(); updatePreview() }, 1000)
    })

    eventSource.onerror = () => {
      if (streamCompleted || !isGenerating.value) return
      if (eventSource.readyState === EventSource.CONNECTING) {
        streamCompleted = true; isGenerating.value = false; eventSource.close()
        setTimeout(async () => { await fetchAppInfo(); updatePreview() }, 1000)
      } else {
        handleError(new Error('SSE连接错误'), aiMessageIndex)
      }
    }
  } catch (e) { handleError(e, aiMessageIndex) }
}

const handleError = (error: unknown, aiMessageIndex: number) => {
  console.error(error)
  messages.value[aiMessageIndex].content = '抱歉，生成过程中出现了错误，请重试。'
  messages.value[aiMessageIndex].loading = false
  message.error('生成失败，请重试')
  isGenerating.value = false
}

const updatePreview = () => {
  if (appId.value) {
    const codeGenType = appInfo.value?.codeGenType || CodeGenTypeEnum.HTML
    previewUrl.value = getStaticPreviewUrl(codeGenType, appId.value)
  }
}

const scrollToBottom = () => {
  if (messagesContainer.value) messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
}

const handleDeploy = async () => {
  if (!appId.value) { message.error('应用ID不存在'); return }
  deploying.value = true
  try {
    const res = await deployAppApi({ appId: appId.value as unknown as number })
    if (res.data.code === 0 && res.data.data) {
      deployUrl.value = res.data.data; deployModalVisible.value = true; message.success('部署成功')
    } else { message.error('部署失败：' + res.data.message) }
  } catch (e) { message.error('部署失败，请重试') } finally { deploying.value = false }
}

const openInNewTab = () => { if (previewUrl.value) window.open(previewUrl.value, '_blank') }
const openDeployedSite = () => { if (deployUrl.value) window.open(deployUrl.value, '_blank') }

const editApp = () => { if (appInfo.value?.id) router.push(`/app/edit/${appInfo.value.id}`) }

const deleteCurrentApp = async () => {
  if (!appInfo.value?.id) return
  try {
    const res = await deleteAppApi({ id: appInfo.value.id })
    if (res.data.code === 0) { message.success('删除成功'); appDetailVisible.value = false; router.push('/') }
    else message.error('删除失败：' + res.data.message)
  } catch (e) { message.error('删除失败') }
}

onMounted(() => { fetchAppInfo() })
</script>

<style scoped>
#appChatPage { height: 100vh; display: flex; flex-direction: column; padding: 16px; background: #fdfdfd; }
.header-bar { display: flex; justify-content: space-between; align-items: center; padding: 12px 16px; }
.header-left { display: flex; align-items: center; gap: 12px; }
.app-name { margin: 0; font-size: 18px; font-weight: 600; color: #1a1a1a; }
.header-right { display: flex; gap: 12px; }
.main-content { flex: 1; display: flex; gap: 16px; padding: 8px; overflow: hidden; }
.chat-section { flex: 2; display: flex; flex-direction: column; background: white; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); overflow: hidden; }
.messages-container { flex: 1; padding: 16px; overflow-y: auto; scroll-behavior: smooth; }
.message-item { margin-bottom: 12px; }
.user-message { display: flex; justify-content: flex-end; align-items: flex-start; gap: 8px; }
.ai-message { display: flex; justify-content: flex-start; align-items: flex-start; gap: 8px; }
.message-content { max-width: 70%; padding: 12px 16px; border-radius: 12px; line-height: 1.5; word-wrap: break-word; }
.user-message .message-content { background: #1890ff; color: white; }
.ai-message .message-content { background: #f5f5f5; color: #1a1a1a; padding: 8px 12px; }
.message-avatar { flex-shrink: 0; }
.loading-indicator { display: flex; align-items: center; gap: 8px; color: #666; }
.input-container { padding: 16px; background: white; }
.input-wrapper { position: relative; }
.input-actions { position: absolute; bottom: 8px; right: 8px; }
.preview-section { flex: 3; display: flex; flex-direction: column; background: white; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); overflow: hidden; }
.preview-header { display: flex; justify-content: space-between; align-items: center; padding: 16px; border-bottom: 1px solid #e8e8e8; }
.preview-header h3 { margin: 0; font-size: 16px; font-weight: 600; }
.preview-content { flex: 1; position: relative; overflow: hidden; }
.preview-placeholder, .preview-loading { display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100%; color: #666; }
.placeholder-icon { font-size: 48px; margin-bottom: 16px; }
.preview-loading p { margin-top: 16px; }
.preview-iframe { width: 100%; height: 100%; border: none; }
@media (max-width: 1024px) { .main-content { flex-direction: column; } .chat-section, .preview-section { flex: none; height: 50vh; } }
</style>
