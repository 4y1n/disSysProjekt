CREATE TABLE IF NOT EXISTS "hourly_usage"(
"hour"  TIMESTAMP NOT NULL PRIMARY KEY,
"energy_used" float NOT NULL DEFAULT 0,
"energy_produced" float NOT NULL DEFAULT 0,
"grid_used" float NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS "energy_percentage"(
    "hour" TIMESTAMP NOT NULL PRIMARY KEY,
    "community_depleted" float NOT NULL DEFAULT 0,
    "grid_portion" float NOT NULL DEFAULT 0
);