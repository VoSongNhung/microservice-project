package com.example.productservice.repository;

import com.example.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product,String> {
//    @Query("{ 'code' : ?0}")
//    List<Product> findProductByCode(String code);

//    Optional<Product> findByCode();
    //List<Product> findByIdIn(List<String> idProduct);
    @Query("{'_id': { $in: ?0 }}")
    List<Product> findByIdIn(List<String> idList);
}
