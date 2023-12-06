package com.Security_App.dto;

import java.util.ArrayList;
import java.util.List;

import com.Security_App.entity.Document;

import lombok.Data;

@Data
public class DocumentResponseDto extends CommonApiResponse {

	List<Document> documents = new ArrayList<>();
	
	private Document document;
}
