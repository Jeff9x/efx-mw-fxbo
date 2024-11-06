package com.empirefx.fxbo.commonlib.models;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
public class RequestPayload implements Serializable {

	@NotEmpty(message = "First name is required")
	private String firstName;

	@NotEmpty(message = "Last name is required")
	private String lastName;

	private String middleName;

	@NotEmpty(message = "Country is required")
	@Size(min = 2, max = 2, message = "Country code must be exactly 2 characters")
	private String country;

	@NotEmpty(message = "Phone number is required")
	@Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number format")
	private String phone;

	@NotEmpty(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotEmpty(message = "Client type is required")
	private String clientType;

	@NotNull(message = "Email verified status is required")
	private Boolean emailVerified;

}