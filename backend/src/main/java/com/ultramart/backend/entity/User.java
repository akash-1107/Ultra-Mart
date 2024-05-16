package com.ultramart.backend.entity;
import com.ultramart.backend.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "password")
    private String password;
    
    @Column(name="name")
    private String name;

    @Column(name = "role")
    private UserRole role;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;    
    
}
    
   

