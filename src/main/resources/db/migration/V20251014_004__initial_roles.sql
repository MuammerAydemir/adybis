INSERT INTO roles (name,description)
VALUES ('admin', 'The role type for admin'),
        ('victim', 'The role type for victim'),
        ('rescue_team', 'The role type for rescue team'),
        ('dispatcher', 'The role type for dispatcher'),
        ('viewer', 'The role type for viewer')
ON CONFLICT (name) DO NOTHING;
