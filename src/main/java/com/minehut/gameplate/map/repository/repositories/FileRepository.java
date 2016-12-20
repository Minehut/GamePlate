package com.minehut.gameplate.map.repository.repositories;

import com.minehut.gameplate.map.repository.exception.RotationLoadException;

import java.io.IOException;

public class FileRepository extends Repository {

    public FileRepository(String path) throws RotationLoadException, IOException {
        super(path);
    }

}
