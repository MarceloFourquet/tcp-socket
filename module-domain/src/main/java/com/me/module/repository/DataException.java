
package com.me.module.repository;

public class DataException extends RuntimeException{

	public DataException(){
	}

	public DataException(String string){
		super(string);
	}

	public DataException(String string, Throwable cause){
		super(string, cause);
	}

	public DataException(Throwable cause){
		super(cause);
	}

}
