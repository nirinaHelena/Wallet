package org.example.repository;

import org.example.model.Account;
import org.example.model.Device;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CruDevice implements CrudOperationInterface<Device> {
    private Connection connection;
    @Override
    public List<Device> findAll() {
        List<Device> deviceList= new ArrayList<>();
        String sql= "select * from device";
        try (PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            ResultSet resultSet= preparedStatement.executeQuery();
            while (resultSet.next()){
                deviceList.add(new Device(
                        resultSet.getString("device"),
                        resultSet.getString("country")
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return deviceList;
    }

    @Override
    public List<Device> saveAll(List<Device> toSave) {
        List<Device> deviceList= new ArrayList<>();
        for (Device device: toSave){
            Device savedDevice= save(device);
            if (savedDevice != null){
                deviceList.add(savedDevice);
            }
        }
        return deviceList;
    }

    @Override
    public Device save(Device toSave) {
       String sql= "insert into device (device, country) values(?, ?) ;" ;
       try (PreparedStatement preparedStatement= connection.prepareStatement(sql)){
           preparedStatement.setString(1, toSave.getDevice());
           preparedStatement.setString(2, toSave.getCountry());
       }catch (SQLException e){
           e.printStackTrace();
       }
       return null;
    }
}
