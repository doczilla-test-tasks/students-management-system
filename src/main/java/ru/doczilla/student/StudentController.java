package ru.doczilla.student;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.eclipse.jetty.http.HttpStatus;
import ru.doczilla.common.RestController;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.List;

import static spark.Spark.*;

public class StudentController implements RestController {

    private final Gson              gson;
    private final StudentService    studentService;

    public StudentController(Gson gson, StudentService studentService) {
        this.gson = gson;
        this.studentService = studentService;
    }

    @Override
    public void registerEndpoints() {
        path("/students", () -> {
                    get(    "",                 this::getAllStudents,       gson::toJson);
                    post(   "/create",          this::createStudent,        gson::toJson);
                    delete( "/delete/:id",      this::deleteStudentById,    gson::toJson);
                }
        );
    }

    List<Student> getAllStudents(Request request, Response response) {
        int status = HttpStatus.OK_200;
        try {
            Integer limit = Integer.parseInt(request.queryParams("limit"));
            Integer offset = Integer.parseInt(request.queryParams("offset"));
            return studentService.getStudentsBatch(limit, offset);
        } catch (SQLException e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR_500;
            return List.of();
        } catch (NumberFormatException e) {
            status = HttpStatus.BAD_REQUEST_400;
            return List.of();
        } finally {
            response.status(status);
        }
    }

    Boolean createStudent(Request request, Response response) {
        int status = HttpStatus.OK_200;
        try {
            var student = gson.fromJson(request.body(), Student.class);
            if (student.id() != null)
                return false;

            return studentService.createStudent(student);
        } catch (SQLException | JsonSyntaxException e) {
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST_400;
            return false;
        } finally {
            response.status(status);
        }
    }

    Boolean deleteStudentById(Request request, Response response) {
        int status = HttpStatus.OK_200;
        try {
            Long id = Long.parseLong(request.params(":id"));

            return studentService.deleteUserById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST_400;
            return false;
        } finally {
            response.status(status);
        }
    }


}
