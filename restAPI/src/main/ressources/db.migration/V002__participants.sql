CREATE TABLE IF NOT EXISTS "users"(
    "id" uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    "name" varchar NOT NULL,
    "association" varchar NOT NULL

);
CREATE TABLE IF NOT EXISTS "producers"(
    "id" uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    "name" varchar NOT NULL,
    "association" varchar NOT NULL
);