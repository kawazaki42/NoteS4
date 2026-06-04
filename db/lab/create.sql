-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler version: 1.1.0-beta1
-- PostgreSQL version: 16.0
-- Project Site: pgmodeler.io
-- Model Author: ---

-- Database creation must be performed outside a multi lined SQL file. 
-- These commands were put in this file only as a convenience.
-- 
-- object: workload | type: DATABASE --
-- DROP DATABASE IF EXISTS workload;
CREATE DATABASE workload;
-- ddl-end --


SET check_function_bodies = false;
-- ddl-end --

-- object: public.lecturer | type: TABLE --
-- DROP TABLE IF EXISTS public.lecturer CASCADE;
CREATE TABLE public.lecturer (
	id integer NOT NULL,
	surname text,
	first_name text NOT NULL,
	patronym text,
	department text,
	degree text,
	occupation text,
	work_begin_year smallint,
	CONSTRAINT lecturer_pk PRIMARY KEY (id)
);
-- ddl-end --

-- object: public.discipline | type: TABLE --
-- DROP TABLE IF EXISTS public.discipline CASCADE;
CREATE TABLE public.discipline (
	id integer NOT NULL,
	name text NOT NULL,
	CONSTRAINT discipline_pk PRIMARY KEY (id)
);
-- ddl-end --

-- object: public."group" | type: TABLE --
-- DROP TABLE IF EXISTS public."group" CASCADE;
CREATE TABLE public."group" (
	name text NOT NULL,
	begin_year smallint,
	id_speciality integer NOT NULL,
	CONSTRAINT group_pk PRIMARY KEY (name),
	CONSTRAINT nonzero CHECK (begin_year > 1900)
);
-- ddl-end --

-- object: public.lesson | type: TABLE --
-- DROP TABLE IF EXISTS public.lesson CASCADE;
CREATE TABLE public.lesson (
	name_group text NOT NULL,
	group_semester smallint NOT NULL,
	id_standard integer NOT NULL,
	id_lecturer integer NOT NULL,
	CONSTRAINT lesson_pk PRIMARY KEY (group_semester,name_group,id_lecturer,id_standard),
	CONSTRAINT sem_number CHECK (group_semester BETWEEN 1 and 20)
);
-- ddl-end --

-- object: group_fk | type: CONSTRAINT --
-- ALTER TABLE public.lesson DROP CONSTRAINT IF EXISTS group_fk CASCADE;
ALTER TABLE public.lesson ADD CONSTRAINT group_fk FOREIGN KEY (name_group)
REFERENCES public."group" (name) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: public.kind | type: TABLE --
-- DROP TABLE IF EXISTS public.kind CASCADE;
CREATE TABLE public.kind (
	name text NOT NULL,
	CONSTRAINT kind_pk PRIMARY KEY (name)
);
-- ddl-end --

-- object: lecturer_fk | type: CONSTRAINT --
-- ALTER TABLE public.lesson DROP CONSTRAINT IF EXISTS lecturer_fk CASCADE;
ALTER TABLE public.lesson ADD CONSTRAINT lecturer_fk FOREIGN KEY (id_lecturer)
REFERENCES public.lecturer (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: public.standard | type: TABLE --
-- DROP TABLE IF EXISTS public.standard CASCADE;
CREATE TABLE public.standard (
	id integer NOT NULL,
	id_discipline integer NOT NULL,
	name_kind text NOT NULL,
	id_speciality integer NOT NULL,
	study_hours smallint NOT NULL,
	CONSTRAINT standard_pk PRIMARY KEY (id),
	CONSTRAINT nonzero CHECK (study_hours > 0)
);
-- ddl-end --

-- object: kind_fk | type: CONSTRAINT --
-- ALTER TABLE public.standard DROP CONSTRAINT IF EXISTS kind_fk CASCADE;
ALTER TABLE public.standard ADD CONSTRAINT kind_fk FOREIGN KEY (name_kind)
REFERENCES public.kind (name) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: discipline_fk | type: CONSTRAINT --
-- ALTER TABLE public.standard DROP CONSTRAINT IF EXISTS discipline_fk CASCADE;
ALTER TABLE public.standard ADD CONSTRAINT discipline_fk FOREIGN KEY (id_discipline)
REFERENCES public.discipline (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: public.speciality | type: TABLE --
-- DROP TABLE IF EXISTS public.speciality CASCADE;
CREATE TABLE public.speciality (
	id integer NOT NULL,
	name text NOT NULL,
	CONSTRAINT speciality_pk PRIMARY KEY (id)
);
-- ddl-end --

-- object: speciality_fk | type: CONSTRAINT --
-- ALTER TABLE public."group" DROP CONSTRAINT IF EXISTS speciality_fk CASCADE;
ALTER TABLE public."group" ADD CONSTRAINT speciality_fk FOREIGN KEY (id_speciality)
REFERENCES public.speciality (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: speciality_fk | type: CONSTRAINT --
-- ALTER TABLE public.standard DROP CONSTRAINT IF EXISTS speciality_fk CASCADE;
ALTER TABLE public.standard ADD CONSTRAINT speciality_fk FOREIGN KEY (id_speciality)
REFERENCES public.speciality (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: standard_fk | type: CONSTRAINT --
-- ALTER TABLE public.lesson DROP CONSTRAINT IF EXISTS standard_fk CASCADE;
ALTER TABLE public.lesson ADD CONSTRAINT standard_fk FOREIGN KEY (id_standard)
REFERENCES public.standard (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: public.casefold_name | type: FUNCTION --
-- DROP FUNCTION IF EXISTS public.casefold_name() CASCADE;
CREATE FUNCTION public.casefold_name ()
	RETURNS trigger
	LANGUAGE plpgsql
	VOLATILE 
	CALLED ON NULL INPUT
	SECURITY INVOKER
	PARALLEL UNSAFE
	COST 1
	AS $$
BEGIN
  NEW.name := LOWER(TRIM(NEW.name));
  RETURN NEW;
END
$$;
-- ddl-end --

-- object: casefold | type: TRIGGER --
-- DROP TRIGGER IF EXISTS casefold ON public.kind CASCADE;
CREATE TRIGGER casefold
	BEFORE INSERT OR UPDATE
	ON public.kind
	FOR EACH ROW
	EXECUTE PROCEDURE public.casefold_name();
-- ddl-end --

-- object: public.validate_cycle | type: FUNCTION --
-- DROP FUNCTION IF EXISTS public.validate_cycle() CASCADE;
CREATE FUNCTION public.validate_cycle ()
	RETURNS trigger
	LANGUAGE plpgsql
	VOLATILE 
	CALLED ON NULL INPUT
	SECURITY INVOKER
	PARALLEL UNSAFE
	COST 1
	AS $$
DECLARE
  from_group INTEGER;
  from_standard INTEGER;
BEGIN
  SELECT id_speciality
    INTO from_group
    FROM "group"
    WHERE name = NEW.name_group;

  SELECT id_speciality
    INTO from_standard
    FROM standard
    WHERE id = NEW.id_standard;

  IF from_group != from_standard THEN
    RETURN NULL;
  ELSE
    RETURN NEW;
  END IF;
END
$$;
-- ddl-end --

-- object: validate_cycle | type: TRIGGER --
-- DROP TRIGGER IF EXISTS validate_cycle ON public.lesson CASCADE;
CREATE TRIGGER validate_cycle
	BEFORE INSERT OR UPDATE
	ON public.lesson
	FOR EACH STATEMENT
	EXECUTE PROCEDURE public.validate_cycle();
-- ddl-end --


