-- 创建hba_rules表
CREATE TABLE IF NOT EXISTS hba_rules (
    id BIGSERIAL PRIMARY KEY,
    connection_type VARCHAR(20) NOT NULL,
    database_name VARCHAR(255) NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    auth_method VARCHAR(50) NOT NULL,
    comment TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_hba_rules_active ON hba_rules(active);
CREATE INDEX IF NOT EXISTS idx_hba_rules_connection_type ON hba_rules(connection_type);
CREATE INDEX IF NOT EXISTS idx_hba_rules_database_name ON hba_rules(database_name);
CREATE INDEX IF NOT EXISTS idx_hba_rules_user_name ON hba_rules(user_name);


-- 创建审计日志表
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGSERIAL PRIMARY KEY,
    rule_id BIGINT,
    action VARCHAR(50) NOT NULL,
    user_name VARCHAR(255),
    ip_address VARCHAR(45),
    user_agent TEXT,
    old_value TEXT,
    new_value TEXT,
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建审计日志索引
CREATE INDEX IF NOT EXISTS idx_audit_logs_rule_id ON audit_logs(rule_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_action ON audit_logs(action);
CREATE INDEX IF NOT EXISTS idx_audit_logs_user_name ON audit_logs(user_name);
CREATE INDEX IF NOT EXISTS idx_audit_logs_created_at ON audit_logs(created_at);



-- 创建配置管理操作审计表（新增）
CREATE TABLE IF NOT EXISTS config_operation_logs (
    id BIGSERIAL PRIMARY KEY,
    operation_type VARCHAR(50) NOT NULL, -- SYNC_CONFIG, RELOAD_CONFIG, CHECK_PERMISSION
    user_name VARCHAR(255),
    ip_address VARCHAR(45),
    user_agent TEXT,
    status VARCHAR(20) NOT NULL, -- SUCCESS, FAILED
    message TEXT,
    details TEXT, -- JSON格式的详细信息
    duration_ms BIGINT, -- 操作耗时（毫秒）
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_config_logs_operation_type ON config_operation_logs(operation_type);
CREATE INDEX IF NOT EXISTS idx_config_logs_user_name ON config_operation_logs(user_name);
CREATE INDEX IF NOT EXISTS idx_config_logs_status ON config_operation_logs(status);
CREATE INDEX IF NOT EXISTS idx_config_logs_created_at ON config_operation_logs(created_at);


-- 创建触发器函数（修正后的美元引用符号）
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS '
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
'
LANGUAGE plpgsql;

-- 创建触发器
DROP TRIGGER IF EXISTS update_hba_rules_updated_at ON hba_rules;
CREATE TRIGGER update_hba_rules_updated_at
    BEFORE UPDATE ON hba_rules
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();