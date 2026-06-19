CREATE TABLE IF NOT EXISTS "hourly_usage" (
    "hour"                TIMESTAMP NOT NULL PRIMARY KEY,
    "community_produced"  float NOT NULL DEFAULT 0,
    "community_used"      float NOT NULL DEFAULT 0,
    "grid_used"           float NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS "current_percentage" (
    "hour"                TIMESTAMP NOT NULL PRIMARY KEY,
    "community_depleted"  float NOT NULL DEFAULT 0,
    "grid_portion"        float NOT NULL DEFAULT 0
);