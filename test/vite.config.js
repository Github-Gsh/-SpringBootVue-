import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// 引入按需加载插件
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig({
  plugins: [
    vue(),

    // 自动导入 API，如 ElMessage、ElMessageBox 等
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),

    // 自动注册组件，如 <el-button>
    Components({
      resolvers: [ElementPlusResolver()],
    }),
  ],
  // 添加代理配置
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  // 后端接口的地址
        changeOrigin: true,  // 确保请求头中 Host 是目标地址
        secure: false,  // 如果是 https 需要设置为 true
      },
    },
  },
})
