// Generated from src/main/antlr4/Hba.g4 by ANTLR 4.9.2
package antlr4;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link HbaParser}.
 */
public interface HbaListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link HbaParser#hbaEntry}.
	 * @param ctx the parse tree
	 */
	void enterHbaEntry(HbaParser.HbaEntryContext ctx);
	/**
	 * Exit a parse tree produced by {@link HbaParser#hbaEntry}.
	 * @param ctx the parse tree
	 */
	void exitHbaEntry(HbaParser.HbaEntryContext ctx);
	/**
	 * Enter a parse tree produced by {@link HbaParser#connectionType}.
	 * @param ctx the parse tree
	 */
	void enterConnectionType(HbaParser.ConnectionTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link HbaParser#connectionType}.
	 * @param ctx the parse tree
	 */
	void exitConnectionType(HbaParser.ConnectionTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link HbaParser#databaseName}.
	 * @param ctx the parse tree
	 */
	void enterDatabaseName(HbaParser.DatabaseNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link HbaParser#databaseName}.
	 * @param ctx the parse tree
	 */
	void exitDatabaseName(HbaParser.DatabaseNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link HbaParser#userName}.
	 * @param ctx the parse tree
	 */
	void enterUserName(HbaParser.UserNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link HbaParser#userName}.
	 * @param ctx the parse tree
	 */
	void exitUserName(HbaParser.UserNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link HbaParser#address}.
	 * @param ctx the parse tree
	 */
	void enterAddress(HbaParser.AddressContext ctx);
	/**
	 * Exit a parse tree produced by {@link HbaParser#address}.
	 * @param ctx the parse tree
	 */
	void exitAddress(HbaParser.AddressContext ctx);
	/**
	 * Enter a parse tree produced by {@link HbaParser#authMethod}.
	 * @param ctx the parse tree
	 */
	void enterAuthMethod(HbaParser.AuthMethodContext ctx);
	/**
	 * Exit a parse tree produced by {@link HbaParser#authMethod}.
	 * @param ctx the parse tree
	 */
	void exitAuthMethod(HbaParser.AuthMethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link HbaParser#comment}.
	 * @param ctx the parse tree
	 */
	void enterComment(HbaParser.CommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HbaParser#comment}.
	 * @param ctx the parse tree
	 */
	void exitComment(HbaParser.CommentContext ctx);
}