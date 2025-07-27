import { createRouter, createWebHistory } from 'vue-router'
import RuleList from '@/views/RuleList.vue'
import RuleForm from '@/views/RuleForm.vue'
import ConfigManagement from '@/views/ConfigManagement.vue' // 新增
import AuditLog from '@/views/AuditLog.vue' // 新增

const routes = [
  {
    path: '/',
    redirect: '/rules',
  },
  {
    path: '/rules',
    name: 'RuleList',
    component: RuleList
  },
  {
    path: '/rules/new',
    name: 'NewRule',
    component: RuleForm
  },
  {
    path: '/rules/:id/edit',
    name: 'EditRule',
    component: RuleForm,
    props: true
  },
  {
      path: '/config', // 新增配置管理路由
      name: 'ConfigManagement',
      component: ConfigManagement
  },
  {
      path: '/audit', // 新增审计日志路由
      name: 'AuditLog',
      component: AuditLog
    }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router