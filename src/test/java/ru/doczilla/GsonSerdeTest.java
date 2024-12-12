package ru.doczilla;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import ru.doczilla.student.Student;
import ru.doczilla.adapter.LocalDateAdapter;

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GsonSerdeTest {

    @Test
    void serializationTest() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        List<Student> students = List.of(
                new Student(1L, "John", "Doe", null, LocalDate.now(), "P0000"),
                new Student(2L, "Jane", "Doe", null, LocalDate.now(), "P9999")
        );

        String gsonEncodedRes = gson.toJson(students);
        System.out.println(gsonEncodedRes);
        assertNotNull(gsonEncodedRes);
    }

}
