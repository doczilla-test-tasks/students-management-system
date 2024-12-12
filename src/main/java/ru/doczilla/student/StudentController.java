package ru.doczilla.student;

import com.google.gson.Gson;
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
                    get(    "",         this::getAllStudents,       gson::toJson);
                    post(   "/:id",     this::createStudentById,    gson::toJson);
                    delete( "/:id",     this::deleteStudentById,    gson::toJson);
                }
        );
    }

    List<Student> getAllStudents(Request request, Response response) {
        response.type("application/json");
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

    Object createStudentById(Request request, Response response) {
        //TODO: implement
        throw new RuntimeException("Not implemented");
    }

    Object deleteStudentById(Request request, Response response) {
        //TODO: implement
        throw new RuntimeException("Not implemented");
    }


}
