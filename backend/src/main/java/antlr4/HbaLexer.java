// Generated from src/main/antlr4/Hba.g4 by ANTLR 4.9.2
package antlr4;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class HbaLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"LOCAL", "HOST", "HOSTSSL", "HOSTNOSSL", "ALL", "SAMEUSER", "SAMEROLE", 
			"REPLICATION", "TRUST", "REJECT", "MD5", "PASSWORD", "GSS", "SSPI", "IDENT", 
			"PEER", "LDAP", "RADIUS", "CERT", "SCRAM_SHA_256", "PLUS_ROLE", "AT_FILE", 
			"IDENTIFIER", "QUOTED_STRING", "IP_ADDRESS", "IP_RANGE", "HOSTNAME", 
			"COMMENT", "WS"
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


	public HbaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Hba.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\37\u0118\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r"+
		"\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27"+
		"\3\27\3\30\3\30\7\30\u00d2\n\30\f\30\16\30\u00d5\13\30\3\31\3\31\3\31"+
		"\3\31\7\31\u00db\n\31\f\31\16\31\u00de\13\31\3\31\3\31\3\32\6\32\u00e3"+
		"\n\32\r\32\16\32\u00e4\3\32\3\32\6\32\u00e9\n\32\r\32\16\32\u00ea\3\32"+
		"\3\32\6\32\u00ef\n\32\r\32\16\32\u00f0\3\32\3\32\6\32\u00f5\n\32\r\32"+
		"\16\32\u00f6\3\33\3\33\3\33\6\33\u00fc\n\33\r\33\16\33\u00fd\3\34\3\34"+
		"\7\34\u0102\n\34\f\34\16\34\u0105\13\34\3\34\3\34\3\35\3\35\7\35\u010b"+
		"\n\35\f\35\16\35\u010e\13\35\3\35\3\35\3\36\6\36\u0113\n\36\r\36\16\36"+
		"\u0114\3\36\3\36\2\2\37\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f"+
		"\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63"+
		"\33\65\34\67\359\36;\37\3\2\n\5\2C\\aac|\7\2//\62;C\\aac|\6\2\f\f\17\17"+
		"$$^^\3\2\62;\5\2\62;C\\c|\6\2/\60\62;C\\c|\4\2\f\f\17\17\5\2\13\f\17\17"+
		"\"\"\2\u0122\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2"+
		"\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2"+
		"\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2"+
		"\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2"+
		"\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3"+
		"\2\2\2\2;\3\2\2\2\3=\3\2\2\2\5C\3\2\2\2\7H\3\2\2\2\tP\3\2\2\2\13Z\3\2"+
		"\2\2\r^\3\2\2\2\17g\3\2\2\2\21p\3\2\2\2\23|\3\2\2\2\25\u0082\3\2\2\2\27"+
		"\u0089\3\2\2\2\31\u008d\3\2\2\2\33\u0096\3\2\2\2\35\u009a\3\2\2\2\37\u009f"+
		"\3\2\2\2!\u00a5\3\2\2\2#\u00aa\3\2\2\2%\u00af\3\2\2\2\'\u00b6\3\2\2\2"+
		")\u00bb\3\2\2\2+\u00c9\3\2\2\2-\u00cc\3\2\2\2/\u00cf\3\2\2\2\61\u00d6"+
		"\3\2\2\2\63\u00e2\3\2\2\2\65\u00f8\3\2\2\2\67\u00ff\3\2\2\29\u0108\3\2"+
		"\2\2;\u0112\3\2\2\2=>\7n\2\2>?\7q\2\2?@\7e\2\2@A\7c\2\2AB\7n\2\2B\4\3"+
		"\2\2\2CD\7j\2\2DE\7q\2\2EF\7u\2\2FG\7v\2\2G\6\3\2\2\2HI\7j\2\2IJ\7q\2"+
		"\2JK\7u\2\2KL\7v\2\2LM\7u\2\2MN\7u\2\2NO\7n\2\2O\b\3\2\2\2PQ\7j\2\2QR"+
		"\7q\2\2RS\7u\2\2ST\7v\2\2TU\7p\2\2UV\7q\2\2VW\7u\2\2WX\7u\2\2XY\7n\2\2"+
		"Y\n\3\2\2\2Z[\7c\2\2[\\\7n\2\2\\]\7n\2\2]\f\3\2\2\2^_\7u\2\2_`\7c\2\2"+
		"`a\7o\2\2ab\7g\2\2bc\7w\2\2cd\7u\2\2de\7g\2\2ef\7t\2\2f\16\3\2\2\2gh\7"+
		"u\2\2hi\7c\2\2ij\7o\2\2jk\7g\2\2kl\7t\2\2lm\7q\2\2mn\7n\2\2no\7g\2\2o"+
		"\20\3\2\2\2pq\7t\2\2qr\7g\2\2rs\7r\2\2st\7n\2\2tu\7k\2\2uv\7e\2\2vw\7"+
		"c\2\2wx\7v\2\2xy\7k\2\2yz\7q\2\2z{\7p\2\2{\22\3\2\2\2|}\7v\2\2}~\7t\2"+
		"\2~\177\7w\2\2\177\u0080\7u\2\2\u0080\u0081\7v\2\2\u0081\24\3\2\2\2\u0082"+
		"\u0083\7t\2\2\u0083\u0084\7g\2\2\u0084\u0085\7l\2\2\u0085\u0086\7g\2\2"+
		"\u0086\u0087\7e\2\2\u0087\u0088\7v\2\2\u0088\26\3\2\2\2\u0089\u008a\7"+
		"o\2\2\u008a\u008b\7f\2\2\u008b\u008c\7\67\2\2\u008c\30\3\2\2\2\u008d\u008e"+
		"\7r\2\2\u008e\u008f\7c\2\2\u008f\u0090\7u\2\2\u0090\u0091\7u\2\2\u0091"+
		"\u0092\7y\2\2\u0092\u0093\7q\2\2\u0093\u0094\7t\2\2\u0094\u0095\7f\2\2"+
		"\u0095\32\3\2\2\2\u0096\u0097\7i\2\2\u0097\u0098\7u\2\2\u0098\u0099\7"+
		"u\2\2\u0099\34\3\2\2\2\u009a\u009b\7u\2\2\u009b\u009c\7u\2\2\u009c\u009d"+
		"\7r\2\2\u009d\u009e\7k\2\2\u009e\36\3\2\2\2\u009f\u00a0\7k\2\2\u00a0\u00a1"+
		"\7f\2\2\u00a1\u00a2\7g\2\2\u00a2\u00a3\7p\2\2\u00a3\u00a4\7v\2\2\u00a4"+
		" \3\2\2\2\u00a5\u00a6\7r\2\2\u00a6\u00a7\7g\2\2\u00a7\u00a8\7g\2\2\u00a8"+
		"\u00a9\7t\2\2\u00a9\"\3\2\2\2\u00aa\u00ab\7n\2\2\u00ab\u00ac\7f\2\2\u00ac"+
		"\u00ad\7c\2\2\u00ad\u00ae\7r\2\2\u00ae$\3\2\2\2\u00af\u00b0\7t\2\2\u00b0"+
		"\u00b1\7c\2\2\u00b1\u00b2\7f\2\2\u00b2\u00b3\7k\2\2\u00b3\u00b4\7w\2\2"+
		"\u00b4\u00b5\7u\2\2\u00b5&\3\2\2\2\u00b6\u00b7\7e\2\2\u00b7\u00b8\7g\2"+
		"\2\u00b8\u00b9\7t\2\2\u00b9\u00ba\7v\2\2\u00ba(\3\2\2\2\u00bb\u00bc\7"+
		"u\2\2\u00bc\u00bd\7e\2\2\u00bd\u00be\7t\2\2\u00be\u00bf\7c\2\2\u00bf\u00c0"+
		"\7o\2\2\u00c0\u00c1\7/\2\2\u00c1\u00c2\7u\2\2\u00c2\u00c3\7j\2\2\u00c3"+
		"\u00c4\7c\2\2\u00c4\u00c5\7/\2\2\u00c5\u00c6\7\64\2\2\u00c6\u00c7\7\67"+
		"\2\2\u00c7\u00c8\78\2\2\u00c8*\3\2\2\2\u00c9\u00ca\7-\2\2\u00ca\u00cb"+
		"\5/\30\2\u00cb,\3\2\2\2\u00cc\u00cd\7B\2\2\u00cd\u00ce\5/\30\2\u00ce."+
		"\3\2\2\2\u00cf\u00d3\t\2\2\2\u00d0\u00d2\t\3\2\2\u00d1\u00d0\3\2\2\2\u00d2"+
		"\u00d5\3\2\2\2\u00d3\u00d1\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\60\3\2\2"+
		"\2\u00d5\u00d3\3\2\2\2\u00d6\u00dc\7$\2\2\u00d7\u00db\n\4\2\2\u00d8\u00d9"+
		"\7^\2\2\u00d9\u00db\13\2\2\2\u00da\u00d7\3\2\2\2\u00da\u00d8\3\2\2\2\u00db"+
		"\u00de\3\2\2\2\u00dc\u00da\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd\u00df\3\2"+
		"\2\2\u00de\u00dc\3\2\2\2\u00df\u00e0\7$\2\2\u00e0\62\3\2\2\2\u00e1\u00e3"+
		"\t\5\2\2\u00e2\u00e1\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e4"+
		"\u00e5\3\2\2\2\u00e5\u00e6\3\2\2\2\u00e6\u00e8\7\60\2\2\u00e7\u00e9\t"+
		"\5\2\2\u00e8\u00e7\3\2\2\2\u00e9\u00ea\3\2\2\2\u00ea\u00e8\3\2\2\2\u00ea"+
		"\u00eb\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec\u00ee\7\60\2\2\u00ed\u00ef\t"+
		"\5\2\2\u00ee\u00ed\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0\u00ee\3\2\2\2\u00f0"+
		"\u00f1\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2\u00f4\7\60\2\2\u00f3\u00f5\t"+
		"\5\2\2\u00f4\u00f3\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6\u00f4\3\2\2\2\u00f6"+
		"\u00f7\3\2\2\2\u00f7\64\3\2\2\2\u00f8\u00f9\5\63\32\2\u00f9\u00fb\7\61"+
		"\2\2\u00fa\u00fc\t\5\2\2\u00fb\u00fa\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fd"+
		"\u00fb\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\66\3\2\2\2\u00ff\u0103\t\6\2"+
		"\2\u0100\u0102\t\7\2\2\u0101\u0100\3\2\2\2\u0102\u0105\3\2\2\2\u0103\u0101"+
		"\3\2\2\2\u0103\u0104\3\2\2\2\u0104\u0106\3\2\2\2\u0105\u0103\3\2\2\2\u0106"+
		"\u0107\t\6\2\2\u01078\3\2\2\2\u0108\u010c\7%\2\2\u0109\u010b\n\b\2\2\u010a"+
		"\u0109\3\2\2\2\u010b\u010e\3\2\2\2\u010c\u010a\3\2\2\2\u010c\u010d\3\2"+
		"\2\2\u010d\u010f\3\2\2\2\u010e\u010c\3\2\2\2\u010f\u0110\b\35\2\2\u0110"+
		":\3\2\2\2\u0111\u0113\t\t\2\2\u0112\u0111\3\2\2\2\u0113\u0114\3\2\2\2"+
		"\u0114\u0112\3\2\2\2\u0114\u0115\3\2\2\2\u0115\u0116\3\2\2\2\u0116\u0117"+
		"\b\36\2\2\u0117<\3\2\2\2\16\2\u00d3\u00da\u00dc\u00e4\u00ea\u00f0\u00f6"+
		"\u00fd\u0103\u010c\u0114\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}