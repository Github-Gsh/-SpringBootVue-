<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const profile = ref({})
const message = ref('')

onMounted(async () => {
  console.log("发送请求获取个人资料...");
  try {
    const res = await axios.get('/api/profile', { withCredentials: true })
    console.log("返回的个人资料数据:", res.data)
    profile.value = res.data
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
})

const updateProfile = async () => {
  try {
    const res = await axios.post('/api/profile/update', profile.value, { withCredentials: true })
    message.value = '修改成功'
    console.log('修改成功:', res.data)
  } catch (err) {
    message.value = '修改失败'
    console.error('修改失败:', err)
  }
}
</script>

<template>
  <div>
    <h2>个人资料修改</h2>
    <el-form :model="profile" label-width="80px">
      <el-form-item label="姓名">
        <el-input v-model="profile.name" disabled />
      </el-form-item>
      <el-form-item label="年龄">
        <el-input v-model="profile.age" />
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="profile.sex" placeholder="选择性别">
          <el-option label="男" value="男" />
          <el-option label="女" value="女" />
        </el-select>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="profile.phone" />
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="profile.address" />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="profile.email" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="updateProfile">保存修改</el-button>
      </el-form-item>
    </el-form>
    <p style="color: green;">{{ message }}</p>
  </div>
</template>
