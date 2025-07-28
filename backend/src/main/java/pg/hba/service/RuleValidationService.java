package pg.hba.service;

import org.springframework.stereotype.Service;
import pg.hba.entity.HbaRule;
import pg.hba.vo.ValidationResult;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class RuleValidationService {

    private static final Set<String> VALID_CONNECTION_TYPES = new HashSet<>(
            Arrays.asList("local", "host", "hostssl", "hostnossl")
    );

    private static final Set<String> VALID_AUTH_METHODS = new HashSet<>(
            Arrays.asList("trust", "reject", "md5", "password", "gss", "sspi",
                    "ident", "peer", "ldap", "radius", "cert", "scram-sha-256")
    );

    private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );

    private static final Pattern CIDR_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/(3[0-2]|[12]?[0-9])$"
    );

    public ValidationResult validateRule(HbaRule rule) {
        ValidationResult result = new ValidationResult();

        // 检查连接类型
        if (!VALID_CONNECTION_TYPES.contains(rule.getConnectionType())) {
            result.addError("Invalid connection type: " + rule.getConnectionType());
        }

        // 检查认证方法
        if (!VALID_AUTH_METHODS.contains(rule.getAuthMethod())) {
            result.addError("Invalid auth method: " + rule.getAuthMethod());
        }

        // 对于非local连接类型，必须有地址
        if (!"local".equals(rule.getConnectionType())) {
            if (rule.getAddress() == null || rule.getAddress().isEmpty()) {
                result.addError("Address is required for non-local connection types");
            } else if (!isValidIpAddress(rule.getAddress())) {
                result.addError("Invalid IP address or CIDR format: " + rule.getAddress());
            }
        } else {
            // 对于local连接类型，地址可以为空或设置为null
            // 这里我们允许local类型有地址，但通常应该为空
            if (rule.getAddress() != null && !rule.getAddress().isEmpty() &&
                    !"all".equals(rule.getAddress())) {
                result.addError("Local connection type should not have specific address, use 'all' or leave empty");
            }
        }

        // 检查必填字段
        if (rule.getDatabaseName() == null || rule.getDatabaseName().isEmpty()) {
            result.addError("Database name is required");
        }

        if (rule.getUserName() == null || rule.getUserName().isEmpty()) {
            result.addError("User name is required");
        }

        result.setValid(result.getErrors().isEmpty());
        return result;
    }

    private boolean isValidIpAddress(String address) {
        return "all".equals(address) ||
                IP_ADDRESS_PATTERN.matcher(address).matches() ||
                CIDR_PATTERN.matcher(address).matches() ||
                address == null ||
                address.isEmpty();
    }


}
