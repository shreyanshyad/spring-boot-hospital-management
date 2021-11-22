drop table if exists doctor;
drop table if exists patient;
drop table if exists patient;

# ROLE_DOCTOR
create table doctor
(
    first_name      VARCHAR(100),
    middle_name     VARCHAR(100),
    last_name       VARCHAR(100),
    address         VARCHAR(100),
    qualifications  VARCHAR(300),
    specializations VARCHAR(300),
    phone_no        VARCHAR(12),
    email           VARCHAR(320),
    id              int primary key,
    foreign key (id) references users (id) on delete cascade
);

# ROLE_PATIENT
create table patient
(
    first_name  VARCHAR(100),
    middle_name VARCHAR(100),
    last_name   VARCHAR(100),
    address     VARCHAR(100),
    age         INT,
    gender      VARCHAR(10) CHECK (
                gender = 'Male' OR
                gender = 'Female' OR
                gender = 'Other'
        ),
    id          int primary key,
    dob         date,
    foreign key (id) references users (id) on delete cascade
);

create table nurse
(
    first_name  VARCHAR(100),
    middle_name VARCHAR(100),
    last_name   VARCHAR(100),
    address     VARCHAR(100),
    phone_no    VARCHAR(12),
    email       VARCHAR(320),
    designation VARCHAR(40),
    id          int primary key,
    foreign key (id) references users (id) on delete cascade
);

create table admin
(
    first_name  VARCHAR(100),
    middle_name VARCHAR(100),
    last_name   VARCHAR(100),
    address     VARCHAR(100),
    phone_no    VARCHAR(12),
    email       VARCHAR(320),
    designation VARCHAR(40),
    id          int primary key,
    foreign key (id) references users (id) on delete cascade
);

create table chemist
(
    first_name  VARCHAR(100),
    middle_name VARCHAR(100),
    last_name   VARCHAR(100),
    address     VARCHAR(100),
    phone_no    VARCHAR(12),
    email       VARCHAR(320),
    designation VARCHAR(40),
    id          int primary key,
    foreign key (id) references users (id) on delete cascade
);

create table lab_staff
(
    first_name  VARCHAR(100),
    middle_name VARCHAR(100),
    last_name   VARCHAR(100),
    address     VARCHAR(100),
    phone_no    VARCHAR(12),
    email       VARCHAR(320),
    designation VARCHAR(40),
    id          int primary key,
    foreign key (id) references users (id) on delete cascade
);

drop table if exists appointments;

# patient doctor relations

create table appointments
(
    appointment_id int primary key not null auto_increment,
    patient_id     int,
    doctor_id      int,
    time           timestamp       null,
    problem        varchar(200),
    confirmed      boolean,
    foreign key (patient_id) references patient (id) on delete cascade
);

create table treatment
(
    treatment_id int primary key not null auto_increment,
    patient_id   int,
    doctor_id    int,
    start_time   datetime,
    end_time     datetime,
    problem      VARCHAR(300),
    treatment    VARCHAR(300),
    foreign key (patient_id) references patient (id) on delete cascade
);

create table medicine
(
    medicine_id       int primary key not null auto_increment,
    name              VARCHAR(100),
    cost              INT,
    quantity_in_stock INT
);

create table ward
(
    ward_id             int primary key auto_increment not null,
    ward_name           VARCHAR(100),
    equipment_available VARCHAR(300),
    cost_per_day        int
);

create table room
(
    room_id int primary key auto_increment not null,
    room_no int,
    ward_id int,
    filled  boolean                        not null default false,
    foreign key (ward_id) references ward (ward_id) on delete cascade
);


create table prescription
(
    prescription_id int primary key auto_increment not null,
    treatment_id    int,
    foreign key (treatment_id) references treatment (treatment_id) on delete cascade,
    medicine_id     int,
    foreign key (medicine_id) references medicine (medicine_id) on delete cascade,
    dosage          VARCHAR(300)
);

create table admission
(
    admission_id   int primary key not null auto_increment,
    admission_time timestamp       null,
    discharge_time timestamp       null,
    treatment_id   int,
    foreign key (treatment_id) references treatment (treatment_id) on delete cascade,
    room_id        int,
    foreign key (room_id) references room (room_id) on delete cascade
);

# lab test related tables
create table test
(
    test_id     int primary key auto_increment not null,
    patient_id  int,
    foreign key (patient_id) references patient (id) on delete cascade,
    time        timestamp                      null,
    name        VARCHAR(100),
    result      VARCHAR(300),
    report_file VARCHAR(100)
);

# bill related tables
create table bill
(
    bill_id     int primary key key auto_increment not null,
    patient_id  int,
    foreign key (patient_id) references patient (id) on delete cascade,
    amount      int,
    amount_paid int,
    time        datetime
);

create table bill_cost
(
    bill_cost_id int auto_increment not null primary key,
    bill_id      int,
    foreign key (bill_id) references bill (bill_id) on delete cascade,
    name         VARCHAR(100),
    quantity     int,
    cost         int
);

create view treatment_details as
select t.treatment_id,
       t.patient_id,
       t.doctor_id,
       t.problem,
       t.treatment,
       t.start_time,
       t.end_time,
       p.first_name as pfname,
       p.last_name  as plname,
       d.first_name as dfname,
       d.last_name  as dlname
from treatment t,
     doctor d,
     patient p
where t.patient_id = p.id
  and t.doctor_id = d.id;

create view admission_details as
select a.admission_time,
       a.discharge_time,
       t.problem,
       t.treatment,
       r.room_no,
       w.ward_name,
       t.pfname,
       t.plname,
       t.dfname,
       t.dlname
from admission a,
     room r,
     ward w,
     treatment_details t
where a.treatment_id = t.treatment_id
  and a.room_id = r.room_id
  and w.ward_id = r.ward_id;

create view ward_details as
select r.room_id, w.ward_name, w.equipment_available, w.cost_per_day, r.room_no, r.filled
from ward w,
     room r
where r.ward_id = w.ward_id;
