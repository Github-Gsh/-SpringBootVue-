<template>
  <div class="common-layout">
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header>
        <div class="header-bar">
          <h2>欢迎，{{ user?.username }}</h2>
          <el-button type="danger" @click="logout">退出登录</el-button>
        </div>
      </el-header>

      <!-- 页面主体部分 -->
      <el-container>
        <!-- 侧边栏菜单 -->
        <el-aside width="200px">
          <el-menu :default-active="activeMenu" class="el-menu-vertical-demo" router>
            <el-menu-item index="/adminMain/userlist">用户列表</el-menu-item>
            <el-menu-item index="/adminMain/adminProject">项目列表</el-menu-item>
            <el-menu-item index="/settings">设置</el-menu-item>
          </el-menu>
        </el-aside>

        <!-- 主内容区域 -->
        <el-main>
         <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter, useRoute } from 'vue-router'

const user = ref(null)
const router = useRouter()
const route = useRoute()
const activeMenu = ref(route.path)

onMounted(async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/me', { withCredentials: true })
    user.value = res.data
  } catch (err) {
    router.push('/login')
  }
})

// 退出登录
const logout = async () => {
  await axios.post('http://localhost:8080/api/logout', {}, { withCredentials: true })
  router.push('/login')
}
</script>

<style scoped>
.common-layout {
  height: 100vh;
}

.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 20px;
  background-color: #409EFF;
  color: white;
}

.el-menu-vertical-demo {
  height: 100%;
  border-right: none;
}
</style>
