package com.animal.adoption.client;

import org.springframework.data.annotation.Id;

public record Dog(@Id int id ,
           String name ,
           String owner,
           String description){}
