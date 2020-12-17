--
-- PostgreSQL database dump
--

-- Dumped from database version 12.4
-- Dumped by pg_dump version 12.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: tags; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (19, 'jquery', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (24, 'django', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (27, 'linux', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (34, 'windows', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (35, 'mongodb', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (38, 'forms', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (46, 'function', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (48, 'linq', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (49, 'rest', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (52, 'numpy', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (58, 'cordova', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (60, 'sockets', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (62, 'tensorflow', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (63, 'dictionary', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (64, 'oop', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (65, 'security', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (66, 'unity3d', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (67, 'pointers', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (68, 'matplotlib', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (69, 'if-statement', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (70, 'variables', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (72, 'unix', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (73, 'nginx', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (74, 'generics', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (76, 'testing', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (77, 'gradle', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (78, 'curl', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (79, 'recursion', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (81, 'flask', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (82, 'image-processing', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (83, 'gcc', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (84, 'events', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (86, 'logging', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (87, 'matrix', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (88, 'encryption', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (90, 'memory', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (93, 'jdbc', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (95, 'random', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (96, 'indexing', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (97, 'text', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (98, 'mobile', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (99, 'kubernetes', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (101, 'methods', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (102, 'file-upload', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (103, 'memory-management', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (104, 'struct', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (105, 'lambda', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (106, 'ssh', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (107, 'junit', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (109, 'filter', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (110, 'replace', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (111, 'reflection', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (112, 'types', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (113, 'encoding', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (117, 'merge', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (118, 'group-by', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (119, 'enums', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (120, 'parameters', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (122, 'deep-learning', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (123, 'count', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (125, 'configuration', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (127, 'flexbox', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (129, 'cron', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (130, 'grep', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (131, 'static', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (132, 'tree', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (135, 'openssl', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (137, 'statistics', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (138, 'macros', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (139, 'iterator', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (141, 'css-selectors', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (142, 'sum', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (143, 'duplicates', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (144, 'computer-vision', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (147, 'network-programming', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (148, 'dependencies', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (149, 'numbers', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (150, 'int', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (151, 'integer', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (152, 'set', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (153, 'passwords', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (154, 'foreign-keys', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (157, 'stack', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (158, 'gdb', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (160, 'queue', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (161, 'subprocess', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (162, 'map', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (164, 'dataset', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (166, 'tuples', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (167, 'iteration', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (170, 'singleton', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (171, 'key', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (172, 'substring', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (173, 'mapping', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (177, 'logic', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (178, 'fragment', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (180, 'double', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (42, 'algorithm', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (140, 'cryptography', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (61, 'apache-spark', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (165, 'append', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (91, 'arraylist', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (22, 'arrays', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (21, 'asp.net', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (75, 'asynchronous', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (89, 'audio', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (124, 'automation', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (29, 'database', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (10, 'greedy', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (45, 'hibernate', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (20, 'ios', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (26, 'iphone', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (23, 'json', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (33, 'laravel', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (13, 'machine-learning', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (12, 'math', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (32, 'pandas', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (11, 'performance', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (25, 'regex', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (85, 'caching', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (56, 'class', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (134, 'transactions', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (59, 'validation', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (146, 'internationalization', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (179, 'local-storage', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (175, 'malloc', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (94, 'multidimensional-array', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (126, 'neural-network', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (121, 'parallel-processing', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (174, 'coding-style', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (92, 'cookies', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (100, 'data-structures', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (14, 'style', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (30, 'wordpress', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (47, 'spring-boot', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (50, 'docker', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (43, 'firebase', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (37, 'git', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (39, 'list', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (41, 'macos', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (36, 'multithreading', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (28, 'spring', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (31, 'string', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (55, 'unit-testing', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (156, 'synchronization', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (168, 'cypher', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (71, 'elasticsearch', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (116, 'error-handling', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (145, 'event-handling', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (128, 'exception-handling', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (115, 'foreach', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (108, 'graph', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (114, 'hash', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (136, 'hashmap', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (159, 'hover', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (176, 'alignment', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (40, 'image', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (54, 'loops', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (44, 'apache', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (57, 'date', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (51, 'maven', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (182, 'constraints', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (183, 'overriding', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (184, 'operator-overloading', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (188, 'out-of-memory', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (189, 'time-complexity', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (191, 'frontend', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (192, 'max', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (193, 'less', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (194, 'refactoring', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (195, 'concatenation', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (198, 'operators', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (199, 'expression', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (202, 'regression', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (203, 'database-connection', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (207, 'grouping', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (208, 'css-animations', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (209, 'overflow', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (210, 'subset', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (226, 'JAVA', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (223, 'Android', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (163, 'base64', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (181, 'bigdata', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (133, 'binary-tree', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (155, 'boolean', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (212, 'border', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (206, 'buffer', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (53, 'selenium', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (9, 'sort', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (8, 'sorting', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (185, 'comparison', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (196, 'conditional', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (204, 'decimal', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (201, 'deserialization', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (186, 'material-design', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (187, 'material-ui', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (205, 'relational-database', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (190, 'try-catch', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (213, 'heap', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (211, 'histogram', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (80, 'inheritance', false);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (197, 'bit-manipulation', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (200, 'byte', true);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (227, 'js', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (228, 'javas', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (229, 'Delete', NULL);
INSERT INTO public.tags (id, name, snippetsusingisempty) VALUES (233, 'javass', NULL);


--
-- Name: tags_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tags_id_seq', 233, true);


--
-- PostgreSQL database dump complete
--

