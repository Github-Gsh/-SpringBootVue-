<template>
  <div class="login-container">
    <h2>用户登录</h2>
    <form @submit.prevent="handleLogin">
      <input v-model="username" placeholder="用户名" required />
      <input v-model="password" type="password" placeholder="密码" required />
      <button type="submit" :disabled="loading">
        {{ loading ? '登录中...' : '登录' }}
      </button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
    <p class="register-link">
      还没有账号？<router-link to="/register">去注册</router-link>
    </p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const username = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)
const router = useRouter()

const handleLogin = async () => {
  loading.value = true
  error.value = ''
  try {
    await axios.post(
      'http://localhost:8080/api/login',
      { username: username.value, password: password.value },
      { withCredentials: true }
    )
    router.push('/adminMain') // 登录成功后跳转页面
  } catch (err) {
    error.value = err.response?.data || '登录失败'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  max-width: 350px;
  margin: auto;
  padding-top: 100px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #f9f9f9;
}

input {
  display: block;
  margin-bottom: 12px;
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #42b983;
  border: none;
  color: white;
  font-weight: bold;
  cursor: pointer;
  border-radius: 4px;
}

button[disabled] {
  background-color: #aaa;
  cursor: not-allowed;
}

.error {
  color: red;
  margin-top: 10px;
}

.register-link {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
}
</style>
