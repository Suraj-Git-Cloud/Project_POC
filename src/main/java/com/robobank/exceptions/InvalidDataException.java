package com.robobank.exceptions;

public class InvalidDataException extends RuntimeException {

	public InvalidDataException(String message)
	{
		super(message);
	}
}