import axios from 'axios'

const API_BASE_URL = '/api/hba-rules'

class HbaRuleService {
  // 获取所有活跃规则
  getAllActiveRules() {
    return axios.get(`${API_BASE_URL}/active`)
  }

  // 获取所有规则
  getAllRules() {
    return axios.get(`${API_BASE_URL}/all`)
  }

  // 获取非活跃规则
  getInactiveRules() {
    return axios.get(`${API_BASE_URL}/inactive`)
  }

  // 根据ID获取规则
  getRuleById(id) {
    return axios.get(`${API_BASE_URL}/${id}`)
  }

  // 解析并保存规则
  parseAndSaveRule(ruleLine) {
    return axios.post(`${API_BASE_URL}/parse-and-save`, { ruleLine })
  }

  // 更新规则
  updateRule(id, rule) {
    return axios.put(`${API_BASE_URL}/${id}`, rule)
  }

  // 删除规则
  deleteRule(id) {
    return axios.delete(`${API_BASE_URL}/${id}`)
  }

  // 停用规则
  deactivateRule(id) {
    return axios.post(`${API_BASE_URL}/${id}/deactivate`)
  }

  // 激活规则
  activateRule(id) {
    return axios.post(`${API_BASE_URL}/${id}/activate`)
  }



  // 同步到pg_hba.conf
  syncToPgHbaConfig() {
    return axios.post(`${API_BASE_URL}/sync-config`)
  }


  // 重载PostGreSQL配置
  reloadPostGreSQL() {
    console.log("start reload postgresql ...")
    return axios.post(`${API_BASE_URL}/reload-postgresql`)
  }

  // 获取配置状态
  getConfigStatus() {
    return axios.get(`${API_BASE_URL}/config-status`)
  }

  // 检查写权限
  checkWritePermission() {
    return axios.get(`${API_BASE_URL}/check-permission`)
  }


// 读取pg_hba.conf文件内容
readPgHbaConfigContent() {
  return axios.get(`${API_BASE_URL}/config-content`)
}

// 更新pg_hba.conf文件内容
updatePgHbaConfigContent(content) {
  return axios.put(`${API_BASE_URL}/config-content`, { content })
}

// 检查 PostgreSQL 状态
checkPostGreSQLStatus() {
  return axios.get(`${API_BASE_URL}/postgres-status`)
}


}

export default new HbaRuleService()