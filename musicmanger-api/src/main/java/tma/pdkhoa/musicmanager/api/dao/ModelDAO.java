package tma.pdkhoa.musicmanager.api.dao;

import java.util.List;

import tma.pdkhoa.musicmanager.api.entity.MusicVO;

public interface ModelDAO {

    public <T> int save(T model);

    public <T> int update(T model);

    public <T> int deleteByID(int id);

    public <T> List<T> getAll();

    public <T> T findByID(int id);

}
