<script setup>
import { useRouter } from 'vue-router'
</script>

<template>
  <div>
    <!-- <h1>欢迎使用 Vue 登录系统</h1> -->
    <router-view></router-view>  <!-- 路由视图占位 -->
  </div>
</template>

<style scoped>
h1 {
  text-align: center;
  margin-top: 20px;
}
</style>
