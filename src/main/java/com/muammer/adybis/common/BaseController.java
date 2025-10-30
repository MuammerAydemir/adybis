package com.muammer.adybis.common;

import org.springframework.http.ResponseEntity;

public interface BaseController<RQ, ID> {
    public ResponseEntity<?> createData(RQ req);

    public ResponseEntity<?> updateData(RQ req, ID id);

    public ResponseEntity<?> findAll();

    public ResponseEntity<?> findById(ID id);

    public ResponseEntity<?> deleteById(ID id);
}
