drop view if exists complete_snippets;

DO '
    BEGIN
        BEGIN
            ALTER TABLE snippets ADD COLUMN deleted BOOLEAN DEFAULT FALSE;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE ''column deleted already exists in snippets.'';
        END;
    END;
' language plpgsql;

DO '
    BEGIN
        BEGIN
            ALTER TABLE snippets ALTER COLUMN flagged DROP DEFAULT;
            ALTER TABLE snippets ALTER COLUMN flagged TYPE BOOLEAN USING CASE WHEN flagged=0 THEN FALSE ELSE TRUE END;
            ALTER TABLE snippets ALTER COLUMN flagged SET DEFAULT FALSE;
        EXCEPTION
            WHEN undefined_function THEN RAISE NOTICE ''column flagged is already a boolean.'';
        END;
    END
' language plpgsql;


DO '
    BEGIN
        BEGIN
            ALTER TABLE users ALTER COLUMN verified DROP DEFAULT;
            ALTER TABLE users ALTER COLUMN verified TYPE BOOLEAN USING CASE WHEN verified=0 THEN FALSE ELSE TRUE END;
            ALTER TABLE users ALTER COLUMN verified SET DEFAULT FALSE;
        EXCEPTION
            WHEN undefined_function THEN RAISE NOTICE ''column verified is already a boolean.'';
        END;
    END
' language plpgsql;


CREATE OR REPLACE VIEW complete_snippets AS
SELECT aux.sn_id   AS id,
       aux.code    AS code,
       aux.title   AS title,
       aux.descr   AS description,
       aux.dc      AS date_created,
       aux.user_id AS user_id,
       aux.u_name  AS username,
       aux.rep     AS reputation,
       aux.lang    AS lang,
       aux.reg     AS region,
       aux.ver     AS verified,
       aux.lang_id AS language_id,
       l.name      AS language,
       aux.icon    AS icon,
       aux.votes   AS votes,
       aux.flag    AS flagged,
       aux.deleted AS deleted
FROM (
         SELECT sn.id           AS sn_id,
                sn.code         AS code,
                sn.title        AS title,
                sn.description  AS descr,
                sn.language_id  AS lang_id,
                sn.date_created AS dc,
                u.id            AS user_id,
                u.username      AS u_name,
                u.reputation    AS rep,
                u.icon          AS icon,
                u.lang          AS lang,
                u.region        AS reg,
                u.verified      AS ver,
                sn.votes        AS votes,
                sn.flag         AS flag,
                sn.deleted      AS deleted
         FROM (SELECT sni.id                    AS id,
                      sni.code                  AS code,
                      sni.title                 AS title,
                      sni.description           AS description,
                      sni.language_id           AS language_id,
                      sni.date_created          AS date_created,
                      sni.user_id               AS user_id,
                      sni.flagged               AS flag,
                      sni.deleted               AS deleted,
                      SUM(COALESCE(vf.type, 0)) AS votes
               FROM snippets AS sni
                        LEFT OUTER JOIN votes_for AS vf ON vf.snippet_id = sni.id
               GROUP BY sni.id) AS sn
                  JOIN users AS u ON sn.user_id = u.id
     ) AS aux
         JOIN languages AS l ON aux.lang_id = l.id;
