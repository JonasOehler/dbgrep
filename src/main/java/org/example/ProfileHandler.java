package org.example;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ProfileHandler {
    private final DatabaseConfig config;
    private URLClassLoader loader;
    public ProfileHandler(DatabaseConfig config){
        this.config = config;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword()
        );
    }

    public void registerDriver() throws Exception{
        if (loader != null) return;

        File jarFile = new File(config.getDriverFile());
        URL jarUrl = jarFile.toURI().toURL();
        loader = new URLClassLoader(new URL[]{jarUrl}, Thread.currentThread().getContextClassLoader());

        Class<?> driverClass = Class.forName(config.getDriverClass(), true, loader);
        Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();

        DriverManager.registerDriver(new DriverShim(driver));
    }
}
