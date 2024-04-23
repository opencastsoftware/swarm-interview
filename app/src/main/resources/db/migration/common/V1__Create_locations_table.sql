CREATE TABLE locations(
    id uuid PRIMARY KEY,
    nickname text NOT NULL
);

COMMENT ON TABLE locations IS 'Locations where energy devices are installed.';

COMMENT ON COLUMN locations.id IS 'Unique identifier for a location.';
COMMENT ON COLUMN locations.nickname IS 'Free text description of a location, provided by the user.';
