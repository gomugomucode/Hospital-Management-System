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
        System.out.println("Starting Hospital Management System...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC Driver loaded.");
            con = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected.");
            patient = new Patient(con);
            doctor = new Doctor(con);
            System.out.println("Patient and Doctor instances created.");
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage() + "\nProceeding with limited functionality.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("Scheduling GUI creation...");
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        System.out.println("Creating GUI...");
        JFrame frame = new JFrame("Hospital Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(600, 400));

        // Main panel with modern styling
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        mainPanel.setBackground(new Color(245, 245, 245)); // Light gray background

        // Title label
        JLabel titleLabel = new JLabel("Hospital Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 150, 243)); // Blue color
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Buttons with modern styling
        ModernButton addPatientButton = new ModernButton("Add Patient");
        ModernButton viewPatientsButton = new ModernButton("View Patients");
        ModernButton viewDoctorsButton = new ModernButton("View Doctors");
        ModernButton bookAppointmentButton = new ModernButton("Book Appointment");
        ModernButton exitButton = new ModernButton("Exit");

        // Add buttons to panel with spacing
        mainPanel.add(addPatientButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(viewPatientsButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(viewDoctorsButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(bookAppointmentButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(exitButton);

        // Add main panel to frame
        frame.add(mainPanel);
        System.out.println("GUI components added.");

        // Button action listeners
        addPatientButton.addActionListener(e -> showAddPatientDialog());
        viewPatientsButton.addActionListener(e -> showViewPatientsDialog());
        viewDoctorsButton.addActionListener(e -> showViewDoctorsDialog());
        bookAppointmentButton.addActionListener(e -> showBookAppointmentDialog());
        exitButton.addActionListener(e -> {
            try {
                if (con != null) {
                    con.close();
                    System.out.println("Database connection closed.");
                }
            } catch (SQLException ex) {
                System.err.println("Error closing database: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, "Error closing database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        });

        // Show frame
        frame.setVisible(true);
        frame.repaint();
        frame.revalidate();
        System.out.println("GUI set visible. Is visible? " + frame.isVisible());
        System.out.println("Frame size: " + frame.getSize());
        System.out.println("Frame location: " + frame.getLocation());
    }

    // Custom button class for modern styling
    static class ModernButton extends JButton {
        public ModernButton(String text) {
            super(text);
            setPreferredSize(new Dimension(250, 50)); // Larger button size
            setMaximumSize(new Dimension(250, 50));
            setFont(new Font("Segoe UI", Font.PLAIN, 16));
            setForeground(Color.WHITE);
            setBackground(new Color(33, 150, 243)); // Blue background
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(true);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Rounded corners
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                @Override
                protected void paintButtonPressed(Graphics g, AbstractButton b) {
                    g.setColor(new Color(25, 118, 210)); // Darker blue when pressed
                    g.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 15, 15);
                }

                @Override
                public void paint(Graphics g, JComponent c) {
                    AbstractButton b = (AbstractButton) c;
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (b.getModel().isPressed()) {
                        g2.setColor(new Color(25, 118, 210));
                    } else if (b.getModel().isRollover()) {
                        g2.setColor(new Color(66, 165, 245)); // Lighter blue on hover
                    } else {
                        g2.setColor(b.getBackground());
                    }
                    g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 15, 15);
                    g2.dispose();
                    super.paint(g, c);
                }
            });

            // Hover effect
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(new Color(66, 165, 245));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(new Color(33, 150, 243));
                }
            });
        }
    }

    private static void showAddPatientDialog() {
        System.out.println("Opening Add Patient dialog...");
        JDialog dialog = new JDialog((Frame) null, "Add Patient", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);
        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField(20);
        JLabel genderLabel = new JLabel("Gender:");
        JTextField genderField = new JTextField(20);
        JButton submitButton = new JButton("Submit");

        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(ageLabel);
        dialog.add(ageField);
        dialog.add(genderLabel);
        dialog.add(genderField);
        dialog.add(new JLabel());
        dialog.add(submitButton);

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
        System.out.println("Add Patient dialog opened.");
    }

    private static void showViewPatientsDialog() {
        System.out.println("Opening View Patients dialog...");
        JDialog dialog = new JDialog((Frame) null, "View Patients", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(null);

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
            System.err.println("Database error in viewPatients: " + ex.getMessage());
            JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dialog.dispose();
            return;
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        dialog.add(closeButton, BorderLayout.SOUTH);

        dialog.setVisible(true);
        System.out.println("View Patients dialog opened.");
    }

    private static void showViewDoctorsDialog() {
        System.out.println("Opening View Doctors dialog...");
        JDialog dialog = new JDialog((Frame) null, "View Doctors", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(null);

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
            System.err.println("Database error in viewDoctors: " + ex.getMessage());
            JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dialog.dispose();
            return;
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        dialog.add(closeButton, BorderLayout.SOUTH);

        dialog.setVisible(true);
        System.out.println("View Doctors dialog opened.");
    }

    private static void showBookAppointmentDialog() {
        System.out.println("Opening Book Appointment dialog...");
        JDialog dialog = new JDialog((Frame) null, "Book Appointment", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel patientIdLabel = new JLabel("Patient ID:");
        JTextField patientIdField = new JTextField(20);
        JLabel doctorIdLabel = new JLabel("Doctor ID:");
        JTextField doctorIdField = new JTextField(20);
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField(20);
        JButton submitButton = new JButton("Submit");

        dialog.add(patientIdLabel);
        dialog.add(patientIdField);
        dialog.add(doctorIdLabel);
        dialog.add(doctorIdField);
        dialog.add(dateLabel);
        dialog.add(dateField);
        dialog.add(new JLabel());
        dialog.add(submitButton);

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
                System.err.println("Database error in bookAppointment: " + ex.getMessage());
                JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
        System.out.println("Book Appointment dialog opened.");
    }
}