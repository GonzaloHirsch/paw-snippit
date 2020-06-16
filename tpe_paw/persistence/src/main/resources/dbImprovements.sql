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
            ALTER TABLE languages ADD COLUMN deleted BOOLEAN DEFAULT FALSE;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE ''column deleted already exists in languages.'';
        END;
    END;
' language plpgsql;

-- UPDATE languages SET deleted = FALSE;

DO '
    BEGIN
        BEGIN
            ALTER TABLE snippets ALTER COLUMN flagged DROP DEFAULT;
            ALTER TABLE snippets ALTER COLUMN flagged TYPE BOOLEAN USING CASE WHEN flagged=0 THEN FALSE ELSE TRUE END;
            ALTER TABLE snippets ALTER COLUMN flagged SET DEFAULT FALSE;
        EXCEPTION WHEN OTHERS THEN
            RAISE NOTICE ''column flagged is already a boolean.'';
        END;
    END
' language plpgsql;


DO '
    BEGIN
        BEGIN
            ALTER TABLE users ALTER COLUMN verified DROP DEFAULT;
            ALTER TABLE users ALTER COLUMN verified TYPE BOOLEAN USING CASE WHEN verified=0 THEN FALSE ELSE TRUE END;
            ALTER TABLE users ALTER COLUMN verified SET DEFAULT FALSE;
        EXCEPTION WHEN OTHERS THEN
            RAISE NOTICE ''column verified is already a boolean.'';
        END;
    END
' language plpgsql;

DO '
    BEGIN
        BEGIN
            ALTER TABLE votes_for RENAME COLUMN type TO is_positive;
        EXCEPTION WHEN OTHERS THEN
            RAISE NOTICE ''column type has already been renamed to is_positive'';
        END;
    END
' language plpgsql;

DO '
    BEGIN
        BEGIN
            ALTER TABLE votes_for DROP CONSTRAINT votes_for_type_check;
        EXCEPTION WHEN OTHERS THEN
            RAISE NOTICE ''constraint votes_for_type_check was already dropped'';
        END;
    END
' language plpgsql;


DO '
    BEGIN
        BEGIN
            ALTER TABLE votes_for ALTER COLUMN is_positive TYPE BOOLEAN USING CASE WHEN is_positive=1 THEN TRUE ELSE FALSE END;
        EXCEPTION WHEN OTHERS THEN
            RAISE NOTICE ''column is_positive is already a boolean.'';
        END;
    END
' language plpgsql;

CREATE TABLE IF NOT EXISTS reported
(
    snippet_id INT REFERENCES snippets (id) ON UPDATE CASCADE ON DELETE CASCADE,
    user_id    INT REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
    detail VARCHAR(500),
    PRIMARY KEY (snippet_id, user_id)
);



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
                      SUM(CASE WHEN vf.is_positive THEN 1 ELSE -1 END) AS votes
               FROM snippets AS sni
                        LEFT OUTER JOIN votes_for AS vf ON vf.snippet_id = sni.id
               GROUP BY sni.id) AS sn
                  JOIN users AS u ON sn.user_id = u.id
     ) AS aux
         JOIN languages AS l ON aux.lang_id = l.id;


-- Notation of trigger uses single quotes instead of $$ due to parser errors while parsing
-- The use of TG_OP is needed to avoid the error of not assigned variables NEW or OLD
CREATE OR REPLACE FUNCTION update_reputation() RETURNS TRIGGER AS
'
    DECLARE
        newReputation int;
        uid           bigint;
        snippetId     bigint;
    BEGIN
        IF TG_OP = ''INSERT'' OR TG_OP = ''UPDATE'' THEN
            snippetId = NEW.snippet_id;
        ELSIF TG_OP = ''DELETE'' THEN
            snippetId = OLD.snippet_id;
        END IF;
        uid = (SELECT user_id
               FROM snippets AS sn
               WHERE sn.id = snippetId);
        newReputation = (SELECT reputation
                         FROM users AS u
                         WHERE u.id = uid);
        IF TG_OP = ''INSERT'' THEN
            IF NEW IS NOT null THEN
                newReputation = newReputation + (CASE WHEN NEW.is_positive THEN 1 ELSE -1 END);
            END IF;
        ELSIF TG_OP = ''UPDATE'' THEN
            IF NEW IS NOT null THEN
                newReputation = newReputation + (CASE WHEN NEW.is_positive THEN 1 ELSE -1 END);
            END IF;
            IF OLD IS NOT null THEN
                newReputation = newReputation - (CASE WHEN OLD.is_positive THEN 1 ELSE -1 END);
            END IF;
        ELSIF TG_OP = ''DELETE'' THEN
            IF OLD IS NOT null THEN
                newReputation = newReputation - (CASE WHEN OLD.is_positive THEN 1 ELSE -1 END);
            END IF;
        END IF;
        UPDATE users AS u
        SET reputation = newReputation
        WHERE u.id = uid;
        IF TG_OP = ''INSERT'' OR TG_OP = ''UPDATE'' THEN
            RETURN NEW;
        ELSIF TG_OP = ''DELETE'' THEN
            RETURN OLD;
        END IF;
    END;
' language plpgsql;

DROP TRIGGER IF EXISTS update_reputation_trigger ON votes_for;

-- Notation uses PROCEDURE instead of FUNCTION to make it backwards compatible
CREATE TRIGGER update_reputation_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON votes_for
    FOR EACH ROW
EXECUTE PROCEDURE update_reputation();