import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import AdminMain from '../views/AdminMain.vue'
import UserList from '../views/UserList.vue'
import UserMain from '../views/UserMain.vue'
import Profile from '../views/Profile.vue'
import component from 'element-plus/es/components/tree-select/src/tree-select-option.mjs'
import Project from '../views/Project.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  {
    path: '/adminMain',
    component: AdminMain,
    children: [
      { path: 'userlist', component: UserList },
      // 更多子页面也可以继续添加，比如:
      // { path: 'profile', component: Profile },
    ]
  },
  {
    path: '/userMain',
    component: UserMain,
    children: [
      { path: 'profile', component: Profile },
      { path: 'project', component:Project}
      // 更多子页面也可以继续添加，比如:
      // { path: 'profile', component: Profile },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
