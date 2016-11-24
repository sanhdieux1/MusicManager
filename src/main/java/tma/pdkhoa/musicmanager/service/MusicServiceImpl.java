package tma.pdkhoa.musicmanager.service;

import java.util.List;

import tma.pdkhoa.musicmanager.api.dao.ModelDAO;
import tma.pdkhoa.musicmanager.api.entity.MusicVO;
import tma.pdkhoa.musicmanager.api.service.MusicService;
import tma.pdkhoa.musicmanager.connection.JMSConnection;

public class MusicServiceImpl implements MusicService{
	
	private ModelDAO musicDAO;
	
	private JMSConnection jmsConnection;
	
	public void setMusicDAO(ModelDAO musicDAO) {
		this.musicDAO = musicDAO;
	}
	
	public void setJmsConnection(JMSConnection jmsConnection) {
		this.jmsConnection = jmsConnection;
	}


	public void updateMusic(MusicVO music){
		musicDAO.update(music);
		notifyAllClient();
	}
	
	public void insertMusic(MusicVO music){
		musicDAO.save(music);
		notifyAllClient();
	}
	
	public void deleteMusic(int id){
		musicDAO.deleteByID(id);
		notifyAllClient();
	}
	
	public List<MusicVO> getListMusic(){
		List<MusicVO> lisMusics =  musicDAO.getAll();
		return lisMusics;
	}

	
	public void notifyAllClient(){
			jmsConnection.sendMessage();
	}

	public MusicVO findByID(int id) {
		return musicDAO.findByID(id);
	}
}
