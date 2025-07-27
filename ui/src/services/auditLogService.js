import axios from 'axios'

const API_BASE_URL = '/api/audit-logs'

class AuditLogService {
  // 获取最新的审计日志
  getLatestAuditLogs(limit = 50) {
    return axios.get(`${API_BASE_URL}/latest`, { params: { limit } })
  }

  // 根据规则ID获取审计日志
  getAuditLogsByRuleId(ruleId) {
    return axios.get(`${API_BASE_URL}/rule/${ruleId}`)
  }

  // 搜索审计日志
  searchAuditLogs(searchParams) {
    return axios.post(`${API_BASE_URL}/search`, searchParams)
  }

  // 获取统计信息
  getStatistics() {
    return axios.get(`${API_BASE_URL}/statistics`)
  }
}

export default new AuditLogService()