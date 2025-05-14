<template>
  <div class="p-6">
    <h2 class="text-2xl font-bold mb-4">我的项目（用户：{{ currentUser?.username || '未登录' }}）</h2>

    <form @submit.prevent="saveProject" class="mb-6 space-y-4">
      <input v-model="form.title" placeholder="项目标题" class="border p-2 w-full" />
      <textarea v-model="form.description" placeholder="项目描述" class="border p-2 w-full"></textarea>
      <input type="file" @change="handleFileUpload" />
      <button class="bg-blue-500 text-white px-4 py-2 rounded">保存</button>
    </form>

    <ul class="space-y-4">
      <li v-for="project in projects" :key="project.pid" class="border p-4 rounded">
        <h3 class="text-lg font-semibold">{{ project.title }}</h3>
        <p>{{ project.description }}</p>
        <p>{{ project.status }}</p>
        <div class="mt-2 flex space-x-2">
          <button @click="editProject(project)" class="text-blue-600">编辑</button>
          <button @click="deleteProject(project.pid)" class="text-red-600">删除</button>
          <a :href="project.filePath" target="_blank" class="text-green-600" v-if="project.filePath">查看文件</a>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const currentUser = ref(null)
const projects = ref([])
const form = ref({ pid: null, title: '', description: '', file: null })

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true
})

const fetchCurrentUser = async () => {
  try {
    const res = await axiosInstance.get('/me')
    currentUser.value = res.data
  } catch (err) {
    alert('请先登录')
  }
}

const fetchProjects = async () => {
  try {
    const res = await axiosInstance.get('/projects')
    projects.value = res.data
  } catch (err) {
    console.error('加载项目失败', err)
  }
}

const saveProject = async () => {
  const formData = new FormData()
  formData.append('title', form.value.title)
  formData.append('description', form.value.description)
  if (form.value.file) formData.append('file', form.value.file)

  try {
    if (form.value.pid) {
      await axiosInstance.put(`/projects/${form.value.pid}`, formData)
    } else {
      await axiosInstance.post('/projects', formData)
    }

    form.value = { pid: null, title: '', description: '', file: null }
    fetchProjects()
  } catch (err) {
    alert('保存失败')
  }
}

const handleFileUpload = (event) => {
  form.value.file = event.target.files[0]
}

const editProject = (project) => {
  form.value = { ...project, file: null }
}

const deleteProject = async (pid) => {
  if (confirm('确定删除此项目？')) {
    await axiosInstance.delete(`/projects/${pid}`)
    fetchProjects()
  }
}

onMounted(async () => {
  await fetchCurrentUser()
  await fetchProjects()
})
</script>
