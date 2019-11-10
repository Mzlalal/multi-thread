package com.mzlalal.multithread.dao;

import com.mzlalal.multithread.entity.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonDAO extends CrudRepository<Person, Integer> {
}
