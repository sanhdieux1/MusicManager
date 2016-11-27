package tma.pdkhoa.musicmanager.api.dao;

import java.util.List;

import tma.pdkhoa.musicmanager.api.entity.MusicVO;
import tma.pdkhoa.musicmanager.api.service.MusicException;

public interface ModelDAO {

    public <T> int save(T model);

    public <T> int update(T model);

    public <T> int deleteByID(int id);

    public <T> List<T> getAll() throws MusicException;

    public <T> T findByID(int id);

}
