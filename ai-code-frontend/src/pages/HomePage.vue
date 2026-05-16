<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { useUserStore } from '@/stores/userStore'
import { addApp, listMyAppVoByPage, listGoodAppVoByPage } from '@/api/app'
import { getDeployUrl } from '@/config/env'
import AppCard from '@/components/AppCard.vue'

const router = useRouter()
const userStore = useUserStore()

const userPrompt = ref('')
const creating = ref(false)

const myApps = ref<API.AppVO[]>([])
const myAppsPage = reactive({ current: 1, pageSize: 6, total: 0 })

const featuredApps = ref<API.AppVO[]>([])
const featuredAppsPage = reactive({ current: 1, pageSize: 6, total: 0 })

const setPrompt = (prompt: string) => { userPrompt.value = prompt }

const createApp = async () => {
  if (!userPrompt.value.trim()) { message.warning('请输入应用描述'); return }
  if (!userStore.loginUser?.id) { message.warning('请先登录'); await router.push('/user/login'); return }
  creating.value = true
  try {
    const res = await addApp({ initPrompt: userPrompt.value.trim() })
    if (res.data.code === 0 && res.data.data) {
      message.success('应用创建成功')
      await router.push(`/app/chat/${String(res.data.data)}`)
    } else {
      message.error('创建失败：' + res.data.message)
    }
  } catch (e) {
    message.error('创建失败，请重试')
  } finally {
    creating.value = false
  }
}

const loadMyApps = async () => {
  if (!userStore.loginUser?.id) return
  try {
    const res = await listMyAppVoByPage({ pageNum: myAppsPage.current, pageSize: myAppsPage.pageSize, sortField: 'createTime', sortOrder: 'desc' })
    if (res.data.code === 0 && res.data.data) {
      myApps.value = res.data.data.records || []
      myAppsPage.total = res.data.data.totalRow || 0
    }
  } catch (e) { console.error(e) }
}

const loadFeaturedApps = async () => {
  try {
    const res = await listGoodAppVoByPage({ pageNum: featuredAppsPage.current, pageSize: featuredAppsPage.pageSize, sortField: 'createTime', sortOrder: 'desc' })
    if (res.data.code === 0 && res.data.data) {
      featuredApps.value = res.data.data.records || []
      featuredAppsPage.total = res.data.data.totalRow || 0
    }
  } catch (e) { console.error(e) }
}

const viewChat = (appId: string | number | undefined) => {
  if (appId) router.push(`/app/chat/${appId}?view=1`)
}

const viewWork = (app: API.AppVO) => {
  if (app.deployKey) window.open(getDeployUrl(app.deployKey), '_blank')
}

onMounted(() => {
  loadMyApps()
  loadFeaturedApps()
  const handleMouseMove = (e: MouseEvent) => {
    document.documentElement.style.setProperty('--mouse-x', `${(e.clientX / window.innerWidth) * 100}%`)
    document.documentElement.style.setProperty('--mouse-y', `${(e.clientY / window.innerHeight) * 100}%`)
  }
  document.addEventListener('mousemove', handleMouseMove)
  return () => document.removeEventListener('mousemove', handleMouseMove)
})
</script>

<template>
  <div id="homePage">
    <div class="container">
      <div class="hero-section">
        <h1 class="hero-title">AI 应用生成平台</h1>
        <p class="hero-description">一句话轻松创建网站应用</p>
      </div>

      <div class="input-section">
        <a-textarea v-model:value="userPrompt" placeholder="帮我创建个人博客网站" :rows="4" :maxlength="1000" class="prompt-input" />
        <div class="input-actions">
          <a-button type="primary" size="large" @click="createApp" :loading="creating">
            <template #icon><span>↑</span></template>
          </a-button>
        </div>
      </div>

      <div class="quick-actions">
        <a-button type="default" @click="setPrompt('创建一个现代化的个人博客网站，包含文章列表、详情页、分类标签、搜索功能、评论系统和个人简介页面。')">个人博客网站</a-button>
        <a-button type="default" @click="setPrompt('设计一个专业的企业官网，包含公司介绍、产品服务展示、新闻资讯、联系我们等页面。')">企业官网</a-button>
        <a-button type="default" @click="setPrompt('构建一个功能完整的在线商城，包含商品展示、购物车、用户注册登录、订单管理、支付结算等功能。')">在线商城</a-button>
        <a-button type="default" @click="setPrompt('制作一个精美的作品展示网站，适合设计师、摄影师、艺术家等创作者。包含作品画廊、项目详情页、个人简历、联系方式等模块。')">作品展示网站</a-button>
      </div>

      <div class="section">
        <h2 class="section-title">我的作品</h2>
        <div class="app-grid">
          <AppCard v-for="app in myApps" :key="app.id" :app="app" @view-chat="viewChat" @view-work="viewWork" />
        </div>
        <div class="pagination-wrapper">
          <a-pagination v-model:current="myAppsPage.current" v-model:page-size="myAppsPage.pageSize" :total="myAppsPage.total" :show-size-changer="false" :show-total="(total: number) => `共 ${total} 个应用`" @change="loadMyApps" />
        </div>
      </div>

      <div class="section">
        <h2 class="section-title">精选案例</h2>
        <div class="featured-grid">
          <AppCard v-for="app in featuredApps" :key="app.id" :app="app" :featured="true" @view-chat="viewChat" @view-work="viewWork" />
        </div>
        <div class="pagination-wrapper">
          <a-pagination v-model:current="featuredAppsPage.current" v-model:page-size="featuredAppsPage.pageSize" :total="featuredAppsPage.total" :show-size-changer="false" :show-total="(total: number) => `共 ${total} 个案例`" @change="loadFeaturedApps" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
#homePage { width: 100%; min-height: 100vh; background: linear-gradient(180deg, #f8fafc 0%, #e2e8f0 100%); position: relative; overflow: hidden; }
#homePage::before { content: ''; position: absolute; inset: 0; background-image: linear-gradient(rgba(59,130,246,0.05) 1px, transparent 1px), linear-gradient(90deg, rgba(59,130,246,0.05) 1px, transparent 1px); background-size: 100px 100px; pointer-events: none; }
#homePage::after { content: ''; position: absolute; inset: 0; background: radial-gradient(600px circle at var(--mouse-x, 50%) var(--mouse-y, 50%), rgba(59,130,246,0.08) 0%, transparent 80%); pointer-events: none; }
.container { max-width: 1200px; margin: 0 auto; padding: 20px; position: relative; z-index: 2; }
.hero-section { text-align: center; padding: 80px 0 60px; margin-bottom: 28px; }
.hero-title { font-size: 56px; font-weight: 700; margin: 0 0 20px; background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 50%, #10b981 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text; }
.hero-description { font-size: 20px; margin: 0; color: #64748b; }
.input-section { position: relative; margin: 0 auto 24px; max-width: 800px; }
.prompt-input { border-radius: 16px; font-size: 16px; padding: 20px 60px 20px 20px; background: rgba(255,255,255,0.95); box-shadow: 0 10px 40px rgba(0,0,0,0.2); }
.input-actions { position: absolute; bottom: 12px; right: 12px; }
.quick-actions { display: flex; gap: 12px; justify-content: center; margin-bottom: 60px; flex-wrap: wrap; }
.quick-actions :deep(.ant-btn) { border-radius: 25px; padding: 8px 20px; height: auto; background: rgba(255,255,255,0.8); border: 1px solid rgba(59,130,246,0.2); color: #475569; }
.quick-actions :deep(.ant-btn:hover) { background: rgba(255,255,255,0.9); border-color: rgba(59,130,246,0.4); color: #3b82f6; transform: translateY(-2px); }
.section { margin-bottom: 60px; }
.section-title { font-size: 32px; font-weight: 600; margin-bottom: 32px; color: #1e293b; }
.app-grid, .featured-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 24px; margin-bottom: 32px; }
.pagination-wrapper { display: flex; justify-content: center; margin-top: 32px; }
@media (max-width: 768px) { .hero-title { font-size: 32px; } .app-grid, .featured-grid { grid-template-columns: 1fr; } }
</style>
