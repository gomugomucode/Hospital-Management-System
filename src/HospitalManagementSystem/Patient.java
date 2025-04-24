package HospitalManagementSystem;

import java.sql.*;

public class Patient {
    private Connection connection;

    public Patient(Connection connection) {
        this.connection = connection;
    }

    public boolean addPatient(String name, int age, String gender, Connection con) throws SQLException {
        String query = "INSERT INTO patients (name, age, gender) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        }
    }

    public ResultSet viewPatients(Connection con) throws SQLException {
        String query = "SELECT * FROM patients";
        Statement statement = con.createStatement();
        return statement.executeQuery(query);
    }

    public boolean getPatientById(int id, Connection con) {
        String query = "SELECT * FROM patients WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
