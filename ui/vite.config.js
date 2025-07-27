import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 13000,  // UI端口改为13000
    host: '0.0.0.0',
    proxy: {
      '/api': {
        target: 'http://localhost:12000',  // 代理到后端12000端口
        changeOrigin: true
      }
    }
  },
  build: {
    outDir: '../backend/src/main/resources/static',  // 构建到后端static目录
    emptyOutDir: true
  },
  minify: false,
  resolve: {
    alias: {
      '@': '/src'
    }
  }
})