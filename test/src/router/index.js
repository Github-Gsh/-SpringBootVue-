import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import AdminMain from '../views/AdminMain.vue'
import UserList from '../views/UserList.vue'

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
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
