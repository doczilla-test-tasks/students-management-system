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
        String selectAllStudentsSql =
                "SELECT * FROM \"students\" ORDER BY \"id\" LIMIT ? OFFSET ?;";
        PreparedStatement statement = connection.prepareStatement(selectAllStudentsSql);
        statement.setInt(1, limit);
        statement.setInt(2, offset);

        ResultSet resultSet = statement.executeQuery();

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
