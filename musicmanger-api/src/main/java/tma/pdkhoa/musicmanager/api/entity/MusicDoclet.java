package tma.pdkhoa.musicmanager.api.entity;

import java.io.Serializable;

/**
 * Client POJO.
 *
 * @hibernate.class table = "musicdoclet"
 * @hibernate.cache usage = "read-write"
 */
public class MusicDoclet implements Serializable{
    private static final long serialVersionUID = -8361595011677919387L;
    /**
    *
    * @hibernate.id    generator-class = "increment"
    *                  column = "clientid"
    */
    private long id;
    /**
    *
    * @hibernate.property  column = "name"
    *                      length = "100"
    *                      not-null = "true"
    */
    private String name;
    
    /**
    *
    * @hibernate.property  column = "casi"
    *                      length = "100"
    *                      not-null = "true"
    */
    private String casi;
    
    
    
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCasi() {
        return casi;
    }

    public void setCasi(String casi) {
        this.casi = casi;
    }

}
