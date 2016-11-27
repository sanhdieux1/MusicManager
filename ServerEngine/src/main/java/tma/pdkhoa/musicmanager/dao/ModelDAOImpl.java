package tma.pdkhoa.musicmanager.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import tma.pdkhoa.musicmanager.api.dao.ModelDAO;
import tma.pdkhoa.musicmanager.api.service.MusicException;

@Transactional
public class ModelDAOImpl<T> extends HibernateDaoSupport implements ModelDAO {
    Class<T> instance;
    final static Logger logger = Logger.getLogger(ModelDAOImpl.class);

    public ModelDAOImpl(Class<T> ins) {
        instance = ins;
        logger.debug("init:"+this.getClass().getName());
    }

    // typo ??
    public boolean iDIsExist(int id) {
        List<?> listMusic = getHibernateTemplate().find("select id from MusicVO where id=?", id);
        if(listMusic.size() > 0)
            return true;
        return false;
    }

    @Override
    public <T> int save(T model) {
        getHibernateTemplate().save(model);
        return 1;
    }

    @Override
    public <T> int update(T model) {
        getHibernateTemplate().update(model);
        return 0;
    }

    @Override
    public <T> int deleteByID(int id) {
        T element = (T) getHibernateTemplate().get(instance, id);
        if(element == null){
            return 0;
        }else{
            getHibernateTemplate().delete(element);
        }
        return 1;
    }

    @Override
    public <T> List<T> getAll() throws MusicException {
        logger.info("getAll()");
        try{
            return (List<T>) getHibernateTemplate().loadAll(instance);
        }catch (Exception e){
            throw new MusicException("can not execute hibernate getAll()", e.getCause());
        }
    }

    @Override
    public <T> T findByID(int id) {
        getHibernateTemplate().load(instance, id);
        return null;
    }

}
