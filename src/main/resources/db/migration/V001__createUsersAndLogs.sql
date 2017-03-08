CREATE TABLE "users" (
  "id"       SERIAL,
  "name"     VARCHAR NOT NULL PRIMARY KEY,
  "password" VARCHAR NOT NULL,
  CONSTRAINT uk_users_id UNIQUE (id)
);

CREATE TABLE "logs" (
  "id"      SERIAL PRIMARY KEY,
  "text"    VARCHAR NOT NULL,
  "user_id" INTEGER,
  CONSTRAINT "fk_logs_users" FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);