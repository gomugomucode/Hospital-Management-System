package HospitalManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "";
    private static Connection con;
    private static Patient patient;
    private static Doctor doctor;

    public static void main(String[] args) {
        // Initialize database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
            patient = new Patient(con);
            doctor = new Doctor(con);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Create main GUI window
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        // Main frame
        JFrame frame = new JFrame("Hospital Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null); // Center window

        // Main panel with buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Buttons for each feature
        JButton addPatientButton = new JButton("Add Patient");
        JButton viewPatientsButton = new JButton("View Patients");
        JButton viewDoctorsButton = new JButton("View Doctors");
        JButton bookAppointmentButton = new JButton("Book Appointment");
        JButton exitButton = new JButton("Exit");

        // Add buttons to panel
        panel.add(addPatientButton);
        panel.add(viewPatientsButton);
        panel.add(viewDoctorsButton);
        panel.add(bookAppointmentButton);
        panel.add(exitButton);

        // Add panel to frame
        frame.add(panel);

        // Button action listeners
        addPatientButton.addActionListener(e -> showAddPatientDialog());
        viewPatientsButton.addActionListener(e -> showViewPatientsDialog());
        viewDoctorsButton.addActionListener(e -> showViewDoctorsDialog());
        bookAppointmentButton.addActionListener(e -> showBookAppointmentDialog());
        exitButton.addActionListener(e -> {
            try {
                con.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error closing database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        });

        // Show frame
        frame.setVisible(true);
    }

    private static void showAddPatientDialog() {
        JDialog dialog = new JDialog((Frame) null, "Add Patient", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));

        // Input fields
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);
        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField(20);
        JLabel genderLabel = new JLabel("Gender:");
        JTextField genderField = new JTextField(20);
        JButton submitButton = new JButton("Submit");

        // Add components
        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(ageLabel);
        dialog.add(ageField);
        dialog.add(genderLabel);
        dialog.add(genderField);
        dialog.add(new JLabel()); // Empty cell
        dialog.add(submitButton);

        // Submit action
        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();
            String gender = genderField.getText().trim();

            if (name.isEmpty() || ageText.isEmpty() || gender.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int age = Integer.parseInt(ageText);
                patient.addPatient(name, age, gender, con);
                JOptionPane.showMessageDialog(dialog, "Patient added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Age must be a number", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private static void showViewPatientsDialog() {
        JDialog dialog = new JDialog((Frame) null, "View Patients", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(null);

        // Table for patients
        Vector<String> columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add("Name");
        columnNames.add("Age");
        columnNames.add("Gender");

        Vector<Vector<Object>> data = new Vector<>();
        try {
            ResultSet rs = patient.viewPatients(con);
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getInt("age"));
                row.add(rs.getString("gender"));
                data.add(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dialog.dispose();
            return;
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        dialog.add(closeButton, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private static void showViewDoctorsDialog() {
        JDialog dialog = new JDialog((Frame) null, "View Doctors", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(null);

        // Table for doctors
        Vector<String> columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add("Name");
        columnNames.add("Specialization");

        Vector<Vector<Object>> data = new Vector<>();
        try {
            ResultSet rs = doctor.viewDoctors(con);
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("specialization"));
                data.add(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dialog.dispose();
            return;
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        dialog.add(closeButton, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private static void showBookAppointmentDialog() {
        JDialog dialog = new JDialog((Frame) null, "Book Appointment", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));

        // Input fields
        JLabel patientIdLabel = new JLabel("Patient ID:");
        JTextField patientIdField = new JTextField(20);
        JLabel doctorIdLabel = new JLabel("Doctor ID:");
        JTextField doctorIdField = new JTextField(20);
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField(20);
        JButton submitButton = new JButton("Submit");

        // Add components
        dialog.add(patientIdLabel);
        dialog.add(patientIdField);
        dialog.add(doctorIdLabel);
        dialog.add(doctorIdField);
        dialog.add(dateLabel);
        dialog.add(dateField);
        dialog.add(new JLabel());
        dialog.add(submitButton);

        // Submit action
        submitButton.addActionListener(e -> {
            String patientIdText = patientIdField.getText().trim();
            String doctorIdText = doctorIdField.getText().trim();
            String date = dateField.getText().trim();

            if (patientIdText.isEmpty() || doctorIdText.isEmpty() || date.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int patientId = Integer.parseInt(patientIdText);
                int doctorId = Integer.parseInt(doctorIdText);
                if (!patient.getPatientById(patientId, con)) {
                    JOptionPane.showMessageDialog(dialog, "Invalid Patient ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!doctor.getDoctorById(doctorId, con)) {
                    JOptionPane.showMessageDialog(dialog, "Invalid Doctor ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (doctor.checkDoctorAvailability(doctorId, date, con)) {
                    doctor.bookAppointment(patientId, doctorId, date, con);
                    JOptionPane.showMessageDialog(dialog, "Appointment booked successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Doctor not available on this date", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Patient ID and Doctor ID must be numbers", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }
}