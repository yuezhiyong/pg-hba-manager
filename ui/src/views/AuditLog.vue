<template>
  <div class="audit-log">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>操作审计日志</span>
        </div>
      </template>

      <!-- Tab页面 -->
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <!-- 规则操作审计Tab -->
        <el-tab-pane label="规则操作审计" name="ruleAudit">
          <!-- 规则审计内容 -->
          <div v-if="activeTab === 'ruleAudit'">
            <!-- 搜索条件 -->
            <el-form :model="ruleSearchForm" :inline="true" class="search-form">
              <el-form-item label="关键词">
                <el-input v-model="ruleSearchForm.keyword" placeholder="用户名或备注" clearable />
              </el-form-item>

              <el-form-item label="操作类型">
                <el-select v-model="ruleSearchForm.action" placeholder="请选择操作类型" clearable>
                  <el-option label="全部" value=""></el-option>
                  <el-option label="创建" value="CREATE"></el-option>
                  <el-option label="更新" value="UPDATE"></el-option>
                  <el-option label="删除" value="DELETE"></el-option>
                  <el-option label="激活" value="ACTIVATE"></el-option>
                  <el-option label="停用" value="DEACTIVATE"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="时间范围">
                <el-date-picker
                  v-model="ruleDateRange"
                  type="datetimerange"
                  range-separator="至"
                  start-placeholder="开始时间"
                  end-placeholder="结束时间"
                  format="YYYY-MM-DD HH:mm:ss"
                  value-format="YYYY-MM-DD HH:mm:ss"
                />
              </el-form-item>

              <el-form-item>
                <el-button type="primary" @click="searchRuleLogs" :loading="loading">
                  <el-icon><Search /></el-icon>
                  搜索
                </el-button>
                <el-button @click="resetRuleSearch">重置</el-button>
              </el-form-item>
            </el-form>

            <!-- 规则审计日志表格 -->
            <el-table
              :data="ruleAuditLogs"
              style="width: 100%"
              v-loading="loading"
              stripe
              :header-cell-style="{ textAlign: 'center', backgroundColor: '#f5f7fa' }"
              :cell-style="{ textAlign: 'center' }"
              height="calc(100vh - 400px)"
            >
              <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
              <el-table-column prop="ruleId" label="规则ID" width="100" align="center">
                <template #default="scope">
                  <el-link type="primary" @click="viewRuleDetail(scope.row.ruleId)">
                    {{ scope.row.ruleId }}
                  </el-link>
                </template>
              </el-table-column>
              <el-table-column prop="action" label="操作类型" width="100" align="center">
                <template #default="scope">
                  <el-tag :type="getRuleActionTagType(scope.row.action)">
                    {{ formatRuleAction(scope.row.action) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="userName" label="操作用户" width="120" align="center"></el-table-column>
              <el-table-column prop="ipAddress" label="IP地址" width="150" align="center"></el-table-column>
              <el-table-column prop="remarks" label="备注" min-width="150" align="center"></el-table-column>
              <el-table-column prop="createdAt" label="操作时间" width="180" align="center">
                <template #default="scope">
                  {{ formatDate(scope.row.createdAt) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120" fixed="right" align="center">
                <template #default="scope">
                  <el-button size="small" @click="viewRuleLogDetail(scope.row)">详情</el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="pagination">
              <el-pagination
                v-model:current-page="rulePagination.currentPage"
                v-model:page-size="rulePagination.pageSize"
                :page-sizes="[10, 20, 50, 100]"
                :total="rulePagination.total"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleRuleSizeChange"
                @current-change="handleRuleCurrentChange"
              />
            </div>
          </div>
        </el-tab-pane>

        <!-- 配置操作审计Tab -->
        <el-tab-pane label="配置操作审计" name="configAudit">
          <!-- 配置审计内容 -->
          <div v-if="activeTab === 'configAudit'">
            <!-- 搜索条件 -->
            <el-form :model="configSearchForm" :inline="true" class="search-form">
              <el-form-item label="关键词">
                <el-input v-model="configSearchForm.keyword" placeholder="用户名或消息" clearable />
              </el-form-item>

              <el-form-item label="操作类型">
                <el-select v-model="configSearchForm.operationType" placeholder="请选择操作类型" clearable>
                  <el-option label="全部" value=""></el-option>
                  <el-option label="同步配置" value="SYNC_CONFIG"></el-option>
                  <el-option label="重载配置" value="RELOAD_CONFIG"></el-option>
                  <el-option label="检查权限" value="CHECK_PERMISSION"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="状态">
                <el-select v-model="configSearchForm.status" placeholder="请选择状态" clearable>
                  <el-option label="全部" value=""></el-option>
                  <el-option label="成功" value="SUCCESS"></el-option>
                  <el-option label="失败" value="FAILED"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="时间范围">
                <el-date-picker
                  v-model="configDateRange"
                  type="datetimerange"
                  range-separator="至"
                  start-placeholder="开始时间"
                  end-placeholder="结束时间"
                  format="YYYY-MM-DD HH:mm:ss"
                  value-format="YYYY-MM-DD HH:mm:ss"
                />
              </el-form-item>

              <el-form-item>
                <el-button type="primary" @click="searchConfigLogs" :loading="loading">
                  <el-icon><Search /></el-icon>
                  搜索
                </el-button>
                <el-button @click="resetConfigSearch">重置</el-button>
              </el-form-item>
            </el-form>

            <!-- 配置审计日志表格 -->
            <el-table
              :data="configAuditLogs"
              style="width: 100%"
              v-loading="loading"
              stripe
              :header-cell-style="{ textAlign: 'center', backgroundColor: '#f5f7fa' }"
              :cell-style="{ textAlign: 'center' }"
              height="calc(100vh - 400px)"
            >
              <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
              <el-table-column prop="operationType" label="操作类型" width="120" align="center">
                <template #default="scope">
                  <el-tag :type="getConfigOperationTagType(scope.row.operationType)">
                    {{ formatConfigOperation(scope.row.operationType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="userName" label="操作用户" width="120" align="center"></el-table-column>
              <el-table-column prop="ipAddress" label="IP地址" width="150" align="center"></el-table-column>
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template #default="scope">
                  <el-tag :type="getStatusTagType(scope.row.status)">
                    {{ formatStatus(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="message" label="消息" min-width="200" align="center"></el-table-column>
              <el-table-column prop="durationMs" label="耗时(ms)" width="100" align="center"></el-table-column>
              <el-table-column prop="createdAt" label="操作时间" width="180" align="center">
                <template #default="scope">
                  {{ formatDate(scope.row.createdAt) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120" fixed="right" align="center">
                <template #default="scope">
                  <el-button size="small" @click="viewConfigLogDetail(scope.row)">详情</el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="pagination">
              <el-pagination
                v-model:current-page="configPagination.currentPage"
                v-model:page-size="configPagination.pageSize"
                :page-sizes="[10, 20, 50, 100]"
                :total="configPagination.total"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleConfigSizeChange"
                @current-change="handleConfigCurrentChange"
              />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>

      <!-- 规则日志详情对话框 -->
      <el-dialog v-model="ruleDetailDialogVisible" title="规则审计日志详情" width="60%">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="ID">{{ currentRuleLog.id }}</el-descriptions-item>
          <el-descriptions-item label="规则ID">{{ currentRuleLog.ruleId }}</el-descriptions-item>
          <el-descriptions-item label="操作类型">
            <el-tag :type="getRuleActionTagType(currentRuleLog.action)">
              {{ formatRuleAction(currentRuleLog.action) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="操作用户">{{ currentRuleLog.userName }}</el-descriptions-item>
          <el-descriptions-item label="IP地址">{{ currentRuleLog.ipAddress }}</el-descriptions-item>
          <el-descriptions-item label="用户代理">{{ currentRuleLog.userAgent }}</el-descriptions-item>
          <el-descriptions-item label="操作时间">{{ formatDate(currentRuleLog.createdAt) }}</el-descriptions-item>
          <el-descriptions-item label="备注">{{ currentRuleLog.remarks }}</el-descriptions-item>
          <el-descriptions-item label="旧值">
            <pre class="json-content">{{ formatJson(currentRuleLog.oldValue) }}</pre>
          </el-descriptions-item>
          <el-descriptions-item label="新值">
            <pre class="json-content">{{ formatJson(currentRuleLog.newValue) }}</pre>
          </el-descriptions-item>
        </el-descriptions>
      </el-dialog>

      <!-- 配置日志详情对话框 -->
      <el-dialog v-model="configDetailDialogVisible" title="配置审计日志详情" width="60%">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="ID">{{ currentConfigLog.id }}</el-descriptions-item>
          <el-descriptions-item label="操作类型">
            <el-tag :type="getConfigOperationTagType(currentConfigLog.operationType)">
              {{ formatConfigOperation(currentConfigLog.operationType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="操作用户">{{ currentConfigLog.userName }}</el-descriptions-item>
          <el-descriptions-item label="IP地址">{{ currentConfigLog.ipAddress }}</el-descriptions-item>
          <el-descriptions-item label="用户代理">{{ currentConfigLog.userAgent }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusTagType(currentConfigLog.status)">
              {{ formatStatus(currentConfigLog.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="消息">{{ currentConfigLog.message }}</el-descriptions-item>
          <el-descriptions-item label="耗时">{{ currentConfigLog.durationMs }} ms</el-descriptions-item>
          <el-descriptions-item label="操作时间">{{ formatDate(currentConfigLog.createdAt) }}</el-descriptions-item>
          <el-descriptions-item label="详细信息">
            <pre class="json-content">{{ formatJson(currentConfigLog.details) }}</pre>
          </el-descriptions-item>
        </el-descriptions>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import auditLogService from '@/services/auditLogService'
import configLogService from '@/services/configLogService'

// Tab控制
const activeTab = ref('ruleAudit')

// 规则审计相关
const ruleAuditLogs = ref([])
const ruleDetailDialogVisible = ref(false)
const currentRuleLog = ref({})

const ruleSearchForm = ref({
  keyword: '',
  action: '',
  page: 1,
  size: 20
})

const ruleDateRange = ref([])
const rulePagination = ref({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 配置审计相关
const configAuditLogs = ref([])
const configDetailDialogVisible = ref(false)
const currentConfigLog = ref({})

const configSearchForm = ref({
  keyword: '',
  operationType: '',
  status: '',
  page: 1,
  size: 20
})

const configDateRange = ref([])
const configPagination = ref({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 通用状态
const loading = ref(false)

// Tab切换处理
const handleTabChange = (tabName) => {
  activeTab.value = tabName
  if (tabName === 'ruleAudit') {
    loadRuleAuditLogs()
  } else if (tabName === 'configAudit') {
    loadConfigAuditLogs()
  }
}

// 规则审计相关方法
const loadRuleAuditLogs = async () => {
  try {
    loading.value = true
    const response = await auditLogService.searchAuditLogs({
      ...ruleSearchForm.value,
      startTime: ruleDateRange.value[0],
      endTime: ruleDateRange.value[1]
    })

    const result = response.data.data
    ruleAuditLogs.value = result.logs || []
    rulePagination.value.total = result.total || 0
    rulePagination.value.currentPage = result.page || 1
    rulePagination.value.pageSize = result.size || 20
  } catch (error) {
    ElMessage.error('加载规则审计日志失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

const searchRuleLogs = () => {
  ruleSearchForm.value.page = 1
  loadRuleAuditLogs()
}

const resetRuleSearch = () => {
  ruleSearchForm.value.keyword = ''
  ruleSearchForm.value.action = ''
  ruleDateRange.value = []
  ruleSearchForm.value.page = 1
  loadRuleAuditLogs()
}

const viewRuleLogDetail = (log) => {
  currentRuleLog.value = log
  ruleDetailDialogVisible.value = true
}

const viewRuleDetail = (ruleId) => {
  ElMessage.info(`查看规则ID: ${ruleId} 的详情`)
}

const handleRuleSizeChange = (val) => {
  ruleSearchForm.value.size = val
  ruleSearchForm.value.page = 1
  loadRuleAuditLogs()
}

const handleRuleCurrentChange = (val) => {
  ruleSearchForm.value.page = val
  loadRuleAuditLogs()
}

// 配置审计相关方法
const loadConfigAuditLogs = async () => {
  try {
    loading.value = true
    const response = await configLogService.searchConfigLogs({
      ...configSearchForm.value,
      startTime: configDateRange.value[0],
      endTime: configDateRange.value[1]
    })

    const result = response.data.data
    configAuditLogs.value = result.logs || []
    configPagination.value.total = result.total || 0
    configPagination.value.currentPage = result.page || 1
    configPagination.value.pageSize = result.size || 20
  } catch (error) {
    ElMessage.error('加载配置审计日志失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

const searchConfigLogs = () => {
  configSearchForm.value.page = 1
  loadConfigAuditLogs()
}

const resetConfigSearch = () => {
  configSearchForm.value.keyword = ''
  configSearchForm.value.operationType = ''
  configSearchForm.value.status = ''
  configDateRange.value = []
  configSearchForm.value.page = 1
  loadConfigAuditLogs()
}

const viewConfigLogDetail = (log) => {
  currentConfigLog.value = log
  configDetailDialogVisible.value = true
}

const handleConfigSizeChange = (val) => {
  configSearchForm.value.size = val
  configSearchForm.value.page = 1
  loadConfigAuditLogs()
}

const handleConfigCurrentChange = (val) => {
  configSearchForm.value.page = val
  loadConfigAuditLogs()
}

// 格式化方法
const getRuleActionTagType = (action) => {
  const types = {
    'CREATE': 'success',
    'UPDATE': 'primary',
    'DELETE': 'danger',
    'ACTIVATE': 'warning',
    'DEACTIVATE': 'info'
  }
  return types[action] || ''
}

const formatRuleAction = (action) => {
  const actions = {
    'CREATE': '创建',
    'UPDATE': '更新',
    'DELETE': '删除',
    'ACTIVATE': '激活',
    'DEACTIVATE': '停用'
  }
  return actions[action] || action
}

const getConfigOperationTagType = (operationType) => {
  const types = {
    'SYNC_CONFIG': 'primary',
    'RELOAD_CONFIG': 'warning',
    'CHECK_PERMISSION': 'info'
  }
  return types[operationType] || ''
}

const formatConfigOperation = (operationType) => {
  const operations = {
    'SYNC_CONFIG': '同步配置',
    'RELOAD_CONFIG': '重载配置',
    'CHECK_PERMISSION': '检查权限'
  }
  return operations[operationType] || operationType
}

const getStatusTagType = (status) => {
  const types = {
    'SUCCESS': 'success',
    'FAILED': 'danger'
  }
  return types[status] || ''
}

const formatStatus = (status) => {
  const statuses = {
    'SUCCESS': '成功',
    'FAILED': '失败'
  }
  return statuses[status] || status
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString('zh-CN')
}

const formatJson = (jsonString) => {
  if (!jsonString) return '无'
  try {
    const obj = JSON.parse(jsonString)
    return JSON.stringify(obj, null, 2)
  } catch (e) {
    return jsonString
  }
}

onMounted(() => {
  // 默认加载规则审计日志
  loadRuleAuditLogs()
})
</script>

<style scoped>
.audit-log {
  padding: 20px;
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.json-content {
  white-space: pre-wrap;
  word-wrap: break-word;
  margin: 0;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
  max-height: 200px;
  overflow-y: auto;
}
</style>