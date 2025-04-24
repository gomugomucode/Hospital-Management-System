#Hospital Management System
A Java Swing desktop application for managing hospital operations, including patient records, doctor information, and appointment scheduling. The application uses a MySQL database (via XAMPP) for data storage and provides a user-friendly GUI built with Java Swing.

#Features

Add Patient: Register new patients with name, age, and gender.
View Patients: Display a table of all patients in the database.
View Doctors: List all doctors with their names and specializations.
Book Appointment: Schedule appointments by selecting a patient, doctor, and date, with availability checks.
Modern UI: Large, styled buttons with hover effects and a clean layout for a modern aesthetic.

#Prerequisites

Operating System: Arch Linux (or any Linux distribution; instructions are Arch-specific).
Java: OpenJDK 17 or 23 (17 recommended for stability).
MySQL: XAMPP for Linux (provides MySQL/MariaDB).
IDE: Visual Studio Code with Java Extension Pack (optional; terminal commands provided).
Graphical Environment: X11 or Wayland (e.g., XFCE, i3) for rendering the Swing GUI.
MySQL Connector: mysql-connector-j-9.3.0.jar (included in lib/ directory).

#Installation
1. Clone or Download the Project

Clone the repository or download the project files to your local machine:git clone <repository-url> ~/Hospital-Management-System

Or copy the project folder to ~/Hospital-Management-System.

2. Install Java

Install OpenJDK 17:sudo pacman -S jdk17-openjdk
sudo archlinux-java set java-17-openjdk


Verify Java version:java -version

Expected output: openjdk 17.x.x.

3. Install XAMPP

Download and install XAMPP for Linux from https://www.apachefriends.org.
Move the installer to /opt and install:sudo chmod +x xampp-linux-x64-8.2.12-0-installer.run
sudo ./xampp-linux-x64-8.2.12-0-installer.run


Start XAMPP:sudo /opt/lampp/lampp start



4. Set Up MySQL Database

Access MySQL:/opt/lampp/bin/mysql -u root -p

(Default password is empty unless changed.)
Create the hospital database:CREATE DATABASE hospital;


Create required tables:USE hospital;
CREATE TABLE patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    age INT,
    gender VARCHAR(10)
);
CREATE TABLE doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    specialization VARCHAR(255)
);
CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT,
    doctor_id INT,
    appointment_date DATE
);



5. Set Up Graphical Environment

The application requires a graphical environment (X11 or Wayland) to display the Swing GUI.
Check current session:echo $XDG_SESSION_TYPE

If output is tty, install a desktop environment (e.g., XFCE):sudo pacman -S xfce4 xfce4-goodies lightdm
sudo systemctl enable lightdm
sudo systemctl start lightdm


Alternatively, use a window manager (e.g., i3):sudo pacman -S i3 xorg xorg-xinit
echo "exec i3" > ~/.xinitrc
startx


Verify graphical environment:echo $XDG_SESSION_TYPE

Expected output: x11 or wayland.
Install X11 utilities:sudo pacman -S xorg-xhost
xhost +local:



6. Install Swing Dependencies

Ensure libraries for Java Swing/AWT are installed:sudo pacman -S libxrender libxtst libxi



Running the Application

Navigate to the project directory:cd ~/Hospital-Management-System


Compile the Java files:javac -cp ".:lib/mysql-connector-j-9.3.0.jar" src/HospitalManagementSystem/*.java


Run the application:java -cp ".:lib/mysql-connector-j-9.3.0.jar:src" HospitalManagementSystem.HospitalManagementSystem


The GUI should open in a new window with the following options:
Add Patient
View Patients
View Doctors
Book Appointment
Exit



Usage

Add Patient: Enter patient details (name, age, gender) to add them to the database.
View Patients: Displays a table of all patients.
View Doctors: Shows a list of doctors and their specializations.
Book Appointment: Input patient ID, doctor ID, and date (YYYY-MM-DD) to schedule an appointment. Checks doctor availability.
Exit: Closes the application and database connection.

#Project Structure

Hospital-Management-System/
├── lib/
│   └── mysql-connector-j-9.3.0.jar
├── src/
│   └── HospitalManagementSystem/
│       ├── Doctor.java
│       ├── Patient.java
│       ├── HospitalManagementSystem.java
├── Hospital-Management-System.iml
└── README.md

Troubleshooting

GUI Doesn’t Display:
Ensure you’re in a graphical environment (XDG_SESSION_TYPE is x11 or wayland).
Test with a minimal Swing program:import javax.swing.*;
public class TestFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Window");
            frame.setSize(400, 300);
            frame.setVisible(true);
        });
    }
}

Compile and run: javac TestFrame.java && java TestFrame.
If using Wayland, try:export _JAVA_AWT_WM_NONREPARENTING=1
java -cp ".:lib/mysql-connector-j-9.3.0.jar:src" HospitalManagementSystem.HospitalManagementSystem


Downgrade to OpenJDK 17 if issues persist:sudo pacman -S jdk17-openjdk
sudo archlinux-java set java-17-openjdk




Database Connection Fails:
Verify XAMPP’s MySQL is running:sudo /opt/lampp/lampp restart
/opt/lampp/bin/mysql -u root -p


Check MySQL logs:cat /opt/lampp/logs/mysql.log


Ensure hospital database and tables exist.


Buttons Look Incorrect:
Install modern fonts:sudo pacman -S ttf-dejavu





Future Improvements

Add data validation for appointment dates.
Implement user authentication.
Enhance UI with icons and more styling.
Migrate to a web-based application using Spring Boot for browser access.
Add export functionality for patient/doctor data.

License
This project is open-source and available under the MIT License.

