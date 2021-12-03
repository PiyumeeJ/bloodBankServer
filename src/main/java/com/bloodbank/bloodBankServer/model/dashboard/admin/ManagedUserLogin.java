package com.bloodbank.bloodBankServer.model.dashboard.admin;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler"})
public class ManagedUserLogin implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String password;
    private String role;
    private String activeStatus;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @PrimaryKeyJoinColumn(name = "managedUsers_id", referencedColumnName = "id")
    private ManagedUser managedUsers;

    public ManagedUserLogin(String userName, String password, String role, String activeStatus, ManagedUser managedUsers) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.activeStatus = activeStatus;
        this.managedUsers = managedUsers;
    }
}
