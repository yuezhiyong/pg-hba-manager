grammar Hba;

// Parser rules
hbaEntry
    : connectionType databaseName userName address authMethod comment? EOF
    ;

connectionType
    : LOCAL
    | HOST
    | HOSTSSL
    | HOSTNOSSL
    ;

databaseName
    : IDENTIFIER
    | QUOTED_STRING
    | ALL
    | SAMEUSER
    | SAMEROLE
    | REPLICATION
    | PLUS_ROLE
    | AT_FILE
    ;

userName
    : IDENTIFIER
    | QUOTED_STRING
    | ALL
    | PLUS_ROLE
    | AT_FILE
    ;

address
    : IP_ADDRESS
    | IP_RANGE
    | HOSTNAME
    | ALL
    ;

authMethod
    : TRUST
    | REJECT
    | MD5
    | PASSWORD
    | GSS
    | SSPI
    | IDENT
    | PEER
    | LDAP
    | RADIUS
    | CERT
    | SCRAM_SHA_256
    ;

comment
    : COMMENT
    ;

// Lexer rules
LOCAL: 'local';
HOST: 'host';
HOSTSSL: 'hostssl';
HOSTNOSSL: 'hostnossl';

ALL: 'all';
SAMEUSER: 'sameuser';
SAMEROLE: 'samerole';
REPLICATION: 'replication';

TRUST: 'trust';
REJECT: 'reject';
MD5: 'md5';
PASSWORD: 'password';
GSS: 'gss';
SSPI: 'sspi';
IDENT: 'ident';
PEER: 'peer';
LDAP: 'ldap';
RADIUS: 'radius';
CERT: 'cert';
SCRAM_SHA_256: 'scram-sha-256';

PLUS_ROLE: '+' IDENTIFIER;
AT_FILE: '@' IDENTIFIER;

IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_-]*;
QUOTED_STRING: '"' (~["\\\r\n] | '\\' .)* '"';
IP_ADDRESS: [0-9]+ '.' [0-9]+ '.' [0-9]+ '.' [0-9]+;
IP_RANGE: IP_ADDRESS '/' [0-9]+;
HOSTNAME: [a-zA-Z0-9] [a-zA-Z0-9.-]* [a-zA-Z0-9];
COMMENT: '#' ~[\r\n]* -> skip;

WS: [ \t\r\n]+ -> skip;