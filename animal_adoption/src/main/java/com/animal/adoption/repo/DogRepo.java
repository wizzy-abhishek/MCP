package com.animal.adoption.repo;

import com.animal.adoption.client.Dog;
import org.springframework.data.repository.ListCrudRepository;

public interface DogRepo extends ListCrudRepository<Dog,Integer > {

}
