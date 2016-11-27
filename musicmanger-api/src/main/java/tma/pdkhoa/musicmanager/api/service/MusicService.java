package tma.pdkhoa.musicmanager.api.service;

import java.util.List;

import tma.pdkhoa.musicmanager.api.entity.MusicVO;


public interface MusicService {
    public static final String SERVICE_NAME="bean:name=musicService";
    
	public void updateMusic(MusicVO music) throws MusicException;
	
	public void insertMusic(MusicVO music) throws MusicException;
	
	public void deleteMusic(int id) throws MusicException;
	
	public List<MusicVO> getListMusic() throws MusicException;
	
	public MusicVO findByID(int id) throws MusicException;
	
	
}
