<template>
  <div>
    <h2>个人资料修改</h2>
    <el-form :model="profile" label-width="80px">
      <!-- 头像上传 -->
      <el-form-item label="头像">
        <el-upload
          class="avatar-uploader"
          action="http://localhost:8080/api/profile/upload-avatar"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeUpload"
          with-credentials
        >
          <img v-if="avatarUrl" :src="avatarUrl" class="avatar" />
          <el-icon v-else><Plus /></el-icon>
        </el-upload>
      </el-form-item>

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

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const profile = ref({})
const message = ref('')
const avatarUrl = ref('')

onMounted(async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/profile', { withCredentials: true })
    profile.value = res.data
    avatarUrl.value = res.data.headshot || ''
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
})

const updateProfile = async () => {
  try {
    await axios.post('http://localhost:8080/api/profile/update', profile.value, { withCredentials: true })
    message.value = '修改成功'
  } catch (err) {
    message.value = '修改失败'
    console.error('修改失败:', err)
  }
}

const handleAvatarSuccess = (response) => {
  avatarUrl.value = response.url
  profile.value.headshot = response.url
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isImage) ElMessage.error('只能上传图片格式！')
  if (!isLt2M) ElMessage.error('图片大小不能超过 2MB！')
  return isImage && isLt2M
}
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 100px;
  height: 100px;
  display: block;
  border-radius: 50%;
}
</style>
