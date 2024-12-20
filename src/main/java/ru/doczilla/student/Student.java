package ru.doczilla.student;

import java.time.LocalDate;

public record Student(
        Long            id,
        String          name,
        String          surname,
        String          patronymic,
        //TODO: think about geographically spread application (maybe store ZonedDateTime with properly set TimeZone)
        LocalDate       birthDate,
        String          group
) {
    public Student(
            String      name,
            String      surname,
            String      patronymic,
            LocalDate   birthDate,
            String      group
    ) {
        this(null, name, surname, patronymic, birthDate, group);
    }
}