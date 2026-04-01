<script setup lang="ts">
/**
 * 用户管理页面（仅管理员可访问）
 * 支持分页查询、按ID/账号/昵称搜索、删除、修改操作
 */
import { ref, reactive, computed, onMounted, h } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { SearchOutlined, ReloadOutlined } from '@ant-design/icons-vue'
import { listUserByPage, deleteUser, updateUser } from '@/api/yonghuguanli'

// 角色映射
const roleMap: Record<number, { label: string; color: string }> = {
  0: { label: '超级管理员', color: 'red' },
  1: { label: '管理员', color: 'blue' },
  2: { label: '普通用户', color: 'default' },
}

// 时间格式化：2026-03-31T18:22:56 => 2026-03-31 18:22:56
const formatTime = (text: string) => (text ? text.replace('T', ' ').slice(0, 19) : '-')

// 表格列定义
const columns = [
  { title: '头像', dataIndex: 'userAvatar', key: 'userAvatar', width: 72, align: 'center' as const },
  { title: 'ID', dataIndex: 'id', key: 'id', width: 200, ellipsis: true },
  { title: '账号', dataIndex: 'userAccount', key: 'userAccount', width: 120 },
  { title: '昵称', dataIndex: 'userName', key: 'userName', width: 120 },
  { title: '简介', dataIndex: 'userProfile', key: 'userProfile', ellipsis: true },
  { title: '角色', dataIndex: 'userRole', key: 'userRole', width: 110, align: 'center' as const },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    width: 170,
    customRender: ({ text }: { text: string }) => formatTime(text),
  },
  { title: '操作', key: 'action', width: 120, align: 'center' as const },
]

// 搜索表单
const searchForm = reactive({ id: '', userAccount: '', userName: '' })

// 分页参数
const pagination = reactive({ current: 1, pageSize: 10, total: 0 })

// 分页配置（抽离到 script，避免模板中使用反引号导致编译报错）
const paginationConfig = computed(() => ({
  current: pagination.current,
  pageSize: pagination.pageSize,
  total: pagination.total,
  showSizeChanger: true,
  showQuickJumper: true,
  pageSizeOptions: ['10', '20', '50'],
  showTotal: (total: number) => `共 ${total} 条`,
}))

// 表格数据
const tableData = ref<API.UserVO[]>([])
const loading = ref(false)

// 编辑弹窗
const editVisible = ref(false)
const editForm = reactive<API.UserUpdateRequest>({
  id: undefined,
  userName: '',
  userAvatar: '',
  userProfile: '',
  userRole: 2,
})

/** 加载用户列表 */
const loadData = async () => {
  loading.value = true
  const res = await listUserByPage({
    pageNum: pagination.current,
    pageSize: pagination.pageSize,
    id: searchForm.id || undefined,
    userName: searchForm.userName || undefined,
    userAccount: searchForm.userAccount || undefined,
  })
  if (res.data.code === 0 && res.data.data) {
    tableData.value = res.data.data.records || []
    pagination.total = Number(res.data.data.totalRow) || 0
  } else {
    message.error(res.data.message || '获取用户列表失败')
  }
  loading.value = false
}

/** 搜索 */
const handleSearch = () => {
  pagination.current = 1
  loadData()
}

/** 重置搜索 */
const handleReset = () => {
  searchForm.id = ''
  searchForm.userName = ''
  searchForm.userAccount = ''
  pagination.current = 1
  loadData()
}

/** 分页变化 */
const handleTableChange = (pag: any) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  loadData()
}

/** 打开编辑弹窗 */
const handleEdit = (record: API.UserVO) => {
  editForm.id = record.id
  editForm.userName = record.userName || ''
  editForm.userAvatar = record.userAvatar || ''
  editForm.userProfile = record.userProfile || ''
  editForm.userRole = record.userRole ?? 2
  editVisible.value = true
}

/** 提交编辑 */
const handleEditSubmit = async () => {
  const res = await updateUser(editForm)
  if (res.data.code === 0) {
    message.success('修改成功')
    editVisible.value = false
    loadData()
  } else {
    message.error(res.data.message || '修改失败')
  }
}

/** 删除用户 */
const handleDelete = (record: API.UserVO) => {
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除用户「${record.userAccount}」吗？此操作不可恢复。`,
    okText: '确认删除',
    okType: 'danger',
    cancelText: '取消',
    onOk: async () => {
      const res = await deleteUser({ id: record.id })
      if (res.data.code === 0) {
        message.success('删除成功')
        loadData()
      } else {
        message.error(res.data.message || '删除失败')
      }
    },
  })
}

onMounted(() => {
  loadData()
})
</script>


<template>
  <div class="user-manage-page">
    <div class="page-header">
      <h2 class="page-title">用户管理</h2>
      <span class="page-desc">管理系统中的所有用户</span>
    </div>

    <!-- 搜索栏 -->
    <a-card :bordered="false" class="search-card">
      <a-row :gutter="12" align="middle">
        <a-col>
          <a-input
            v-model:value="searchForm.id"
            placeholder="用户ID"
            style="width: 200px"
            allow-clear
            @change="(e: any) => { if (!e.target.value) loadData() }"
          />
        </a-col>
        <a-col>
          <a-input
            v-model:value="searchForm.userAccount"
            placeholder="账号"
            style="width: 160px"
            allow-clear
            @change="(e: any) => { if (!e.target.value) loadData() }"
          />
        </a-col>
        <a-col>
          <a-input
            v-model:value="searchForm.userName"
            placeholder="昵称"
            style="width: 160px"
            allow-clear
            @change="(e: any) => { if (!e.target.value) loadData() }"
          />
        </a-col>
        <a-col>
          <a-space>
            <a-button type="primary" :icon="h(SearchOutlined)" @click="handleSearch">搜索</a-button>
            <a-button :icon="h(ReloadOutlined)" @click="handleReset">重置</a-button>
          </a-space>
        </a-col>
        <a-col flex="auto" style="text-align: right">
          <span class="total-tip">共 {{ pagination.total }} 条记录</span>
        </a-col>
      </a-row>
    </a-card>

    <!-- 用户表格 -->
    <a-card :bordered="false" class="table-card">
      <a-table
        :columns="columns"
        :data-source="tableData"
        :loading="loading"
        :pagination="paginationConfig"
        row-key="id"
        size="middle"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <!-- 头像列 -->
          <template v-if="column.key === 'userAvatar'">
            <a-avatar v-if="record.userAvatar" :src="record.userAvatar" :size="36" />
            <a-avatar v-else :size="36" style="background-color: #1890ff">
              {{ (record.userName || record.userAccount || '?')[0].toUpperCase() }}
            </a-avatar>
          </template>
          <!-- 角色列 -->
          <template v-else-if="column.key === 'userRole'">
            <a-tag :color="roleMap[record.userRole]?.color ?? 'default'">
              {{ roleMap[record.userRole]?.label ?? '未知' }}
            </a-tag>
          </template>
          <!-- 操作列 -->
          <template v-else-if="column.key === 'action'">
            <a-space size="small">
              <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
              <a-divider type="vertical" />
              <a-button type="link" danger size="small" @click="handleDelete(record)">删除</a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 编辑弹窗 -->
    <a-modal
      v-model:open="editVisible"
      title="编辑用户信息"
      width="480px"
      ok-text="保存"
      cancel-text="取消"
      @ok="handleEditSubmit"
    >
      <a-form :model="editForm" layout="vertical" style="margin-top: 16px">
        <a-form-item label="用户昵称">
          <a-input v-model:value="editForm.userName" placeholder="请输入用户昵称" />
        </a-form-item>
        <a-form-item label="头像 URL">
          <a-input v-model:value="editForm.userAvatar" placeholder="请输入头像图片链接" />
        </a-form-item>
        <a-form-item label="用户简介">
          <a-textarea v-model:value="editForm.userProfile" placeholder="请输入用户简介" :rows="3" />
        </a-form-item>
        <a-form-item label="角色">
          <a-select v-model:value="editForm.userRole" style="width: 100%">
            <a-select-option :value="0">超级管理员</a-select-option>
            <a-select-option :value="1">管理员</a-select-option>
            <a-select-option :value="2">普通用户</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<style scoped>
.user-manage-page { padding: 4px 0; }
.page-header { margin-bottom: 20px; }
.page-title { font-size: 20px; font-weight: 600; color: #1a1a1a; margin: 0 0 4px; }
.page-desc { color: #8c8c8c; font-size: 13px; }
.search-card { margin-bottom: 16px; border-radius: 8px; box-shadow: 0 1px 4px rgba(0,0,0,.06); }
.table-card { border-radius: 8px; box-shadow: 0 1px 4px rgba(0,0,0,.06); }
.total-tip { color: #8c8c8c; font-size: 13px; }
</style>
