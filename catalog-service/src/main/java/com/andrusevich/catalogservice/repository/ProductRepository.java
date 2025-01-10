package com.andrusevich.catalogservice.repository;

import com.andrusevich.catalogservice.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query(value = "select p from Product p where p.name like :filter")
    Iterable<Product> findAllByNameLikeIgnoreCase(@Param("filter") String filter);
}
