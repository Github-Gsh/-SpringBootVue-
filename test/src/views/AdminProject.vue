<template>
  <div class="p-6">
    <h2 class="text-2xl font-bold mb-4">管理员项目审批</h2>

    <ul class="space-y-4">
      <li v-for="project in projects" :key="project.pid" class="border p-4 rounded">
        <h3 class="text-lg font-semibold">{{ project.title }}</h3>
        <p>描述：{{ project.description }}</p>
        <p>用户：{{ project.name }}</p>
        <p>状态：<span :class="project.status === '已审批' ? 'text-green-600' : 'text-yellow-600'">
          {{ project.status }}
        </span></p>
        <div class="mt-2 flex space-x-2">
          <button
            v-if="project.status !== '已审批'"
            @click="approveProject(project.pid)"
            class="bg-green-500 text-white px-4 py-1 rounded"
          >
            审批
          </button>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const projects = ref([])

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true
})

const fetchProjects = async () => {
  try {
    const res = await axiosInstance.get('/admin/projects')
    projects.value = res.data
  } catch (err) {
    alert('无权限或获取项目失败')
  }
}

const approveProject = async (pid) => {
  try {
    await axiosInstance.put(`/admin/projects/${pid}/approve`)
    fetchProjects()
  } catch (err) {
    alert('审批失败')
  }
}

onMounted(() => {
  fetchProjects()
})
</script>
