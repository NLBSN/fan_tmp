package com.http.day13_JSP_EL_JSTL.StuManager.src.com.itheima.domain;

public class Student {
	
	//到底有哪些成员。 想要在页面上显示多少。 
	private int id ; 
	private String name;
	private int age ;
	private String gender;
	private String address;
	
	
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
}
