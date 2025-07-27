<template>
  <div id="app">
    <el-container style="height: 100vh;">
      <el-header style="background-color: #409eff; color: white; display: flex; align-items: center;">
        <h1 style="margin: 0;">PgHBA 规则管理器</h1>
      </el-header>

      <el-container>
        <!-- 侧边栏导航 -->
        <el-aside width="200px" style="background-color: #f5f7fa;">
          <el-menu
            :default-active="$route.path"
            class="el-menu-vertical-demo"
            @select="handleMenuSelect"
            router
          >
            <el-menu-item index="/rules">
              <el-icon><List /></el-icon>
              <span>规则管理</span>
            </el-menu-item>
            <el-menu-item index="/config">
              <el-icon><Setting /></el-icon>
              <span>配置管理</span>
            </el-menu-item>
            <el-menu-item index="/audit"> <!-- 新增审计菜单 -->
              <el-icon><Document /></el-icon>
              <span>操作审计</span>
            </el-menu-item>
          </el-menu>
        </el-aside>

        <el-main>
          <router-view />
        </el-main>
      </el-container>

      <el-footer style="text-align: center; padding: 20px;">
        PgHBA 规则管理器 ©2023
      </el-footer>
    </el-container>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { List, Setting, Document } from '@element-plus/icons-vue' // 更新导入

const route = useRoute()
const router = useRouter()

const handleMenuSelect = (index) => {
  router.push(index)
}
</script>