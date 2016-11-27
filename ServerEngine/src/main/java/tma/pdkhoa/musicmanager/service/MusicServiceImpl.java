package tma.pdkhoa.musicmanager.service;

import java.util.List;

import org.apache.log4j.Logger;

import tma.pdkhoa.musicmanager.api.dao.ModelDAO;
import tma.pdkhoa.musicmanager.api.entity.MusicVO;
import tma.pdkhoa.musicmanager.api.service.MusicException;
import tma.pdkhoa.musicmanager.api.service.MusicService;
import tma.pdkhoa.musicmanager.connection.JMSConnection;

public class MusicServiceImpl implements MusicService {
    final static Logger logger = Logger.getLogger(MusicServiceImpl.class);
    private ModelDAO musicDAO;

    private JMSConnection jmsConnection;

    public void setMusicDAO(ModelDAO musicDAO) {
        this.musicDAO = musicDAO;
    }

    public void setJmsConnection(JMSConnection jmsConnection) {
        this.jmsConnection = jmsConnection;
    }

    public void updateMusic(MusicVO music) throws MusicException {
        musicDAO.update(music);
        notifyAllClient();
    }

    public void insertMusic(MusicVO music) throws MusicException {
        musicDAO.save(music);
        notifyAllClient();
    }

    public void deleteMusic(int id) throws MusicException {
        musicDAO.deleteByID(id);
        notifyAllClient();
    }

    public List<MusicVO> getListMusic() throws MusicException {
        logger.info("getListMusic()");
        List<MusicVO> lisMusics = null;
        try{
            lisMusics = musicDAO.getAll();
        }catch (Exception e){
            logger.error("Error getListMusic()", e.getCause());
            throw new MusicException("loi trong qua trinh query", e.getCause());
        }
        return lisMusics;
    }

    public void notifyAllClient() throws MusicException {
        jmsConnection.sendMessage();
    }

    public MusicVO findByID(int id) {
        return musicDAO.findByID(id);
    }
}
