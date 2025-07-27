import axios from 'axios'

const API_BASE_URL = '/api/config-logs'

class ConfigLogService {
  // 获取最新的配置操作日志
  getLatestConfigLogs(limit = 50) {
    return axios.get(`${API_BASE_URL}/latest`, { params: { limit } })
  }

  // 搜索配置操作日志
  searchConfigLogs(searchParams) {
    return axios.post(`${API_BASE_URL}/search`, searchParams)
  }

  // 获取配置统计信息
  getConfigStatistics() {
    return axios.get(`${API_BASE_URL}/statistics`)
  }
}

export default new ConfigLogService()