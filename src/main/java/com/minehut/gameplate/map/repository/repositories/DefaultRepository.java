package com.minehut.gameplate.map.repository.repositories;

import com.minehut.gameplate.GamePlate;
import com.minehut.gameplate.map.repository.exception.RotationLoadException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class DefaultRepository extends Repository {

    @SuppressWarnings("FieldCanBeLocal")
    private String DEFAULT_MAP_RESOURCE = "DefaultMap";

    public DefaultRepository() throws RotationLoadException, IOException {
        super(Repository.getNewRepoPath("default-gameplate-repo"));
    }

    @Override
    public void refreshRepo() throws RotationLoadException, IOException {
        cloneResources();
        super.refreshRepo();
    }

    private void cloneResources() throws IOException {
        cloneFile("map.json");
        cloneFile("level.dat");
        cloneFile("region/r.0.0.mca" );
        cloneFile("region/r.0.-1.mca");
        cloneFile("region/r.-1.0.mca");
        cloneFile("region/r.-1.-1.mca");
    }

    private void cloneFile(String file) throws IOException {
        file = DEFAULT_MAP_RESOURCE + "/" + file;
        FileUtils.copyInputStreamToFile(GamePlate.getInstance().getResource(file), new File(getPath(), file));
    }

    @Override
    public String getSource(boolean op) {
        return "Default GamePlate Repository";
    }

}
