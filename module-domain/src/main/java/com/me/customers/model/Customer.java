
package com.me.customers.model;

public class Customer{

	private int id;
	private String name;
	private String last_name;
	private int dni;
	private String address;
	private String email;
	private String phone;

	public Customer(){
	}

	public Customer(int id, String name, String last_name, int dni, String address, String email, String phone){
		this.id = id;
		this.name = name;
		this.last_name = last_name;
		this.dni = dni;
		this.address = address;
		this.email = email;
		this.phone = phone;
	}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getLast_name(){
		return last_name;
	}

	public void setLast_name(String last_name){
		this.last_name = last_name;
	}

	public int getDni(){
		return dni;
	}

	public void setDni(int dni){
		this.dni = dni;
	}

	public String getAddress(){
		return address;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getEmail(){
		return email;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getPhone(){
		return phone;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	@Override
	public String toString(){
		return String.format("CUSTOMER:%s;%s;%d;%s;%s;%s", name, last_name, dni, address, email, phone);
	}

}
