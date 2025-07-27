// Generated from src/main/antlr4/Hba.g4 by ANTLR 4.9.2
package antlr4;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class HbaParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LOCAL=1, HOST=2, HOSTSSL=3, HOSTNOSSL=4, ALL=5, SAMEUSER=6, SAMEROLE=7, 
		REPLICATION=8, TRUST=9, REJECT=10, MD5=11, PASSWORD=12, GSS=13, SSPI=14, 
		IDENT=15, PEER=16, LDAP=17, RADIUS=18, CERT=19, SCRAM_SHA_256=20, PLUS_ROLE=21, 
		AT_FILE=22, IDENTIFIER=23, QUOTED_STRING=24, IP_ADDRESS=25, IP_RANGE=26, 
		HOSTNAME=27, COMMENT=28, WS=29;
	public static final int
		RULE_hbaEntry = 0, RULE_connectionType = 1, RULE_databaseName = 2, RULE_userName = 3, 
		RULE_address = 4, RULE_authMethod = 5, RULE_comment = 6;
	private static String[] makeRuleNames() {
		return new String[] {
			"hbaEntry", "connectionType", "databaseName", "userName", "address", 
			"authMethod", "comment"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'local'", "'host'", "'hostssl'", "'hostnossl'", "'all'", "'sameuser'", 
			"'samerole'", "'replication'", "'trust'", "'reject'", "'md5'", "'password'", 
			"'gss'", "'sspi'", "'ident'", "'peer'", "'ldap'", "'radius'", "'cert'", 
			"'scram-sha-256'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LOCAL", "HOST", "HOSTSSL", "HOSTNOSSL", "ALL", "SAMEUSER", "SAMEROLE", 
			"REPLICATION", "TRUST", "REJECT", "MD5", "PASSWORD", "GSS", "SSPI", "IDENT", 
			"PEER", "LDAP", "RADIUS", "CERT", "SCRAM_SHA_256", "PLUS_ROLE", "AT_FILE", 
			"IDENTIFIER", "QUOTED_STRING", "IP_ADDRESS", "IP_RANGE", "HOSTNAME", 
			"COMMENT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Hba.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public HbaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class HbaEntryContext extends ParserRuleContext {
		public ConnectionTypeContext connectionType() {
			return getRuleContext(ConnectionTypeContext.class,0);
		}
		public DatabaseNameContext databaseName() {
			return getRuleContext(DatabaseNameContext.class,0);
		}
		public UserNameContext userName() {
			return getRuleContext(UserNameContext.class,0);
		}
		public AddressContext address() {
			return getRuleContext(AddressContext.class,0);
		}
		public AuthMethodContext authMethod() {
			return getRuleContext(AuthMethodContext.class,0);
		}
		public TerminalNode EOF() { return getToken(HbaParser.EOF, 0); }
		public CommentContext comment() {
			return getRuleContext(CommentContext.class,0);
		}
		public HbaEntryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hbaEntry; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HbaListener ) ((HbaListener)listener).enterHbaEntry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HbaListener ) ((HbaListener)listener).exitHbaEntry(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HbaVisitor ) return ((HbaVisitor<? extends T>)visitor).visitHbaEntry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HbaEntryContext hbaEntry() throws RecognitionException {
		HbaEntryContext _localctx = new HbaEntryContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_hbaEntry);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(14);
			connectionType();
			setState(15);
			databaseName();
			setState(16);
			userName();
			setState(17);
			address();
			setState(18);
			authMethod();
			setState(20);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMENT) {
				{
				setState(19);
				comment();
				}
			}

			setState(22);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConnectionTypeContext extends ParserRuleContext {
		public TerminalNode LOCAL() { return getToken(HbaParser.LOCAL, 0); }
		public TerminalNode HOST() { return getToken(HbaParser.HOST, 0); }
		public TerminalNode HOSTSSL() { return getToken(HbaParser.HOSTSSL, 0); }
		public TerminalNode HOSTNOSSL() { return getToken(HbaParser.HOSTNOSSL, 0); }
		public ConnectionTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_connectionType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HbaListener ) ((HbaListener)listener).enterConnectionType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HbaListener ) ((HbaListener)listener).exitConnectionType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HbaVisitor ) return ((HbaVisitor<? extends T>)visitor).visitConnectionType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConnectionTypeContext connectionType() throws RecognitionException {
		ConnectionTypeContext _localctx = new ConnectionTypeContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_connectionType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LOCAL) | (1L << HOST) | (1L << HOSTSSL) | (1L << HOSTNOSSL))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DatabaseNameContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(HbaParser.IDENTIFIER, 0); }
		public TerminalNode QUOTED_STRING() { return getToken(HbaParser.QUOTED_STRING, 0); }
		public TerminalNode ALL() { return getToken(HbaParser.ALL, 0); }
		public TerminalNode SAMEUSER() { return getToken(HbaParser.SAMEUSER, 0); }
		public TerminalNode SAMEROLE() { return getToken(HbaParser.SAMEROLE, 0); }
		public TerminalNode REPLICATION() { return getToken(HbaParser.REPLICATION, 0); }
		public TerminalNode PLUS_ROLE() { return getToken(HbaParser.PLUS_ROLE, 0); }
		public TerminalNode AT_FILE() { return getToken(HbaParser.AT_FILE, 0); }
		public DatabaseNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_databaseName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HbaListener ) ((HbaListener)listener).enterDatabaseName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HbaListener ) ((HbaListener)listener).exitDatabaseName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HbaVisitor ) return ((HbaVisitor<? extends T>)visitor).visitDatabaseName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DatabaseNameContext databaseName() throws RecognitionException {
		DatabaseNameContext _localctx = new DatabaseNameContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_databaseName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL) | (1L << SAMEUSER) | (1L << SAMEROLE) | (1L << REPLICATION) | (1L << PLUS_ROLE) | (1L << AT_FILE) | (1L << IDENTIFIER) | (1L << QUOTED_STRING))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UserNameContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(HbaParser.IDENTIFIER, 0); }
		public TerminalNode QUOTED_STRING() { return getToken(HbaParser.QUOTED_STRING, 0); }
		public TerminalNode ALL() { return getToken(HbaParser.ALL, 0); }
		public TerminalNode PLUS_ROLE() { return getToken(HbaParser.PLUS_ROLE, 0); }
		public TerminalNode AT_FILE() { return getToken(HbaParser.AT_FILE, 0); }
		public UserNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_userName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HbaListener ) ((HbaListener)listener).enterUserName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HbaListener ) ((HbaListener)listener).exitUserName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HbaVisitor ) return ((HbaVisitor<? extends T>)visitor).visitUserName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UserNameContext userName() throws RecognitionException {
		UserNameContext _localctx = new UserNameContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_userName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(28);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL) | (1L << PLUS_ROLE) | (1L << AT_FILE) | (1L << IDENTIFIER) | (1L << QUOTED_STRING))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AddressContext extends ParserRuleContext {
		public TerminalNode IP_ADDRESS() { return getToken(HbaParser.IP_ADDRESS, 0); }
		public TerminalNode IP_RANGE() { return getToken(HbaParser.IP_RANGE, 0); }
		public TerminalNode HOSTNAME() { return getToken(HbaParser.HOSTNAME, 0); }
		public TerminalNode ALL() { return getToken(HbaParser.ALL, 0); }
		public AddressContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_address; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HbaListener ) ((HbaListener)listener).enterAddress(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HbaListener ) ((HbaListener)listener).exitAddress(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HbaVisitor ) return ((HbaVisitor<? extends T>)visitor).visitAddress(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AddressContext address() throws RecognitionException {
		AddressContext _localctx = new AddressContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_address);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(30);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALL) | (1L << IP_ADDRESS) | (1L << IP_RANGE) | (1L << HOSTNAME))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AuthMethodContext extends ParserRuleContext {
		public TerminalNode TRUST() { return getToken(HbaParser.TRUST, 0); }
		public TerminalNode REJECT() { return getToken(HbaParser.REJECT, 0); }
		public TerminalNode MD5() { return getToken(HbaParser.MD5, 0); }
		public TerminalNode PASSWORD() { return getToken(HbaParser.PASSWORD, 0); }
		public TerminalNode GSS() { return getToken(HbaParser.GSS, 0); }
		public TerminalNode SSPI() { return getToken(HbaParser.SSPI, 0); }
		public TerminalNode IDENT() { return getToken(HbaParser.IDENT, 0); }
		public TerminalNode PEER() { return getToken(HbaParser.PEER, 0); }
		public TerminalNode LDAP() { return getToken(HbaParser.LDAP, 0); }
		public TerminalNode RADIUS() { return getToken(HbaParser.RADIUS, 0); }
		public TerminalNode CERT() { return getToken(HbaParser.CERT, 0); }
		public TerminalNode SCRAM_SHA_256() { return getToken(HbaParser.SCRAM_SHA_256, 0); }
		public AuthMethodContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_authMethod; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HbaListener ) ((HbaListener)listener).enterAuthMethod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HbaListener ) ((HbaListener)listener).exitAuthMethod(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HbaVisitor ) return ((HbaVisitor<? extends T>)visitor).visitAuthMethod(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AuthMethodContext authMethod() throws RecognitionException {
		AuthMethodContext _localctx = new AuthMethodContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_authMethod);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(32);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUST) | (1L << REJECT) | (1L << MD5) | (1L << PASSWORD) | (1L << GSS) | (1L << SSPI) | (1L << IDENT) | (1L << PEER) | (1L << LDAP) | (1L << RADIUS) | (1L << CERT) | (1L << SCRAM_SHA_256))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CommentContext extends ParserRuleContext {
		public TerminalNode COMMENT() { return getToken(HbaParser.COMMENT, 0); }
		public CommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HbaListener ) ((HbaListener)listener).enterComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HbaListener ) ((HbaListener)listener).exitComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HbaVisitor ) return ((HbaVisitor<? extends T>)visitor).visitComment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentContext comment() throws RecognitionException {
		CommentContext _localctx = new CommentContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_comment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(34);
			match(COMMENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\37\'\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2\3\2\3\2\3\2\3\2\3\2\5"+
		"\2\27\n\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b"+
		"\2\2\t\2\4\6\b\n\f\16\2\7\3\2\3\6\4\2\7\n\27\32\4\2\7\7\27\32\4\2\7\7"+
		"\33\35\3\2\13\26\2 \2\20\3\2\2\2\4\32\3\2\2\2\6\34\3\2\2\2\b\36\3\2\2"+
		"\2\n \3\2\2\2\f\"\3\2\2\2\16$\3\2\2\2\20\21\5\4\3\2\21\22\5\6\4\2\22\23"+
		"\5\b\5\2\23\24\5\n\6\2\24\26\5\f\7\2\25\27\5\16\b\2\26\25\3\2\2\2\26\27"+
		"\3\2\2\2\27\30\3\2\2\2\30\31\7\2\2\3\31\3\3\2\2\2\32\33\t\2\2\2\33\5\3"+
		"\2\2\2\34\35\t\3\2\2\35\7\3\2\2\2\36\37\t\4\2\2\37\t\3\2\2\2 !\t\5\2\2"+
		"!\13\3\2\2\2\"#\t\6\2\2#\r\3\2\2\2$%\7\36\2\2%\17\3\2\2\2\3\26";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}