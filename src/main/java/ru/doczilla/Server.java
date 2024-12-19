package ru.doczilla;

import com.google.gson.GsonBuilder;
import ru.doczilla.util.DatabaseAccessor;
import ru.doczilla.util.DatabaseAccessor.DatabaseAccessorBuilder;
import ru.doczilla.common.RestController;
import ru.doczilla.student.SimpleStudentService;
import ru.doczilla.student.StudentController;
import ru.doczilla.util.Filters;
import ru.doczilla.adapter.LocalDateAdapter;

import javax.crypto.spec.PSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.Set;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        DatabaseAccessor databaseAccessor;
        try {
              databaseAccessor = new DatabaseAccessorBuilder()
                      .withUrl("jdbc:h2:file:./db/dev")
                      .withUsername(System.getenv("DB_USERNAME"))
                      .withPassword(System.getenv("DB_PASSWORD"))
                      .build();

            var gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();
            var studentService = new SimpleStudentService(databaseAccessor.getConnection());


            staticFileLocation("static");

            before("api/*", Filters::jsonContentType);
            path("api/v1", () -> {
                Set<RestController> controllers = Set.of(
                        new StudentController(gson, studentService)
                );

                controllers.forEach(RestController::registerEndpoints);
            });

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("\nConnection to database closed");
                    databaseAccessor.getConnection().close();
                } catch (SQLException e) {
                    //TODO: replace with more reliable logging (with logback or log4j)
                    System.out.println(e.getMessage());
                }
            }));
        } catch (Exception e) {
            //TODO: replace with more reliable logging (with logback or log4j)
            System.out.println(e.getMessage());
        }
    }
}