<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, RouterView } from 'vue-router'
import BasicLayout from '@/layouts/BasicLayout.vue'

const route = useRoute()
// AppChatPage 是全屏页面，不使用 BasicLayout
const useBasicLayout = computed(() => !route.path.startsWith('/app/chat'))
</script>

<template>
  <BasicLayout v-if="useBasicLayout">
    <RouterView />
  </BasicLayout>
  <RouterView v-else />
</template>
