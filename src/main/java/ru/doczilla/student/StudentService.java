package ru.doczilla.student;

import java.sql.SQLException;
import java.util.List;

public interface StudentService {
    List<Student> getStudentsBatch(Integer limit, Integer offset) throws SQLException;
}