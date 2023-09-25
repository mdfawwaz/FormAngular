
package com.prodapt.learningspring.model;

 

import lombok.Data;

 
@Data



public class RegistrationForm {

    public String getUsername() {
		return username;
	}

 

	public void setUsername(String username) {
		this.username = username;
	}

 

	public String getPassword() {
		return password;
	}

 

	public void setPassword(String password) {
		this.password = password;
	}

 

	public String getRepeatPassword() {
		return repeatPassword;
	}

 

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
	private String username;
    private String password;
    private String repeatPassword;
    private String email;
    private String dateOfBirth;
    private String bio;

 

    public boolean isValid() {

 

        return password.equals(repeatPassword);

 

    }

 

	public String getDateOfBirth() {
		return dateOfBirth;
	}

 

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

 

	public String getBio() {
		return bio;
	}

 

	public void setBio(String bio) {
		this.bio = bio;
	}

 

	public String getEmail() {
		return email;
	}

 

	public void setEmail(String email) {
		this.email = email;
	}

 

 

	

 

}