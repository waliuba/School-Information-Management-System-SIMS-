-- Recovery guidance: dropping these tables removes settings and audit history; export and retain
-- required audit data before rollback. Reverting TOKEN_VERSION to zero re-enables old JWTs.
DROP TRIGGER TRG_SIMS_AUDIT_IMMUTABLE;
DROP INDEX IX_SIMS_AUDIT_ACTOR;
DROP INDEX IX_SIMS_AUDIT_OCCURRED;
DROP TABLE SIMS_AUDIT_EVENT PURGE;
DROP TABLE SIMS_SETTING PURGE;
ALTER TABLE USERS DROP COLUMN TOKEN_VERSION;
