-- Create the hospital database
CREATE DATABASE IF NOT EXISTS hospital;

-- Use the hospital database
USE hospital;

-- Create the patients table
CREATE TABLE patients (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10) NOT NULL
);

-- Create the doctors table
CREATE TABLE doctors (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL
);

-- Create the appointments table
CREATE TABLE appointments (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    patient_id INT UNSIGNED NOT NULL,
    doctor_id INT UNSIGNED NOT NULL,
    appointment_date DATE NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
);

-- Insert sample data for testing
INSERT INTO patients (name, age, gender) VALUES
('John Doe', 30, 'Male'),
('Jane Smith', 25, 'Female');

INSERT INTO doctors (name, specialization) VALUES
('Dr. Alice Brown', 'Cardiology'),
('Dr. Bob Wilson', 'Neurology');