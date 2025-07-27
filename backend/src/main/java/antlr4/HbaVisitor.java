// Generated from src/main/antlr4/Hba.g4 by ANTLR 4.9.2
package antlr4;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link HbaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface HbaVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link HbaParser#hbaEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHbaEntry(HbaParser.HbaEntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link HbaParser#connectionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnectionType(HbaParser.ConnectionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link HbaParser#databaseName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabaseName(HbaParser.DatabaseNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link HbaParser#userName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserName(HbaParser.UserNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link HbaParser#address}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddress(HbaParser.AddressContext ctx);
	/**
	 * Visit a parse tree produced by {@link HbaParser#authMethod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAuthMethod(HbaParser.AuthMethodContext ctx);
	/**
	 * Visit a parse tree produced by {@link HbaParser#comment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment(HbaParser.CommentContext ctx);
}