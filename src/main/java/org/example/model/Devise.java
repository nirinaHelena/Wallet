package org.example.model;

<<<<<<< HEAD:src/main/java/org/example/model/Devise.java
public class Devise {
=======
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Device {
    private String device;
    private String country;
>>>>>>> 08c0a42 (add model device):src/main/java/org/example/model/Device.java
}
