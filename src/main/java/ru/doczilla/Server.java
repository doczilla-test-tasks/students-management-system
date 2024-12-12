package ru.doczilla;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.doczilla.common.RestController;
import ru.doczilla.student.SimpleStudentService;
import ru.doczilla.student.StudentController;
import ru.doczilla.student.StudentService;
import ru.doczilla.util.Filters;
import ru.doczilla.adapter.LocalDateAdapter;

import java.sql.*;
import java.time.LocalDate;
import java.util.Set;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        try {
            staticFileLocation("static");

            DriverManager.registerDriver(new org.h2.Driver());
            Connection connection = DriverManager.getConnection(
                    "jdbc:h2:file:./db/dev",
                    System.getenv("DB_USERNAME"),
                    System.getenv("DB_PASSWORD")
            );

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();
            StudentService studentService = new SimpleStudentService(connection);

            path("api/v1", () -> {
                before(Filters::jsonContentType);
                Set<RestController> controllers = Set.of(
                        new StudentController(gson, studentService)
                );

                controllers.forEach(RestController::registerEndpoints);
            });
        } catch (Exception e) {
            //TODO: replace with more reliable logging (with logback or log4j)
            e.printStackTrace();
            System.out.println("SQLException caught");
        }
    }
}