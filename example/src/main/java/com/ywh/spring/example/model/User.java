package com.ywh.spring.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ywh
 * @since 4/20/2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private long id;

    private String name;
}
