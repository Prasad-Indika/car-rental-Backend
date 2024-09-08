package lk.carrental.CarRental.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String contact;
    private String nic;
    private String address;
    private String dateTime;
    private String userName;
    private String password;

    public Customer(String firstName, String lastName, String userName, String secondDateAsString, String encodedPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateTime = secondDateAsString;
        this.userName = userName;
        this.password = encodedPassword;
    }
}
