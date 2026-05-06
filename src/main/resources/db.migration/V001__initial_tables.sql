CREATE TABLE IF NOT EXISTS "energy_usage"(
"time" datetime NOT NULL,
"energy_used" float NOT NULL,
"energy_produced" float NOT NULL,
"grid_used" float NOT NULL
)
CREATE TABLE IF NOT EXISTS "energy_percentage"(
    "time" datetime NOT NULL,
    "community_depleted" float NOT NULL,
    "grid_portion" float NOT NULL
)