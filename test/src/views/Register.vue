<template>
    <div class="register-container">
      <h2>用户注册</h2>
      <form @submit.prevent="handleRegister">
        <input v-model="username" placeholder="用户名" required />
        <input v-model="password" type="password" placeholder="密码" required />
        <button type="submit">注册</button>
      </form>
      <p v-if="error" style="color: red">{{ error }}</p>
      <p>已有账户？<router-link to="/login">登录</router-link></p>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import axios from 'axios'
  import { useRouter } from 'vue-router'
  
  const username = ref('')
  const password = ref('')
  const error = ref('')
  const router = useRouter()
  
  const handleRegister = async () => {
    try {
      await axios.post('http://localhost:8080/api/register',
        { username: username.value, password: password.value },
        { withCredentials: true }
      )
      // 注册成功，跳转到登录页面
      router.push('/login')
    } catch (err) {
      error.value = err.response?.data || '注册失败'
    }
  }
  </script>
  
  <style scoped>
  .register-container {
    max-width: 300px;
    margin: auto;
    padding-top: 100px;
  }
  input {
    display: block;
    margin-bottom: 10px;
    width: 100%;
  }
  </style>
  