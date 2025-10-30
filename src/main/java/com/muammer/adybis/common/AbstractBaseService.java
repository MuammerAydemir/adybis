package com.muammer.adybis.common;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.muammer.adybis.base.exceptions.InvalidResourceException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

public abstract class AbstractBaseService<E, RQ, RS, ID> {

    protected final JpaRepository<E, ID> repository;
    protected final BaseMapper<E, RQ, RS> mapper;
    private final String entityName;

    public AbstractBaseService(JpaRepository<E, ID> repository, BaseMapper<E, RQ, RS> mapper, Class<E> entityClass) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityName = entityClass.getSimpleName();
    }

    @Transactional
    public E save(E entity) {
        requireNonNullOrEmpty(entity);
        return repository.save(entity);
    }

    @Transactional
    public List<E> saveAll(List<E> entity) {
        requireNonNullOrEmpty(entity);
        return repository.saveAll(entity);
    }

    @Transactional
    public RS saveAndReturnDto(RQ request) {
        requireNonNullOrEmpty(request);
        E entity = mapper.toEntity(request);
        return mapper.toDto(save(entity));
    }

    @Transactional
    public E saveAndReturnEntity(RQ request) {
        requireNonNullOrEmpty(request);
        E entity = mapper.toEntity(request);
        return save(entity);
    }

    @Transactional
    public List<RS> saveAllAndReturnDto(List<RQ> request) {
        requireNonNullOrEmpty(request);
        List<E> entity = mapper.toEntityList(request);
        return mapper.toDtoList(saveAll(entity));
    }

    @Transactional
    public List<RS> findAllAndReturnDtos() {
        return mapper.toDtoList(repository.findAll());
    }

    @Transactional
    public List<E> findAll() {
        List<E> list = repository.findAll();
        if (list.isEmpty() || list == null)
            throw new EntityNotFoundException(entityName + " list not found!");
        return list;
    }

    @Transactional
    public E findById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityName + " data not found!"));
    }

    @Transactional
    public RS findByIdAndReturnDto(ID id) {
        return mapper.toDto(findById(id));
    }

    @Transactional
    public RS updateDatasAndReturnDto(RQ request, ID id) {
        E entity = findById(id);
        mapper.updateEntityFromDto(request, entity);
        E updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Transactional
    public void deleteById(ID id) {
        repository.delete(findById(id));
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    protected <T> void requireNonNullOrEmpty(T value) {
        if (value == null)
            throw new InvalidResourceException(entityName + " cannot be null!");
    }
}
