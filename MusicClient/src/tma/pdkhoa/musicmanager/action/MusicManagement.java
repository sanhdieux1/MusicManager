package tma.pdkhoa.musicmanager.action;

import java.util.List;

import tma.pdkhoa.musicmanager.api.entity.MusicVO;
import tma.pdkhoa.musicmanager.api.service.MusicService;
import tma.pdkhoa.musicmanager.client.connector.JMXConnection;

public class MusicManagement {
    private MusicService musicService;
    public static MusicManagement INSTANCE = new MusicManagement();
    private MusicManagement() {
        musicService = JMXConnection.getInstance().getMusicService();
    }
    
    public boolean canExecute(){
        return JMXConnection.getInstance().isConnected();
    }
    public void addMusic(MusicVO musicVO) {
        if(canExecute()){
            musicService.insertMusic(musicVO);
        }
    }
    
    public static MusicManagement getInstance() {
        return INSTANCE;
    }

    public void deleteMusic(int id) {
        if(canExecute()){
            musicService.deleteMusic(id);
        }
    }

    public void editMusic(MusicVO musicVO) {
        if(canExecute()){
            musicService.updateMusic(musicVO);
        }
    }

    public List<MusicVO> getListMusic() {
        if(canExecute()){
            return musicService.getListMusic();
        }
        return null;
    }

}
