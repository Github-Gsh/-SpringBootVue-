<template>
    <div>
      <h2>欢迎，{{ user?.username }}</h2>
      <button @click="logout">退出登录</button>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import axios from 'axios'
  import { useRouter } from 'vue-router'
  
  const user = ref(null)
  const router = useRouter()
  
  onMounted(async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/me', { withCredentials: true })
      user.value = res.data
    } catch (err) {
      router.push('/login')
    }
  })
  
  const logout = async () => {
    await axios.post('http://localhost:8080/api/logout', {}, { withCredentials: true })
    router.push('/login')
  }
  </script>
  