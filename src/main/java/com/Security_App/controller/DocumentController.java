package com.Security_App.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Security_App.dto.CommonApiResponse;
import com.Security_App.service.DocumentService;

@RestController
@RequestMapping("/api/document")
public class DocumentController {

	@Autowired
	private DocumentService documentService;

	// Method to save Documents

	@PostMapping("/save/{userid}")
	public ResponseEntity<CommonApiResponse> saveDocuments(@PathVariable("userid") Long userId,
			@RequestParam("addar") MultipartFile addar, @RequestParam("pan") MultipartFile pan) {
		return documentService.saveDocuments(userId, addar, pan);
	}

	// Method to update Documents
	@PutMapping("/update/{userid}/{documentid}")
	public ResponseEntity<CommonApiResponse> updateDocuments(@PathVariable("userid") Long userId,
			@PathVariable("documentid") Long docId, @RequestParam("addar") MultipartFile addar,
			@RequestParam("pan") MultipartFile pan) {
		return documentService.updateDocuments(userId, docId, addar, pan);
	}

	// Method to Fetch addarcard
	@GetMapping("fetch/addar/image/{addar}")
	public ResponseEntity<byte[]> findAddar(@PathVariable("addar") String addar) {
		return documentService.findAddar(addar);
	}

	// Method to Fetch pancard
	@GetMapping("fetch/pan/image/{pan}")
	public ResponseEntity<byte[]> findPan(@PathVariable("pan") String pan) {
		return documentService.findpan(pan);
	}

	// Method to delete Particular user document
	@DeleteMapping("/delete/byid/{docid}")
	public ResponseEntity<CommonApiResponse> deleteDocument(@PathVariable("docid") Long docId) {
		return documentService.deleteDocument(docId);
	}

}
