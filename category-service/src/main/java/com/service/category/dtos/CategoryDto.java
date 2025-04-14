package com.service.category.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private String id;

    @NotEmpty(message = "title is required !!!")
    @Size(min = 3, max = 50, message = "title must be lies between 3 and 50 characters!")
    private String title;

    @NotEmpty(message = "description is necessary !!!")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss a", timezone = "IST")
    private Date addedDate;

    private String bannerImageUrl;

}
