<template>
  <div class="rule-form">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>{{ isEditMode ? '编辑规则' : '新增规则' }}</span>
          <el-button @click="goBack">返回</el-button>
        </div>
      </template>

      <el-form
        ref="ruleFormRef"
        :model="ruleForm"
        :rules="rules"
        label-width="120px"
        label-position="left"
      >
        <!-- 规则文本输入 -->
        <el-form-item label="规则文本" prop="ruleLine">
          <el-input
            v-model="ruleForm.ruleLine"
            type="textarea"
            :rows="3"
            placeholder="例如: host all all 192.168.1.0/24 md5"
            @input="parseRuleLine"
          />
          <div class="rule-help">
            <el-alert
              title="规则格式说明"
              type="info"
              description="格式: [连接类型] [数据库] [用户] [地址] [认证方法] #[注释]"
              show-icon
              :closable="false"
            />
          </div>
        </el-form-item>

        <el-divider />

        <!-- 分步表单 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="连接类型" prop="connectionType">
              <el-select v-model="ruleForm.connectionType" placeholder="请选择连接类型">
                <el-option label="local" value="local"></el-option>
                <el-option label="host" value="host"></el-option>
                <el-option label="hostssl" value="hostssl"></el-option>
                <el-option label="hostnossl" value="hostnossl"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="数据库" prop="databaseName">
              <el-input v-model="ruleForm.databaseName" placeholder="数据库名称或'all'"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户" prop="userName">
              <el-input v-model="ruleForm.userName" placeholder="用户名或'all'"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="地址" prop="address">
              <el-input
                v-model="ruleForm.address"
                placeholder="IP地址、CIDR或'all'"
                :disabled="ruleForm.connectionType === 'local'"
              />
              <div v-if="ruleForm.connectionType === 'local'" class="address-disabled-tip">
                <el-alert type="info" :closable="false" title="local连接类型不需要地址" />
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="认证方法" prop="authMethod">
              <el-select v-model="ruleForm.authMethod" placeholder="请选择认证方法">
                <el-option-group label="基本认证">
                  <el-option label="trust" value="trust"></el-option>
                  <el-option label="reject" value="reject"></el-option>
                  <el-option label="md5" value="md5"></el-option>
                  <el-option label="password" value="password"></el-option>
                  <el-option label="scram-sha-256" value="scram-sha-256"></el-option>
                </el-option-group>
                <el-option-group label="高级认证">
                  <el-option label="gss" value="gss"></el-option>
                  <el-option label="sspi" value="sspi"></el-option>
                  <el-option label="ident" value="ident"></el-option>
                  <el-option label="peer" value="peer"></el-option>
                  <el-option label="ldap" value="ldap"></el-option>
                  <el-option label="radius" value="radius"></el-option>
                  <el-option label="cert" value="cert"></el-option>
                </el-option-group>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="状态">
              <el-switch
                v-model="ruleForm.active"
                active-text="活跃"
                inactive-text="非活跃"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="注释">
          <el-input
            v-model="ruleForm.comment"
            type="textarea"
            :rows="2"
            placeholder="规则注释（可选）"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="submitLoading">
            {{ isEditMode ? '更新规则' : '保存规则' }}
          </el-button>
          <el-button @click="resetForm">重置</el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import hbaRuleService from '@/services/hbaRuleService'

const route = useRoute()
const router = useRouter()

// 表单引用
const ruleFormRef = ref()

// 表单数据
const ruleForm = reactive({
  id: null,
  ruleLine: '',
  connectionType: 'host',
  databaseName: 'all',
  userName: 'all',
  address: '',
  authMethod: 'md5',
  comment: '',
  active: true
})

// 表单验证规则
const rules = computed(() => ({
  ruleLine: [
    { required: false, message: '请输入规则文本', trigger: 'blur' }
  ],
  connectionType: [
    { required: true, message: '请选择连接类型', trigger: 'change' }
  ],
  databaseName: [
    { required: true, message: '请输入数据库名称', trigger: 'blur' }
  ],
  userName: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  address: [
    {
      required: ruleForm.connectionType !== 'local',
      message: '请输入地址',
      trigger: 'blur'
    }
  ],
  authMethod: [
    { required: true, message: '请选择认证方法', trigger: 'change' }
  ]
}))

// 监听连接类型变化，动态调整地址字段
watch(() => ruleForm.connectionType, (newType) => {
  if (newType === 'local') {
    // 对于local类型，清空地址字段
    ruleForm.address = ''
  }
})

// 状态
const submitLoading = ref(false)
const isEditMode = computed(() => !!route.params.id)

// 方法
const parseRuleLine = () => {
  if (!ruleForm.ruleLine.trim()) return

  try {
    // 简单的规则解析
    const parts = ruleForm.ruleLine.trim().split(/\s+/)
    if (parts.length >= 5) {
      ruleForm.connectionType = parts[0]
      ruleForm.databaseName = parts[1]
      ruleForm.userName = parts[2]
      ruleForm.address = parts[3]
      ruleForm.authMethod = parts[4]

      // 提取注释
      const commentMatch = ruleForm.ruleLine.match(/#(.*)$/)
      if (commentMatch) {
        ruleForm.comment = commentMatch[1].trim()
      }
    }
  } catch (error) {
    console.warn('规则解析失败:', error)
  }
}

const loadRule = async (id) => {
  try {
    const response = await hbaRuleService.getRuleById(id)
    const rule = response.data

    Object.assign(ruleForm, {
      id: rule.id,
      connectionType: rule.connectionType,
      databaseName: rule.databaseName,
      userName: rule.userName,
      address: rule.address,
      authMethod: rule.authMethod,
      comment: rule.comment,
      active: rule.active,
      ruleLine: formatRuleLine(rule)
    })
  } catch (error) {
    ElMessage.error('加载规则失败: ' + error.message)
  }
}

const formatRuleLine = (rule) => {
  let line = `${rule.connectionType} ${rule.databaseName} ${rule.userName} ${rule.address || 'all'} ${rule.authMethod}`
  if (rule.comment) {
    line += ` # ${rule.comment}`
  }
  return line
}

const submitForm = async () => {
  if (!ruleFormRef.value) return

  await ruleFormRef.value.validate(async (valid) => {
    if (!valid) return

    try {
      submitLoading.value = true

      if (isEditMode.value) {
        // 更新规则
        const updateData = {
          connectionType: ruleForm.connectionType,
          databaseName: ruleForm.databaseName,
          userName: ruleForm.userName,
          address: ruleForm.connectionType === 'local' ? null : ruleForm.address,
          authMethod: ruleForm.authMethod,
          comment: ruleForm.comment,
          active: ruleForm.active
        }

        await hbaRuleService.updateRule(ruleForm.id, updateData)
        ElMessage.success('规则更新成功')
      } else {
        // 新增规则
        let ruleLine = ruleForm.ruleLine.trim()
        if (!ruleLine) {
          // 如果没有直接输入规则行，则构造规则行
          const address = ruleForm.connectionType === 'local' ? 'all' : (ruleForm.address || 'all')
          ruleLine = `${ruleForm.connectionType} ${ruleForm.databaseName} ${ruleForm.userName} ${address} ${ruleForm.authMethod}`
          if (ruleForm.comment) {
            ruleLine += ` # ${ruleForm.comment}`
          }
        }

        const response = await hbaRuleService.parseAndSaveRule(ruleLine)
        if (response.data.success) {
          ElMessage.success('规则保存成功')
        } else {
          ElMessage.error('规则保存失败: ' + response.data.message)
          return
        }
      }

      goBack()
    } catch (error) {
      ElMessage.error('操作失败: ' + (error.response?.data?.message || error.message))
    } finally {
      submitLoading.value = false
    }
  })
}

const resetForm = () => {
  Object.assign(ruleForm, {
    id: null,
    ruleLine: '',
    connectionType: 'host',
    databaseName: 'all',
    userName: 'all',
    address: '',
    authMethod: 'md5',
    comment: '',
    active: true
  })
}

const goBack = () => {
  router.push('/rules')
}

// 生命周期
onMounted(() => {
  if (isEditMode.value) {
    loadRule(route.params.id)
  }
})
</script>

<style scoped>
.rule-form {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.rule-help {
  margin-top: 10px;
}

.address-disabled-tip {
  margin-top: 5px;
}
</style>