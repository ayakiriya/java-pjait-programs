package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository {
    Connection connection;

    private String createTableSql =
            "CREATE TABLE person(" +
                    "id INT GENERATED BY DEFAULT AS IDENTITY," +
                    "name VARCHAR(20)," +
                    "surname VARCHAR(50)," +
                    "age INT" +
                    ")";
    private String insertSql =
            "INSERT INTO person(name, surname, age) " +
                    "VALUES " +
                    "(?, ?, ?)";
    private String deleteSql =
            "DELETE FROM person " +
                    "WHERE id=?";
    private String updateSql =
            "UPDATE person " +
                    "SET (name, surname, age) = (?, ?, ?) " +
                    "WHERE id=?";
    private String selectAllSql =
            "SELECT * FROM person";
    private Statement createTable;
    private PreparedStatement insert;
    private PreparedStatement delete;
    private PreparedStatement update;
    private PreparedStatement selectAll;

    public PersonRepository(){
        try {
            connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/workdb");
            createTable = connection.createStatement();
            insert = connection.prepareStatement(insertSql);
            delete = connection.prepareStatement(deleteSql);
            update = connection.prepareStatement(updateSql);
            selectAll = connection.prepareStatement(selectAllSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable(){
        try {
            ResultSet data = connection.getMetaData().getTables(null, null, null, null);
            boolean tableExists = false;
            while (data.next()){
                if(data.getString("TABLE_NAME").equalsIgnoreCase("person")){
                    tableExists = true;
                    break;
                }
            }
            if(!tableExists){
                createTable.executeUpdate(createTableSql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(Person person) {
        try {
            insert.setString(1, person.getName());
            insert.setString(2, person.getSurname());
            insert.setInt(3, person.getAge());
            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            delete.setInt(1, id);
            delete.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Person person) {
        try {
            update.setString(1, person.getName());
            update.setString(2, person.getSurname());
            update.setInt(3, person.getAge());
            update.setInt(4, person.getId());
            update.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Person> getAll() {
        List<Person> people = new ArrayList<>();

        try {
            ResultSet data = selectAll.executeQuery();
            while (data.next()){
                Person person = new Person();

                person.setId(data.getInt("id"));
                person.setName(data.getString("name"));
                person.setSurname(data.getString("surname"));
                person.setAge(data.getInt("age"));

                people.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return people;
    }
}
