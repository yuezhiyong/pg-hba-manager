<template>
  <div class="config-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>PostgreSQL配置管理</span>
        </div>
      </template>

      <!-- PostgreSQL状态 -->
      <el-card class="status-card" v-loading="checkingStatus">
        <template #header>
          <span>PostgreSQL状态</span>
        </template>

        <el-descriptions :column="2" border>
          <el-descriptions-item label="连接状态">
            <el-tag :type="postgresStatus.connected ? 'success' : 'danger'">
              {{ postgresStatus.connected ? '已连接' : '未连接' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="版本信息">{{ postgresStatus.version }}</el-descriptions-item>
        </el-descriptions>

        <div style="margin-top: 15px; text-align: center;">
          <el-button @click="checkPostGreSQLStatus" :loading="checkingStatus">
            <el-icon><Refresh /></el-icon>
            刷新状态
          </el-button>
        </div>
      </el-card>

      <!-- 配置文件状态 -->
      <el-card class="status-card" v-loading="loadingStatus">
        <template #header>
          <span>配置文件状态</span>
        </template>

        <el-descriptions :column="2" border>
          <el-descriptions-item label="配置文件路径">{{ configStatus.path }}</el-descriptions-item>
          <el-descriptions-item label="文件存在">
            <el-tag :type="configStatus.exists ? 'success' : 'danger'">
              {{ configStatus.exists ? '是' : '否' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="可写权限">
            <el-tag :type="configStatus.writable ? 'success' : 'danger'">
              {{ configStatus.writable ? '是' : '否' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="文件大小">{{ formatFileSize(configStatus.size) }}</el-descriptions-item>
          <el-descriptions-item label="最后修改">{{ configStatus.lastModified }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button type="primary" @click="syncConfig" :loading="syncing">
          <el-icon><Refresh /></el-icon>
          同步到pg_hba.conf
        </el-button>

        <el-button type="success" @click="reloadPostGreSQL" :loading="reloading">
          <el-icon><RefreshRight /></el-icon>
          重载PostgreSQL配置
        </el-button>

        <el-button @click="checkPermission">
          <el-icon><Check /></el-icon>
          检查权限
        </el-button>

        <!-- 新增的在线编辑和预览按钮 -->
        <el-button type="warning" @click="openConfigEditor">
          <el-icon><Edit /></el-icon>
          在线编辑
        </el-button>

        <el-button @click="openConfigPreview">
          <el-icon><View /></el-icon>
          在线预览
        </el-button>
      </div>

      <!-- 重载结果 -->
      <el-card class="result-card" v-if="reloadResult">
        <template #header>
          <span>重载结果</span>
        </template>

        <el-descriptions :column="1" border>
          <el-descriptions-item label="状态">
            <el-tag :type="reloadResult.success ? 'success' : 'danger'">
              {{ reloadResult.success ? '成功' : '失败' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="消息">{{ reloadResult.message }}</el-descriptions-item>
          <el-descriptions-item label="使用方法">{{ reloadResult.methodUsed }}</el-descriptions-item>
          <el-descriptions-item label="输出" v-if="reloadResult.output">
            <pre>{{ reloadResult.output }}</pre>
          </el-descriptions-item>
          <el-descriptions-item label="错误" v-if="reloadResult.error">
            <pre style="color: red;">{{ reloadResult.error }}</pre>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 操作日志 -->
      <el-card class="log-card" v-if="logs.length > 0">
        <template #header>
          <span>操作日志</span>
        </template>

        <el-timeline>
          <el-timeline-item
            v-for="(log, index) in logs"
            :key="index"
            :timestamp="log.timestamp"
            :type="log.type"
          >
            {{ log.message }}
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </el-card>

    <!-- 配置文件编辑对话框 -->
    <el-dialog
      v-model="configEditorDialogVisible"
      :title="isPreviewMode ? '配置文件预览' : '编辑配置文件'"
      width="80%"
      :fullscreen="editorFullscreen"
      @close="handleEditorDialogClose"
    >
      <template #header>
        <div class="dialog-header">
          <span>{{ isPreviewMode ? '配置文件预览' : '编辑配置文件' }}</span>
          <div class="dialog-header-actions">
            <el-button
              :icon="editorFullscreen ? 'FullScreen' : 'ScaleToOriginal'"
              @click="toggleEditorFullscreen"
              circle
              size="small"
            ></el-button>
          </div>
        </div>
      </template>

      <div class="editor-container">
        <!-- 使用新的代码编辑器组件，支持主题切换 -->
        <CodeEditor
          ref="codeEditorRef"
          v-model="configContent"
          :readonly="isPreviewMode"
          :theme="editorTheme"
          placeholder="请输入配置内容..."
          class="config-code-editor"
          @theme-change="handleThemeChange"
        />

        <div class="editor-info" v-if="configFileInfo">
          <el-tag type="info">文件路径: {{ configFileInfo.path }}</el-tag>
          <el-tag type="info" style="margin-left: 10px;">文件大小: {{ formatFileSize(configFileInfo.size || 0) }}</el-tag>
          <el-tag type="info" style="margin-left: 10px;">总行数: {{ lineCount }} 行</el-tag>
          <el-tag type="info" style="margin-left: 10px;">主题: {{ currentThemeName }}</el-tag>
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="configEditorDialogVisible = false">取消</el-button>
          <el-button
            v-if="!isPreviewMode"
            type="primary"
            @click="saveConfigContent"
            :loading="savingConfig"
          >
            保存配置
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Refresh,
  RefreshRight,
  Check,
  Edit,
  View,
  FullScreen,
  ScaleToOriginal
} from '@element-plus/icons-vue'
import hbaRuleService from '@/services/hbaRuleService'
import CodeEditor from '@/components/CodeEditor.vue' // 导入新的代码编辑器组件

const configStatus = ref({
  path: '',
  exists: false,
  writable: false,
  lastModified: '',
  size: 0
})

const postgresStatus = ref({
  connected: false,
  version: 'Unknown'
})

const syncing = ref(false)
const reloading = ref(false)
const loadingStatus = ref(false)
const checkingStatus = ref(false)
const reloadResult = ref(null)
const logs = ref([])
const savingConfig = ref(false)

// 配置编辑器相关
const configEditorDialogVisible = ref(false)
const configContent = ref('')
const configFileInfo = ref(null)
const isPreviewMode = ref(false)
const editorFullscreen = ref(false)
const codeEditorRef = ref(null)
const editorTheme = ref('code-editor') // 默认使用代码编辑器主题

// 主题映射
const themeNames = {
  'light': '浅色主题',
  'dark': '深色主题',
  'code-editor': '代码编辑器主题',
  'text-editor': '文本编辑器主题'
}

const currentThemeName = computed(() => {
  return themeNames[editorTheme.value] || '未知主题'
})

// 添加行数计算
const lineCount = computed(() => {
  if (!configContent.value) return 0
  return configContent.value.split('\n').length
})

const loadConfigStatus = async () => {
  try {
    loadingStatus.value = true
    const response = await hbaRuleService.getConfigStatus()
    configStatus.value = response.data.data
  } catch (error) {
    ElMessage.error('获取配置状态失败: ' + error.message)
  } finally {
    loadingStatus.value = false
  }
}

const checkPostGreSQLStatus = async () => {
  try {
    checkingStatus.value = true
    const response = await hbaRuleService.checkPostGreSQLStatus()
    postgresStatus.value = {
      connected: response.data.data.connected,
      version: response.data.data.version
    }
    addLog('info', 'PostgreSQL状态检查完成')
  } catch (error) {
    ElMessage.error('检查PostgreSQL状态失败: ' + error.message)
    addLog('danger', '检查PostgreSQL状态失败: ' + error.message)
  } finally {
    checkingStatus.value = false
  }
}

const syncConfig = async () => {
  try {
    syncing.value = true
    await hbaRuleService.syncToPgHbaConfig()
    ElMessage.success('配置同步成功')
    addLog('success', '配置同步成功')
    await loadConfigStatus()
  } catch (error) {
    ElMessage.error('配置同步失败: ' + error.message)
    addLog('danger', '配置同步失败: ' + error.message)
  } finally {
    syncing.value = false
  }
}

const reloadPostGreSQL = async () => {
  try {
    reloading.value = true
    const response = await hbaRuleService.reloadPostGreSQL()
    reloadResult.value = response.data.data
    if (response.data.success) {
      ElMessage.success(response.data.message)
      addLog('success', response.data.message)
    } else {
      ElMessage.error(response.data.message)
      addLog('danger', response.data.message)
    }
  } catch (error) {
    ElMessage.error('PostgreSQL配置重载失败: ' + error.message)
    addLog('danger', 'PostgreSQL配置重载失败: ' + error.message)
    reloadResult.value = {
      success: false,
      message: error.message,
      methodUsed: 'Unknown',
      error: error.message
    }
  } finally {
    reloading.value = false
  }
}

const checkPermission = async () => {
  try {
    const response = await hbaRuleService.checkWritePermission()
    const hasPermission = response.data.data
    if (hasPermission) {
      ElMessage.success('写权限检查通过')
      addLog('success', '写权限检查通过')
    } else {
      ElMessage.error('写权限检查失败')
      addLog('danger', '写权限检查失败')
    }
    await loadConfigStatus()
  } catch (error) {
    ElMessage.error('权限检查失败: ' + error.message)
    addLog('danger', '权限检查失败: ' + error.message)
  }
}

// 新增的在线编辑和预览功能
const openConfigEditor = async () => {
  try {
    const response = await hbaRuleService.readPgHbaConfigContent()
    configContent.value = response.data.data.content
    configFileInfo.value = {
      path: response.data.data.path,
      size: response.data.data.content.length
    }
    isPreviewMode.value = false
    editorTheme.value = 'code-editor' // 编辑模式默认使用代码编辑器主题
    configEditorDialogVisible.value = true
  } catch (error) {
    ElMessage.error('读取配置文件失败: ' + error.message)
  }
}

const openConfigPreview = async () => {
  try {
    const response = await hbaRuleService.readPgHbaConfigContent()
    configContent.value = response.data.data.content
    configFileInfo.value = {
      path: response.data.data.path,
      size: response.data.data.content.length
    }
    isPreviewMode.value = true
    editorTheme.value = 'text-editor' // 预览模式默认使用文本编辑器主题
    configEditorDialogVisible.value = true
  } catch (error) {
    ElMessage.error('读取配置文件失败: ' + error.message)
  }
}

const saveConfigContent = async () => {
  if (!configContent.value.trim()) {
    ElMessage.warning('配置内容不能为空')
    return
  }

  try {
    await ElMessageBox.confirm(
      '确定要保存配置文件吗？请确保配置内容正确，错误的配置可能导致PostgreSQL服务异常。',
      '确认保存',
      {
        confirmButtonText: '确定保存',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )

    savingConfig.value = true
    const response = await hbaRuleService.updatePgHbaConfigContent(configContent.value)

    if (response.data.success) {
      ElMessage.success('配置文件保存成功')
      configEditorDialogVisible.value = false
      addLog('success', '配置文件保存成功')
      // 重新加载配置状态
      await loadConfigStatus()
    } else {
      ElMessage.error('配置文件保存失败: ' + response.data.message)
      addLog('danger', '配置文件保存失败: ' + response.data.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('保存配置文件失败: ' + error.message)
      addLog('danger', '保存配置文件失败: ' + error.message)
    }
  } finally {
    savingConfig.value = false
  }
}

const toggleEditorFullscreen = () => {
  editorFullscreen.value = !editorFullscreen.value
}

const handleEditorDialogClose = () => {
  configContent.value = ''
  configFileInfo.value = null
  isPreviewMode.value = false
  editorFullscreen.value = false
  editorTheme.value = 'code-editor'
}

// 处理主题切换
const handleThemeChange = (theme) => {
  editorTheme.value = theme
}

const addLog = (type, message) => {
  logs.value.unshift({
    type: type,
    message: message,
    timestamp: new Date().toLocaleString()
  })
}

const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

onMounted(() => {
  loadConfigStatus()
  checkPostGreSQLStatus()
})
</script>

<style scoped>
.config-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.status-card {
  margin-bottom: 20px;
}

.action-buttons {
  margin-bottom: 20px;
  text-align: center;
}

.action-buttons .el-button {
  margin: 0 10px;
}

.result-card {
  margin-bottom: 20px;
}

.log-card {
  margin-top: 20px;
}

pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  margin: 0;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

/* 编辑器对话框样式 */
.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.dialog-header-actions {
  flex-shrink: 0;
}

.editor-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.config-code-editor {
  flex: 1;
  min-height: 400px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.editor-info {
  margin-top: 10px;
  padding: 10px;
  background-color: #f0f2f5;
  border-radius: 4px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.dialog-footer .el-button {
  margin-left: 10px;
}
</style>