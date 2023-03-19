package com.project.splitwise.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {

    @NotNull
    String referenceId;
    String name;
    @NotBlank(message = "Phone number cannot be blank")
    String phoneNumber;
    @NotBlank(message = "Phone number cannot be blank")
    String email;
}