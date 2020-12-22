/*
    TO manually populate database in terminal: psql -U postgres -d paw -a -f populate.sql
 */

delete from roles where true;
delete from user_roles where true;

insert into roles(role) values('ADMIN');
insert into roles(role) values('USER');

insert into user_roles(user_id,role_id) values((select id from users where username='JohnDoe'),(select id from roles where role='USER'));
insert into user_roles(user_id,role_id) values((select id from users where username='JaneRoe'),(select id from roles where role='USER'));
insert into user_roles(user_id,role_id) values((select id from users where username='admin'),(select id from roles where role='ADMIN'));

UPDATE users SET email = 'snippit.website@gmail.com' WHERE username='admin';

-- LANGUAGES
insert into languages(name) values('A#');
insert into languages(name) values('A+');
insert into languages(name) values('A++');
insert into languages(name) values('ABAP');
insert into languages(name) values('ACC');
insert into languages(name) values('Accent');
insert into languages(name) values('Action!');
insert into languages(name) values('Ada');
insert into languages(name) values('Agora');
insert into languages(name) values('AIMMS');
insert into languages(name) values('AmbientTalk');
insert into languages(name) values('AMOS');
insert into languages(name) values('AngelScript');
insert into languages(name) values('APL');
insert into languages(name) values('AppleScript');
insert into languages(name) values('Arc');
insert into languages(name) values('Argus');
insert into languages(name) values('Averest');
insert into languages(name) values('AWK');
insert into languages(name) values('B');
insert into languages(name) values('Ballerina');
insert into languages(name) values('Bash');
insert into languages(name) values('BASIC');
insert into languages(name) values('BCPL');
insert into languages(name) values('BETA');
insert into languages(name) values('Boomerang');
insert into languages(name) values('C--');
insert into languages(name) values('C++');
insert into languages(name) values('C Shell');
insert into languages(name) values('Clipper');
insert into languages(name) values('CLIST');
insert into languages(name) values('CLU');
insert into languages(name) values('COBOL');
insert into languages(name) values('Cobra');
insert into languages(name) values('CPL');
insert into languages(name) values('COMPASS');
insert into languages(name) values('Coral 66');
insert into languages(name) values('Cypher Query');
insert into languages(name) values('Cython');
insert into languages(name) values('D');
insert into languages(name) values('Darwin');
insert into languages(name) values('dBase');
insert into languages(name) values('DYNAMO');
insert into languages(name) values('E');
insert into languages(name) values('Ease');
insert into languages(name) values('ECMAScript');
insert into languages(name) values('Elixir');
insert into languages(name) values('Epigram');
insert into languages(name) values('EXEC 2');
insert into languages(name) values('F');
insert into languages(name) values('F#');
insert into languages(name) values('FAUST');
insert into languages(name) values('Flex');
insert into languages(name) values('FOCUS');
insert into languages(name) values('@Formula');
insert into languages(name) values('Fortran');
insert into languages(name) values('Fortress');
insert into languages(name) values('F-Script');
insert into languages(name) values('GAMS');
insert into languages(name) values('GAP');
insert into languages(name) values('GDScript');
insert into languages(name) values('GIT');
insert into languages(name) values('Go');
insert into languages(name) values('Go!');
insert into languages(name) values('GOAL');
insert into languages(name) values('Google Apps Script');
insert into languages(name) values('GOTRAN');
insert into languages(name) values('GraphTalk');
insert into languages(name) values('HTML');
insert into languages(name) values('Hack');
insert into languages(name) values('Harbour');
insert into languages(name) values('Haskell');
insert into languages(name) values('Hermes');
insert into languages(name) values('High Level Assembly');
insert into languages(name) values('HyperTalk');
insert into languages(name) values('BAL');
insert into languages(name) values('IBM RPG');
insert into languages(name) values('J');
insert into languages(name) values('J#');
insert into languages(name) values('J++');
insert into languages(name) values('JASS');
insert into languages(name) values('JavaFX Script');
insert into languages(name) values('Jess');
insert into languages(name) values('Join Java');
insert into languages(name) values('JCL');
insert into languages(name) values('Joule');
insert into languages(name) values('JOVIAL');
insert into languages(name) values('JScript');
insert into languages(name) values('Julia');
insert into languages(name) values('K');
insert into languages(name) values('Kaleidoscope');
insert into languages(name) values('KIF');
insert into languages(name) values('Kotlin');
insert into languages(name) values('KRL');
insert into languages(name) values('KRYPTON');
insert into languages(name) values('Korn Shell');
insert into languages(name) values('Kodu');
insert into languages(name) values('Lasso');
insert into languages(name) values('Lava');
insert into languages(name) values('Lingo');
insert into languages(name) values('Lisp');
insert into languages(name) values('Lite-C');
insert into languages(name) values('Logo');
insert into languages(name) values('Logtalk');
insert into languages(name) values('LSL');
insert into languages(name) values('LiveCode');
insert into languages(name) values('LiveScript');
insert into languages(name) values('Lynx');
insert into languages(name) values('Maple');
insert into languages(name) values('MATH-MATIC');
insert into languages(name) values('MaxScript');
insert into languages(name) values('MIMIC');
insert into languages(name) values('MIVA Script');
insert into languages(name) values('ML');
insert into languages(name) values('NASM');
insert into languages(name) values('NetLogo');
insert into languages(name) values('Objective-C');
insert into languages(name) values('Octave');
insert into languages(name) values('Opal');
insert into languages(name) values('OpenCL');
insert into languages(name) values('OPL');
insert into languages(name) values('Oriel');
insert into languages(name) values('Orwell');
insert into languages(name) values('Oz');
insert into languages(name) values('P');
insert into languages(name) values('Pascal');
insert into languages(name) values('PEARL');
insert into languages(name) values('PHP');
insert into languages(name) values('Pico');
insert into languages(name) values('Pure');
insert into languages(name) values('QBasic');
insert into languages(name) values('R');
insert into languages(name) values('R++');
insert into languages(name) values('Racket');
insert into languages(name) values('Ruby');
insert into languages(name) values('Rust');
insert into languages(name) values('Scala');
insert into languages(name) values('Scheme');
insert into languages(name) values('SenseTalk');
insert into languages(name) values('SMALL');
insert into languages(name) values('Smalltalk');
insert into languages(name) values('SOL');
insert into languages(name) values('SPARK');
insert into languages(name) values('SQL');
insert into languages(name) values('SuperTalk');
insert into languages(name) values('Swift');
insert into languages(name) values('Turing');
insert into languages(name) values('UNITY');
insert into languages(name) values('Viper');
insert into languages(name) values('Visual Basic');
insert into languages(name) values('Visual Fortran');
insert into languages(name) values('Wolfram');
insert into languages(name) values('xBase');
insert into languages(name) values('XOD');
insert into languages(name) values('XQuery');
insert into languages(name) values('XSharp');
insert into languages(name) values('Zebra');
insert into languages(name) values('ZOPL');

-- TAGS
INSERT INTO tags(name) VALUES('android');
INSERT INTO tags(name) VALUES('jquery');
INSERT INTO tags(name) VALUES('ios');
INSERT INTO tags(name) VALUES('asp.net');
INSERT INTO tags(name) VALUES('arrays');
INSERT INTO tags(name) VALUES('json');
INSERT INTO tags(name) VALUES('django');
INSERT INTO tags(name) VALUES('regex');
INSERT INTO tags(name) VALUES('iphone');
INSERT INTO tags(name) VALUES('linux');
INSERT INTO tags(name) VALUES('spring');
INSERT INTO tags(name) VALUES('database');
INSERT INTO tags(name) VALUES('wordpress');
INSERT INTO tags(name) VALUES('string');
INSERT INTO tags(name) VALUES('pandas');
INSERT INTO tags(name) VALUES('laravel');
INSERT INTO tags(name) VALUES('windows');
INSERT INTO tags(name) VALUES('mongodb');
INSERT INTO tags(name) VALUES('multithreading');
INSERT INTO tags(name) VALUES('git');
INSERT INTO tags(name) VALUES('forms');
INSERT INTO tags(name) VALUES('list');
INSERT INTO tags(name) VALUES('image');
INSERT INTO tags(name) VALUES('macos');
INSERT INTO tags(name) VALUES('algorithm');
INSERT INTO tags(name) VALUES('firebase');
INSERT INTO tags(name) VALUES('apache');
INSERT INTO tags(name) VALUES('hibernate');
INSERT INTO tags(name) VALUES('function');
INSERT INTO tags(name) VALUES('spring-boot');
INSERT INTO tags(name) VALUES('linq');
INSERT INTO tags(name) VALUES('rest');
INSERT INTO tags(name) VALUES('docker');
INSERT INTO tags(name) VALUES('maven');
INSERT INTO tags(name) VALUES('numpy');
INSERT INTO tags(name) VALUES('selenium');
INSERT INTO tags(name) VALUES('loops');
INSERT INTO tags(name) VALUES('unit-testing');
INSERT INTO tags(name) VALUES('class');
INSERT INTO tags(name) VALUES('date');
INSERT INTO tags(name) VALUES('cordova');
INSERT INTO tags(name) VALUES('validation');
INSERT INTO tags(name) VALUES('sockets');
INSERT INTO tags(name) VALUES('apache-spark');
INSERT INTO tags(name) VALUES('tensorflow');
INSERT INTO tags(name) VALUES('dictionary');
INSERT INTO tags(name) VALUES('oop');
INSERT INTO tags(name) VALUES('security');
INSERT INTO tags(name) VALUES('unity3d');
INSERT INTO tags(name) VALUES('pointers');
INSERT INTO tags(name) VALUES('matplotlib');
INSERT INTO tags(name) VALUES('if-statement');
INSERT INTO tags(name) VALUES('variables');
INSERT INTO tags(name) VALUES('elasticsearch');
INSERT INTO tags(name) VALUES('unix');
INSERT INTO tags(name) VALUES('nginx');
INSERT INTO tags(name) VALUES('generics');
INSERT INTO tags(name) VALUES('asynchronous');
INSERT INTO tags(name) VALUES('testing');
INSERT INTO tags(name) VALUES('gradle');
INSERT INTO tags(name) VALUES('curl');
INSERT INTO tags(name) VALUES('recursion');
INSERT INTO tags(name) VALUES('inheritance');
INSERT INTO tags(name) VALUES('flask');
INSERT INTO tags(name) VALUES('image-processing');
INSERT INTO tags(name) VALUES('gcc');
INSERT INTO tags(name) VALUES('events');
INSERT INTO tags(name) VALUES('caching');
INSERT INTO tags(name) VALUES('logging');
INSERT INTO tags(name) VALUES('matrix');
INSERT INTO tags(name) VALUES('encryption');
INSERT INTO tags(name) VALUES('audio');
INSERT INTO tags(name) VALUES('memory');
INSERT INTO tags(name) VALUES('arraylist');
INSERT INTO tags(name) VALUES('cookies');
INSERT INTO tags(name) VALUES('jdbc');
INSERT INTO tags(name) VALUES('multidimensional-array');
INSERT INTO tags(name) VALUES('random');
INSERT INTO tags(name) VALUES('indexing');
INSERT INTO tags(name) VALUES('text');
INSERT INTO tags(name) VALUES('mobile');
INSERT INTO tags(name) VALUES('kubernetes');
INSERT INTO tags(name) VALUES('data-structures');
INSERT INTO tags(name) VALUES('methods');
INSERT INTO tags(name) VALUES('file-upload');
INSERT INTO tags(name) VALUES('memory-management');
INSERT INTO tags(name) VALUES('struct');
INSERT INTO tags(name) VALUES('lambda');
INSERT INTO tags(name) VALUES('ssh');
INSERT INTO tags(name) VALUES('junit');
INSERT INTO tags(name) VALUES('graph');
INSERT INTO tags(name) VALUES('filter');
INSERT INTO tags(name) VALUES('replace');
INSERT INTO tags(name) VALUES('reflection');
INSERT INTO tags(name) VALUES('types');
INSERT INTO tags(name) VALUES('encoding');
INSERT INTO tags(name) VALUES('hash');
INSERT INTO tags(name) VALUES('foreach');
INSERT INTO tags(name) VALUES('error-handling');
INSERT INTO tags(name) VALUES('merge');
INSERT INTO tags(name) VALUES('group-by');
INSERT INTO tags(name) VALUES('enums');
INSERT INTO tags(name) VALUES('parameters');
INSERT INTO tags(name) VALUES('parallel-processing');
INSERT INTO tags(name) VALUES('deep-learning');
INSERT INTO tags(name) VALUES('count');
INSERT INTO tags(name) VALUES('automation');
INSERT INTO tags(name) VALUES('configuration');
INSERT INTO tags(name) VALUES('neural-network');
INSERT INTO tags(name) VALUES('flexbox');
INSERT INTO tags(name) VALUES('exception-handling');
INSERT INTO tags(name) VALUES('cron');
INSERT INTO tags(name) VALUES('grep');
INSERT INTO tags(name) VALUES('static');
INSERT INTO tags(name) VALUES('tree');
INSERT INTO tags(name) VALUES('binary-tree');
INSERT INTO tags(name) VALUES('transactions');
INSERT INTO tags(name) VALUES('openssl');
INSERT INTO tags(name) VALUES('hashmap');
INSERT INTO tags(name) VALUES('statistics');
INSERT INTO tags(name) VALUES('macros');
INSERT INTO tags(name) VALUES('iterator');
INSERT INTO tags(name) VALUES('cryptography');
INSERT INTO tags(name) VALUES('css-selectors');
INSERT INTO tags(name) VALUES('sum');
INSERT INTO tags(name) VALUES('duplicates');
INSERT INTO tags(name) VALUES('computer-vision');
INSERT INTO tags(name) VALUES('event-handling');
INSERT INTO tags(name) VALUES('internationalization');
INSERT INTO tags(name) VALUES('network-programming');
INSERT INTO tags(name) VALUES('dependencies');
INSERT INTO tags(name) VALUES('numbers');
INSERT INTO tags(name) VALUES('int');
INSERT INTO tags(name) VALUES('integer');
INSERT INTO tags(name) VALUES('set');
INSERT INTO tags(name) VALUES('passwords');
INSERT INTO tags(name) VALUES('foreign-keys');
INSERT INTO tags(name) VALUES('boolean');
INSERT INTO tags(name) VALUES('synchronization');
INSERT INTO tags(name) VALUES('stack');
INSERT INTO tags(name) VALUES('gdb');
INSERT INTO tags(name) VALUES('hover');
INSERT INTO tags(name) VALUES('queue');
INSERT INTO tags(name) VALUES('subprocess');
INSERT INTO tags(name) VALUES('map');
INSERT INTO tags(name) VALUES('base64');
INSERT INTO tags(name) VALUES('dataset');
INSERT INTO tags(name) VALUES('append');
INSERT INTO tags(name) VALUES('tuples');
INSERT INTO tags(name) VALUES('iteration');
INSERT INTO tags(name) VALUES('cypher');
INSERT INTO tags(name) VALUES('cursor');
INSERT INTO tags(name) VALUES('singleton');
INSERT INTO tags(name) VALUES('key');
INSERT INTO tags(name) VALUES('substring');
INSERT INTO tags(name) VALUES('mapping');
INSERT INTO tags(name) VALUES('coding-style');
INSERT INTO tags(name) VALUES('malloc');
INSERT INTO tags(name) VALUES('alignment');
INSERT INTO tags(name) VALUES('logic');
INSERT INTO tags(name) VALUES('fragment');
INSERT INTO tags(name) VALUES('local-storage');
INSERT INTO tags(name) VALUES('double');
INSERT INTO tags(name) VALUES('bigdata');
INSERT INTO tags(name) VALUES('constraints');
INSERT INTO tags(name) VALUES('overriding');
INSERT INTO tags(name) VALUES('operator-overloading');
INSERT INTO tags(name) VALUES('comparison');
INSERT INTO tags(name) VALUES('material-design');
INSERT INTO tags(name) VALUES('material-ui');
INSERT INTO tags(name) VALUES('out-of-memory');
INSERT INTO tags(name) VALUES('time-complexity');
INSERT INTO tags(name) VALUES('try-catch');
INSERT INTO tags(name) VALUES('frontend');
INSERT INTO tags(name) VALUES('max');
INSERT INTO tags(name) VALUES('less');
INSERT INTO tags(name) VALUES('refactoring');
INSERT INTO tags(name) VALUES('concatenation');
INSERT INTO tags(name) VALUES('conditional');
INSERT INTO tags(name) VALUES('bit-manipulation');
INSERT INTO tags(name) VALUES('operators');
INSERT INTO tags(name) VALUES('expression');
INSERT INTO tags(name) VALUES('byte');
INSERT INTO tags(name) VALUES('deserialization');
INSERT INTO tags(name) VALUES('regression');
INSERT INTO tags(name) VALUES('database-connection');
INSERT INTO tags(name) VALUES('decimal');
INSERT INTO tags(name) VALUES('relational-database');
INSERT INTO tags(name) VALUES('buffer');
INSERT INTO tags(name) VALUES('grouping');
INSERT INTO tags(name) VALUES('css-animations');
INSERT INTO tags(name) VALUES('overflow');
INSERT INTO tags(name) VALUES('subset');
INSERT INTO tags(name) VALUES('histogram');
INSERT INTO tags(name) VALUES('border');
INSERT INTO tags(name) VALUES('heap');