package org.example.ExplicationMahefa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class AutoCrudOP<T>  implements crudOP<T> {
    private Connection connection;
  //sarotra
    private static String Find_all_querry = "select {colums} from {table}";
    public List<T> findAll(){
        /*
        Obtenir  tous les attributs (utiles) de la class T
        Obtenir le nom de la table a T
         */
        List<Object> attributs;
        String tableName;

        /*return Find_all_querry
                .replace("{colums}", {attributs })
                .replace("{table}", {tableName})
    */
    }

    void map(){
        ResultSet rs;

        rs.
    }
     //tsotra
    public List<?> findAll(){

    }
}
