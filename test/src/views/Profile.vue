<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const profile = ref({})

onMounted(async () => {
  try {
    console.log('发送请求获取个人资料...')
    const res = await axios.get('/api/profile', { withCredentials: true })
    
    // 打印返回的响应数据
    console.log('返回的个人资料数据:', res.data)

    // 将数据赋值给 profile
    profile.value = res.data
  } catch (error) {
    console.error('获取用户信息失败:', error)
    
    if (error.response) {
      console.error('响应状态码:', error.response.status)
      console.error('响应体:', error.response.data)
    }
  }
})
</script>

<template>
  <div style="border: 1px solid red; padding: 10px;">
    <h2>个人资料</h2>
    <p>姓名：{{ profile.name }}</p>
    <p>邮箱：{{ profile.email }}</p>
    <p>电话：{{ profile.phone }}</p>
  </div>
</template>
