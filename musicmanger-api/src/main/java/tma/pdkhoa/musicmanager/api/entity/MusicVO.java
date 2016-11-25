package tma.pdkhoa.musicmanager.api.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "music", catalog = "musicmanager")
public class MusicVO implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "size")
    private double size;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

}
