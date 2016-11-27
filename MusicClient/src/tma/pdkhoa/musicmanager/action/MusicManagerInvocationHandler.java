package tma.pdkhoa.musicmanager.action;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import tma.pdkhoa.musicmanager.client.exception.MusicClientException;

public class MusicManagerInvocationHandler implements InvocationHandler {
    private MusicManagement musicManagement;

    public MusicManagerInvocationHandler(MusicManagement musicManagement) {
        this.musicManagement = musicManagement;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(musicManagement.getInstance().canExecute()) {
            return method.invoke(musicManagement, args);
        }
        throw new MusicClientException("Can't connect MusicManagement", "");
    }

}
