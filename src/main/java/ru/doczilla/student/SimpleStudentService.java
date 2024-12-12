package ru.doczilla.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SimpleStudentService implements StudentService {

    private final Connection connection;

    public SimpleStudentService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Student> getStudentsBatch(Integer limit, Integer offset) throws SQLException {
        String selectStudentsBatchSql =
                "SELECT * FROM \"students\" ORDER BY \"id\" LIMIT ? OFFSET ?;";

        try (PreparedStatement statement = connection.prepareStatement(selectStudentsBatchSql)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Student> students = new ArrayList<>();
                while (resultSet.next()) {
                    students.add(new Student(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("surname"),
                            resultSet.getString("patronymic"),
                            resultSet.getObject("birth_date", LocalDate.class),
                            resultSet.getString("group")
                    ));
                }
                return students;
            }
        }
    }

    @Override
    public Boolean createStudent(Student student) throws SQLException {
        String insertStudentSql =
                "INSERT INTO \"students\" " +
                        "(\"name\", \"surname\", \"patronymic\", \"birth_date\", \"group\") " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertStudentSql)) {
            statement.setString(1, student.name());
            statement.setString(2, student.surname());
            statement.setString(3, student.patronymic());
            statement.setObject(4, student.birthDate());
            statement.setString(5, student.group());
            int insertionResult = statement.executeUpdate();
            return insertionResult > 0;
        }
    }

    @Override
    public Boolean deleteUserById(Long id) throws SQLException {
        String deleteStudentByIdSql = "DELETE FROM \"students\" WHERE \"id\"=? LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(deleteStudentByIdSql)) {
            statement.setLong(1, id);

            int deletionResult = statement.executeUpdate();
            return deletionResult == 1;
        }
    }


}
