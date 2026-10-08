INSERT INTO student_entity (student_name)
VALUES
    ('Anjali'),
    ('Rahul'),
    ('Priya'),
    ('Amit'),
    ('Sneha'),
    ('Vikram'),
    ('Neha'),
    ('Rohan'),
    ('Pooja'),
    ('Karan');

INSERT INTO subject_entity (subject_id, subject_title)
VALUES
    (101, 'Physics'),
    (102, 'Maths'),
    (103, 'English'),
    (104, 'CS'),
    (105, 'Chemistry');

SELECT setval('subject_id_seq', 105, true);


INSERT INTO professor_entity (professor_id,professor_name)
VALUES
    (2001,'Dr. Sharma'),
    (2002,'Dr. Mehta'),
    (2003,'Dr. Kapoor'),
    (2004,'Dr. Verma'),
    (2005,'Dr. Singh'),
    (2006,'Dr. Patel'),
    (2007,'Dr. Joshi'),
    (2008,'Dr. Rao'),
    (2009,'Dr. Malhotra'),
    (2010,'Dr. Iyer');

SELECT setval('professor_seq_id', 2010, true);
