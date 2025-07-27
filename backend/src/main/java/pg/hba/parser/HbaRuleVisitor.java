package pg.hba.parser;

import antlr4.HbaBaseVisitor;
import antlr4.HbaParser;
import pg.hba.entity.HbaRule;

public class HbaRuleVisitor extends HbaBaseVisitor<HbaRule> {

    private final HbaRule currentRule = new HbaRule();

    @Override
    public HbaRule visitConnectionType(HbaParser.ConnectionTypeContext ctx) {
        currentRule.setConnectionType(ctx.getText().toLowerCase());
        return currentRule;
    }

    @Override
    public HbaRule visitDatabaseName(HbaParser.DatabaseNameContext ctx) {
        String text = ctx.getText();
        if (text.startsWith("\"") && text.endsWith("\"")) {
            text = text.substring(1, text.length() - 1);
        }
        currentRule.setDatabaseName(text);
        return currentRule;
    }

    @Override
    public HbaRule visitUserName(HbaParser.UserNameContext ctx) {
        String text = ctx.getText();
        if (text.startsWith("\"") && text.endsWith("\"")) {
            text = text.substring(1, text.length() - 1);
        }
        currentRule.setUserName(text);
        return currentRule;
    }

    @Override
    public HbaRule visitAddress(HbaParser.AddressContext ctx) {
        currentRule.setAddress(ctx.getText());
        return currentRule;
    }

    @Override
    public HbaRule visitAuthMethod(HbaParser.AuthMethodContext ctx) {
        currentRule.setAuthMethod(ctx.getText());
        return currentRule;
    }

    @Override
    public HbaRule visitHbaEntry(HbaParser.HbaEntryContext ctx) {
        visitChildren(ctx);
        return currentRule;
    }
}
