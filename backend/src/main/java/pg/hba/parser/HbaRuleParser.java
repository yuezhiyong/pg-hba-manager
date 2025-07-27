package pg.hba.parser;


import antlr4.HbaLexer;
import antlr4.HbaParser;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.springframework.stereotype.Service;
import pg.hba.entity.HbaRule;
import pg.hba.vo.ParseResult;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HbaRuleParser {

    public ParseResult parseRule(String ruleLine) {
        try {
            // 创建词法分析器
            HbaLexer lexer = new HbaLexer(CharStreams.fromString(ruleLine));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            // 创建语法分析器
            HbaParser parser = new HbaParser(tokens);
            ParseTree tree = parser.hbaEntry();
            // 访问者模式解析
            HbaRuleVisitor visitor = new HbaRuleVisitor();
            HbaRule rule = visitor.visit(tree);
            log.debug("current rule:{}", rule);
            return new ParseResult(true, rule, null);
        } catch (Exception e) {
            return new ParseResult(false, null, e.getMessage());
        }
    }

    public List<ParseResult> parseMultipleRules(List<String> ruleLines) {
        List<ParseResult> results = new ArrayList<>();
        for (String line : ruleLines) {
            if (line != null && !line.trim().isEmpty() && !line.trim().startsWith("#")) {
                results.add(parseRule(line.trim()));
            }
        }
        return results;
    }


}
