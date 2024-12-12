package ru.doczilla.util;

import spark.Request;
import spark.Response;

public class Filters {
    public static void jsonContentType(Request request, Response response) {
        response.type("application/json");
    }
}
