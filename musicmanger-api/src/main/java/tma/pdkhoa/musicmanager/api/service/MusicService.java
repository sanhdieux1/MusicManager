package tma.pdkhoa.musicmanager.api.service;

import java.util.List;

import tma.pdkhoa.musicmanager.api.entity.MusicVO;


public interface MusicService {

	public void updateMusic(MusicVO music);
	
	public void insertMusic(MusicVO music);
	
	public void deleteMusic(int id);
	
	public List<MusicVO> getListMusic();
	
	public MusicVO findByID(int id);
	
	
}
