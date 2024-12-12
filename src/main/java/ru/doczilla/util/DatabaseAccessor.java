package ru.doczilla.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseAccessor {
    private final Connection connection;

    private DatabaseAccessor(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public static class DatabaseAccessorBuilder {
        private String url;
        private String username;
        private String password;

        public DatabaseAccessorBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public DatabaseAccessorBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public DatabaseAccessorBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public DatabaseAccessor build() throws SQLException {
            Connection connection = DriverManager.getConnection(url, username, password);
            return new DatabaseAccessor(connection);
        }
    }
}
