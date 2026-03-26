<script setup lang="ts">
import { RouterView } from 'vue-router'
import BasicLayout from '@/layouts/BasicLayout.vue'

import { sayHello } from '@/api/apiController.ts'

sayHello().then((res) => {
  console.log(res)
})
</script>

<template>
  <BasicLayout>
    <RouterView />
  </BasicLayout>
</template>
