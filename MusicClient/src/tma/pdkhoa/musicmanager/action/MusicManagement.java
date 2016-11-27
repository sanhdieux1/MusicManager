package tma.pdkhoa.musicmanager.action;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import tma.pdkhoa.musicmanager.api.entity.MusicVO;
import tma.pdkhoa.musicmanager.api.service.MusicException;
import tma.pdkhoa.musicmanager.api.service.MusicService;
import tma.pdkhoa.musicmanager.client.connector.JMSListener;
import tma.pdkhoa.musicmanager.client.connector.JMXConnection;

public class MusicManagement {
    private MusicService musicService;
    private static MusicManagement instance = new MusicManagement();
    final static Logger logger = Logger.getLogger(MusicManagement.class);
    
    private MusicManagement() {
        musicService = JMXConnection.getInstance().getMusicService();
    }

    public boolean canExecute() {
        return JMXConnection.getInstance().isConnected();
    }

    public void addMusic(MusicVO musicVO) {
        try{
            musicService.insertMusic(musicVO);
        }catch (MusicException e){
            e.printStackTrace();
        }
    }

    public synchronized static MusicManagement getInstance() {
        if(instance == null){
            MusicManagement instanceWrapper = new MusicManagement();
            instance = (MusicManagement) Proxy.newProxyInstance(MusicManagement.class.getClassLoader(), new Class[] { MusicManagement.class }, new MusicManagerInvocationHandler(instanceWrapper));
        }
        return instance;
    }

    public void deleteMusic(int id) {
        try{
            musicService.deleteMusic(id);
        }catch (MusicException e){
            e.printStackTrace();
        }
    }

    public void editMusic(MusicVO musicVO) {
        try{
            musicService.updateMusic(musicVO);
        }catch (MusicException e){
            e.printStackTrace();
        }
    }

    public List<MusicVO> getListMusic() {
        try{
            return musicService.getListMusic();
        }catch (MusicException e){
            logger.error("Cannot getListMusic(): Cause by :"+e.getMessage(),e.getCause());
        }catch(Exception e) {
            logger.error("getListMusic()", e.getCause());
        }
        return new ArrayList<MusicVO>();
    }

}
