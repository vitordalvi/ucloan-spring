-- Drop legacy stateful token table as authentication is now completely stateless
DROP TABLE IF EXISTS tb_token CASCADE;
