package com.example.studentmanagmentrest.key.model;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SecredKey {

    @Id
    Long id;
    String key;



}
