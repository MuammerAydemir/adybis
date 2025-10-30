CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS "users" (
  "id" UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  "username" VARCHAR(50) UNIQUE NOT NULL,
  "email" VARCHAR(255) UNIQUE NOT NULL,
  "phone" VARCHAR(20) UNIQUE NOT NULL,
  "hashed_password" VARCHAR(255) NOT NULL,
  "birthday_date" DATE NOT NULL,
  "blood_type"  VARCHAR(20) NOT NULL,
  "chronic_illnesses" BOOLEAN DEFAULT false,
  "people_with_disabilities" BOOLEAN DEFAULT false,
  "special_case_description" TEXT,
  "is_2fa_enabled" BOOLEAN DEFAULT false,
  "is_verified" BOOLEAN DEFAULT false,
  "created_at" TIMESTAMP DEFAULT (now()),
  "updated_at" TIMESTAMP DEFAULT (now())
);

CREATE TABLE IF NOT EXISTS "roles" (
  "id" UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  "name" VARCHAR(50) UNIQUE NOT NULL,
  "description" TEXT,
  "created_at" TIMESTAMP DEFAULT (now())
);

CREATE TABLE IF NOT EXISTS "user_roles" (
  "id" UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  "user_id" UUID NOT NULL,
  "role_id" UUID NOT NULL,
  "created_at" TIMESTAMP DEFAULT (now())
);

CREATE TABLE IF NOT EXISTS "rescue_team_locations" (
  "id" UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  "rescue_team_id" UUID NOT NULL,
  "latitude" DECIMAL(10,8) NOT NULL,
  "longitude" DECIMAL(11,8) NOT NULL,
  "created_at" TIMESTAMP NOT NULL DEFAULT NOW(),
  "updated_at" TIMESTAMP  NOT NULL DEFAULT NOW(),
  "status" VARCHAR(30) DEFAULT 'active'
);

CREATE TABLE IF NOT EXISTS "two_factor_auth" (
  "id" UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  "user_id" UUID NOT NULL,
  "method" VARCHAR(10) NOT NULL,
  "code" VARCHAR(10) NOT NULL,
  "expires_at" TIMESTAMP NOT NULL,
  "is_used" BOOLEAN DEFAULT false,
  "created_at" TIMESTAMP DEFAULT (now())
);

CREATE TABLE IF NOT EXISTS "help_requests" (
  "id" UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  "victim_id" UUID NOT NULL,
  "latitude" DECIMAL(10,8) NOT NULL,
  "longitude" DECIMAL(11,8) NOT NULL,
  "status" VARCHAR(20) DEFAULT 'pending',
  "description" TEXT,
  "created_at" TIMESTAMP DEFAULT (now()),
  "updated_at" TIMESTAMP DEFAULT (now())
);

CREATE TABLE IF NOT EXISTS "help_points" (
  "id" UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  "name" VARCHAR(255) NOT NULL,
  "latitude" DECIMAL(10,8) NOT NULL,
  "longitude" DECIMAL(11,8) NOT NULL,
  "description" TEXT,
  "type" VARCHAR(50) NOT NULL,
  "is_active" BOOLEAN DEFAULT true,
  "created_by" UUID NOT NULL,
  "created_at" TIMESTAMP DEFAULT (now()),
  "updated_at" TIMESTAMP DEFAULT (now())
);

CREATE TABLE IF NOT EXISTS "rescue_assignments" (
  "id" UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  "help_request_id" UUID NOT NULL,
  "rescue_team_id" UUID NOT NULL,
  "created_by" UUID NOT NULL,
  "assigned_at" TIMESTAMP DEFAULT (now()),
  "status" VARCHAR(20) DEFAULT 'assigned',
  "completed_at" TIMESTAMP
);




CREATE UNIQUE INDEX  ON "user_roles"  ("user_id", "role_id");

ALTER TABLE "user_roles" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "user_roles" ADD FOREIGN KEY ("role_id") REFERENCES "roles" ("id");

ALTER TABLE "rescue_team_locations" ADD FOREIGN KEY ("rescue_team_id") REFERENCES "users" ("id");

ALTER TABLE "two_factor_auth" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "help_requests" ADD FOREIGN KEY ("victim_id") REFERENCES "users" ("id");

ALTER TABLE "help_points" ADD FOREIGN KEY ("created_by") REFERENCES "users" ("id");

ALTER TABLE "rescue_assignments" ADD FOREIGN KEY ("help_request_id") REFERENCES "help_requests" ("id");

ALTER TABLE "rescue_assignments" ADD FOREIGN KEY ("rescue_team_id") REFERENCES "users" ("id");

ALTER TABLE "rescue_assignments" ADD FOREIGN KEY ("created_by") REFERENCES "users" ("id");

