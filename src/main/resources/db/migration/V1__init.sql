-- Initial schema

CREATE TABLE IF NOT EXISTS app_settings (
    key   TEXT PRIMARY KEY,
    value TEXT NOT NULL
);

INSERT INTO app_settings (key, value) VALUES ('theme', 'Dracula');
INSERT INTO app_settings (key, value) VALUES ('font_size', '13px');
