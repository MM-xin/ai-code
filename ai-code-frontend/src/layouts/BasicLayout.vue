<script setup lang="ts">
import GlobalHeader from '@/components/GlobalHeader.vue'
import GlobalFooter from '@/components/GlobalFooter.vue'
</script>

<template>
  <a-layout class="basic-layout">
    <GlobalHeader />
    <a-layout-content class="main-content">
      <router-view />
    </a-layout-content>
    <GlobalFooter />
  </a-layout>
</template>

<style scoped>
.basic-layout {
  min-height: 100vh;
  background: #f5f6fa;
}

.main-content {
  max-width: 1280px;
  width: 100%;
  margin: 24px auto 72px;
  padding: 0 24px;
}
</style>
