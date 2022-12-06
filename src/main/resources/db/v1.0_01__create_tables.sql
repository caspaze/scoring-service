CREATE TABLE "client" (
                                 "id" serial NOT NULL,
                                 "checking_account_status" integer NOT NULL,
                                 "duration" integer NOT NULL,
                                 "credit_history" integer NOT NULL,
                                 "purpose" integer NOT NULL,
                                 "credit_amount" integer NOT NULL,
                                 "saving_accounts" integer NOT NULL,
                                 "employment_since" integer NOT NULL,
                                 "rate_percent" DECIMAL NOT NULL,
                                 "personal_status" integer NOT NULL,
                                 "other_debtors" integer NOT NULL,
                                 "resident_since" integer NOT NULL,
                                 "property" integer NOT NULL,
                                 "age" integer NOT NULL,
                                 "other_installment" integer NOT NULL,
                                 "housing" integer NOT NULL,
                                 "existing_credits_number" integer NOT NULL,
                                 "job" integer NOT NULL,
                                 "num_dependents" integer NOT NULL,
                                 "telephone" bool NOT NULL,
                                 "foreign_worker" bool NOT NULL,
                                 "creditor_type" varchar NOT NULL,
                                 CONSTRAINT "clientt_pk" PRIMARY KEY ("id")
) WITH (
      OIDS=FALSE
    );



CREATE TABLE "checking_account" (
                                           "id" serial NOT NULL,
                                           "status" varchar(255) NOT NULL,
                                           CONSTRAINT "checking_account_pk" PRIMARY KEY ("id")
) WITH (
      OIDS=FALSE
    );



CREATE TABLE "credit_history" (
                                         "id" serial NOT NULL,
                                         "type" varchar(255) NOT NULL,
                                         CONSTRAINT "credit_history_pk" PRIMARY KEY ("id")
) WITH (
      OIDS=FALSE
    );



CREATE TABLE "purpose" (
                                  "id" serial NOT NULL,
                                  "purpose" varchar(255) NOT NULL,
                                  CONSTRAINT "purpose_pk" PRIMARY KEY ("id")
) WITH (
      OIDS=FALSE
    );



CREATE TABLE "accounts" (
                                   "id" serial NOT NULL,
                                   "saves" VARCHAR(255) NOT NULL,
                                   CONSTRAINT "accounts_pk" PRIMARY KEY ("id")
) WITH (
      OIDS=FALSE
    );



CREATE TABLE "employment" (
                                     "id" serial NOT NULL,
                                     "duration" serial NOT NULL,
                                     CONSTRAINT "employment_pk" PRIMARY KEY ("id")
) WITH (
      OIDS=FALSE
    );



CREATE TABLE "personal_status" (
                                          "id" serial NOT NULL,
                                          "status" varchar(255) NOT NULL,
                                          CONSTRAINT "personal_status_pk" PRIMARY KEY ("id")
) WITH (
      OIDS=FALSE
    );



CREATE TABLE "guarantors" (
                                     "id" serial NOT NULL,
                                     "type" varchar(255) NOT NULL,
                                     CONSTRAINT "guarantors_pk" PRIMARY KEY ("id")
) WITH (
      OIDS=FALSE
    );



CREATE TABLE "property" (
                                   "id" serial NOT NULL,
                                   "type" varchar(255) NOT NULL,
                                   CONSTRAINT "property_pk" PRIMARY KEY ("id")
) WITH (
      OIDS=FALSE
    );



CREATE TABLE "other_installment" (
                                            "id" serial NOT NULL,
                                            "installment" varchar(255) NOT NULL,
                                            CONSTRAINT "other_installment_pk" PRIMARY KEY ("id")
) WITH (
      OIDS=FALSE
    );

CREATE TABLE "housing" (
                                             "id" serial NOT NULL,
                                             "type" varchar(255) NOT NULL,
                                             CONSTRAINT "housing_pk" PRIMARY KEY ("id")
) WITH (
      OIDS=FALSE
    );


CREATE TABLE "job" (
                                   "id" serial NOT NULL,
                                   "job" varchar(255) NOT NULL,
                                   CONSTRAINT "job_pk" PRIMARY KEY ("id")
) WITH (
      OIDS=FALSE
    );


CREATE TABLE "phone" (
                                   "id" serial NOT NULL,
                                   "type" varchar(255) NOT NULL,
                                   CONSTRAINT "phone_pk" PRIMARY KEY ("id")
) WITH (
      OIDS=FALSE
    );


CREATE TABLE "foreign_worker" (
                                 "id" serial NOT NULL,
                                 "worker_type" varchar(255) NOT NULL,
                                 CONSTRAINT "foreign_worker_pk" PRIMARY KEY ("id")
) WITH (
      OIDS=FALSE
    );



ALTER TABLE "client" ADD CONSTRAINT "client_fk0" FOREIGN KEY ("checking_account_status") REFERENCES "checking_account"("id");
ALTER TABLE "client" ADD CONSTRAINT "client_fk1" FOREIGN KEY ("credit_history") REFERENCES "credit_history"("id");
ALTER TABLE "client" ADD CONSTRAINT "client_fk2" FOREIGN KEY ("purpose") REFERENCES "purpose"("id");
ALTER TABLE "client" ADD CONSTRAINT "client_fk3" FOREIGN KEY ("saving_accounts") REFERENCES "accounts"("id");
ALTER TABLE "client" ADD CONSTRAINT "client_fk4" FOREIGN KEY ("employment_since") REFERENCES "employment"("id");
ALTER TABLE "client" ADD CONSTRAINT "client_fk5" FOREIGN KEY ("personal_status") REFERENCES "personal_status"("id");
ALTER TABLE "client" ADD CONSTRAINT "client_fk6" FOREIGN KEY ("other_debtors") REFERENCES "guarantors"("id");
ALTER TABLE "client" ADD CONSTRAINT "client_fk7" FOREIGN KEY ("property") REFERENCES "property"("id");
ALTER TABLE "client" ADD CONSTRAINT "client_fk8" FOREIGN KEY ("other_installment") REFERENCES "other_installment"("id");
ALTER TABLE "client" ADD CONSTRAINT "client_fk9" FOREIGN KEY ("housing") REFERENCES "housing"("id");
ALTER TABLE "client" ADD CONSTRAINT "client_fk10" FOREIGN KEY ("job") REFERENCES "job"("id");



















