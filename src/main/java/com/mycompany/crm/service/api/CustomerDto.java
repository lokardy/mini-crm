package com.mycompany.crm.service.api;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import lombok.ToString;

@Data()
@ToString
public class CustomerDto extends VersionDto {

	@NotNull
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z_ ]*$")
	@Length(max = 128, min = 2)
	private String customerName;

	@NotNull
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9#,_ ]*$")
	@Length(max = 512, min = 5)
	private String address;
	
	@Digits(integer = 12, fraction = 0)
	private Long phoneNumber;

	@Max(100)
	@Min(1)
	private int age;

}
