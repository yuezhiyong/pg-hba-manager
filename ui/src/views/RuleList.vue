<template>
  <div class="rule-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>规则列表</span>
          <el-button type="primary" @click="handleAddRule">
            <el-icon><Plus /></el-icon>
            新增规则
          </el-button>
        </div>
      </template>

      <!-- 筛选和搜索 -->
      <div class="filter-section">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-select v-model="filter.status" placeholder="状态" clearable @change="loadRules">
              <el-option label="活跃" value="active"></el-option>
              <el-option label="非活跃" value="inactive"></el-option>
              <el-option label="全部" value="all"></el-option>
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="filter.connectionType" placeholder="连接类型" clearable @change="loadRules">
              <el-option label="local" value="local"></el-option>
              <el-option label="host" value="host"></el-option>
              <el-option label="hostssl" value="hostssl"></el-option>
              <el-option label="hostnossl" value="hostnossl"></el-option>
            </el-select>
          </el-col>
          <el-col :span="12">
            <el-input
              v-model="filter.search"
              placeholder="搜索规则..."
              clearable
              @clear="loadRules"
              @keyup.enter="loadRules"
            >
              <template #append>
                <el-button @click="loadRules">
                  <el-icon><Search /></el-icon>
                </el-button>
              </template>
            </el-input>
          </el-col>
        </el-row>
      </div>

      <!-- 规则表格 -->
      <el-table
        :data="filteredRules"
        style="width: 100%"
        v-loading="loading"
        stripe
        :header-cell-style="{ textAlign: 'center', backgroundColor: '#f5f7fa' }"
        :cell-style="{ textAlign: 'center' }"
        height="calc(100vh - 300px)"
      >
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="connectionType" label="连接类型" width="120" align="center">
          <template #default="scope">
            <el-tag :type="getConnectionTypeTagType(scope.row.connectionType)">
              {{ scope.row.connectionType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="databaseName" label="数据库" width="150" align="center"></el-table-column>
        <el-table-column prop="userName" label="用户" width="150" align="center"></el-table-column>
        <el-table-column prop="address" label="地址" width="180" align="center">
          <template #default="scope">
            <span v-if="scope.row.address">{{ scope.row.address }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="authMethod" label="认证方法" width="150" align="center">
          <template #default="scope">
            <el-tag :type="getAuthMethodTagType(scope.row.authMethod)">
              {{ scope.row.authMethod }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="active" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.active ? 'success' : 'danger'">
              {{ scope.row.active ? '活跃' : '非活跃' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" min-width="180" align="center">
          <template #default="scope">
            {{ formatDate(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right" align="center">
          <template #default="scope">
            <el-button
              size="small"
              type="primary"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              size="small"
              :type="scope.row.active ? 'warning' : 'success'"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.active ? '停用' : '激活' }}
            </el-button>
            <el-popconfirm
              title="确定要删除这个规则吗？"
              @confirm="handleDelete(scope.row)"
            >
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="totalRules"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import hbaRuleService from '@/services/hbaRuleService'

const router = useRouter()

// 数据
const rules = ref([])
const loading = ref(false)
const filter = ref({
  status: 'active',
  connectionType: '',
  search: ''
})
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 计算属性
const filteredRules = computed(() => {
  let filtered = [...rules.value]

  // 根据搜索内容过滤
  if (filter.value.search) {
    const search = filter.value.search.toLowerCase()
    filtered = filtered.filter(rule =>
      rule.databaseName.toLowerCase().includes(search) ||
      rule.userName.toLowerCase().includes(search) ||
      (rule.address && rule.address.toLowerCase().includes(search)) ||
      rule.authMethod.toLowerCase().includes(search)
    )
  }

  // 分页
  const start = (pagination.value.currentPage - 1) * pagination.value.pageSize
  const end = start + pagination.value.pageSize
  return filtered.slice(start, end)
})

const totalRules = computed(() => rules.value.length)

// 方法
const loadRules = async () => {
  try {
    loading.value = true

    let response
    switch (filter.value.status) {
      case 'active':
        response = await hbaRuleService.getAllActiveRules()
        break
      case 'inactive':
        response = await hbaRuleService.getInactiveRules()
        break
      case 'all':
      default:
        response = await hbaRuleService.getAllRules()
        break
    }

    rules.value = response.data
    pagination.value.total = response.data.length
  } catch (error) {
    ElMessage.error('加载规则失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

const handleAddRule = () => {
  router.push('/rules/new')
}

const handleEdit = (rule) => {
  router.push(`/rules/${rule.id}/edit`)
}

const handleToggleStatus = async (rule) => {
  try {
    if (rule.active) {
      await hbaRuleService.deactivateRule(rule.id)
      ElMessage.success('规则已停用')
    } else {
      await hbaRuleService.activateRule(rule.id)
      ElMessage.success('规则已激活')
    }
    await loadRules()
  } catch (error) {
    ElMessage.error('操作失败: ' + error.message)
  }
}

const handleDelete = async (rule) => {
  try {
    await hbaRuleService.deleteRule(rule.id)
    ElMessage.success('规则已删除')
    await loadRules()
  } catch (error) {
    ElMessage.error('删除失败: ' + error.message)
  }
}

const handleSizeChange = (val) => {
  pagination.value.pageSize = val
  pagination.value.currentPage = 1
}

const handleCurrentChange = (val) => {
  pagination.value.currentPage = val
}

const getConnectionTypeTagType = (type) => {
  const types = {
    'local': '',
    'host': 'success',
    'hostssl': 'warning',
    'hostnossl': 'danger'
  }
  return types[type] || ''
}

const getAuthMethodTagType = (method) => {
  const types = {
    'trust': 'success',
    'reject': 'danger',
    'md5': 'primary',
    'password': 'warning',
    'scram-sha-256': 'info'
  }
  return types[method] || ''
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString('zh-CN')
}

// 生命周期
onMounted(() => {
  loadRules()
})
</script>

<style scoped>
.rule-list {
  padding: 20px;
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-section {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

/* 确保表格内容居中对齐 */
:deep(.el-table th) {
  text-align: center !important;
}

:deep(.el-table td) {
  text-align: center !important;
}

/* 调整表格高度以适应内容 */
:deep(.el-table__body-wrapper) {
  min-height: 300px;
}
</style>