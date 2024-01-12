package org.example.DAO;

import org.example.databaseConfiguration.DatabaseConnection;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractCrudDAO<T> implements CrudDAO<T> {

    protected DatabaseConnection connection;
    private T toSave;

    public AbstractCrudDAO() {
        this.connection = new DatabaseConnection();
    }

    protected abstract T mapResultSetToEntity(ResultSet rs);

    @Override
    public List<T> findAll() {
        List<T> entityList = new ArrayList<>();

        String sql = "SELECT * FROM " + getTableName() + ";";

        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                T entity = mapResultSetToEntity(resultSet);
                entityList.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entityList;
    }

    @Override
    public T save(T toSave) {
        String sql = "INSERT INTO " + getTableName() + " " + getInsertColumns() + " VALUES " + getInsertValues() + ";";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setInsertParameters(preparedStatement, toSave);

            int rowsAdded = preparedStatement.executeUpdate();

            if (rowsAdded > 0) {
                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        return findById(generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    // Méthode abstraite pour obtenir le nom de la table dans la base de données
    protected abstract String getTableName();

    //Use  reflection for getting dynamicly the columns to insert
    private String getInsertColumns(){
        StringBuilder columns = new StringBuilder("(");

        Field[] fields = toSave.getClass().getDeclaredFields();
            for (Field field : fields){
                columns.append(field.getName()).append(", ");
            }
            columns.delete(columns.length() - 2, columns.length()); //Remove last ','
        columns.append(")");

        return columns.toString();
    };

    // Méthode abstraite pour obtenir les valeurs à insérer dans la requête SQL INSERT
    protected abstract String getInsertValues();

    // Méthode abstraite pour définir les paramètres de la requête SQL INSERT
    protected abstract void setInsertParameters(PreparedStatement preparedStatement, T toSave);

    // Méthode abstraite pour obtenir un enregistrement par son ID
    protected abstract T findById(int id);

    // Méthode abstraite pour obtenir un enregistrement par un identifiant UUID
    protected abstract T findById(UUID id);
}

