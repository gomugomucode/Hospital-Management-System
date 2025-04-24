# Hospital Management System

A Java Swing desktop application for managing hospital operations, including patient records, doctor information, and appointment scheduling. The application uses a MySQL database (via XAMPP) for data storage and provides a user-friendly GUI built with Java Swing.

## Features
- **Add Patient**: Register new patients with name, age, and gender.
- **View Patients**: Display a table of all patients in the database.
- **View Doctors**: List all doctors with their names and specializations.
- **Book Appointment**: Schedule appointments by selecting a patient, doctor, and date, with availability checks.
- **Modern UI**: Large, styled buttons with hover effects and a clean layout for a modern aesthetic.

## Prerequisites
- **Operating System**: Arch Linux (or any Linux distribution; instructions are Arch-specific).
- **Java**: OpenJDK 17 or 23 (17 recommended for stability).
- **MySQL**: XAMPP for Linux (provides MySQL/MariaDB).
- **IDE**: Visual Studio Code with Java Extension Pack (optional; terminal commands provided).
- **Graphical Environment**: X11 or Wayland (e.g., XFCE, i3) for rendering the Swing GUI.
- **MySQL Connector**: `mysql-connector-j-9.3.0.jar` (included in `lib/` directory).

## Installation

### 1. Clone or Download the Project
- Clone the repository or download the project files to your local machine:
  ```bash
  git clone <repository-url> ~/Hospital-Management-System
