package tma.pdkhoa.musicmanager.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="person", catalog="hibernate")
public class Person implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "age")
	private int age;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
}
