package com.Security_App.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.Security_App.dto.CommonApiResponse;
import com.Security_App.dto.DocumentResponseDto;
import com.Security_App.entity.Document;
import com.Security_App.entity.User;
import com.Security_App.repository.DocumentRepository;
import com.Security_App.repository.UserRepository;
import com.Security_App.utils.CustomerUtils;

@Service
public class DocumentService {

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StorageService storageService;

	// Method to save Documents

	public ResponseEntity<CommonApiResponse> saveDocuments(Long userId, MultipartFile addar, MultipartFile pan) {
		CommonApiResponse response = new CommonApiResponse();
		try {

			if (userId == 0) {
				response.setResponseMessage("UserID is not Selected");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			if (addar == null || pan == null) {
				response.setResponseMessage("File is not Selected");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			Optional<User> user = userRepository.findById(userId);
			if (user.isEmpty()) {
				response.setResponseMessage("No user Found");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			Boolean flag = true;
			List<Document> lst = documentRepository.findAll();
			if (!lst.isEmpty()) {
				for (Document document : lst) {

					if (document.getUser().getId().equals(userId)) {
						flag = false;
						break;
					}
					flag = true;
				}
			}
			if (flag) {
				Document document = new Document();

				String addars = storageService.store(addar);
				String pans = storageService.store(pan);
				document.setAadharCard(addars);
				document.setPanCard(pans);
				document.setUser(user.get());
				Document data = documentRepository.save(document);
				if (data != null) {
					response.setResponseMessage("Documents saved successfully:----->" + data);
					response.setSuccess(true);
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
				response.setResponseMessage("Failed to save Documents..");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			} else {
				response.setResponseMessage("Document is Already Present,Try to Update it");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setResponseMessage("SOMETHING_WENT_WRONG");
		response.setSuccess(false);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Method to update Documents

	public ResponseEntity<CommonApiResponse> updateDocuments(Long userId, Long docId, MultipartFile addar,
			MultipartFile pan) {
		CommonApiResponse response = new CommonApiResponse();
		try {

			if (userId == 0 || docId == 0) {
				response.setResponseMessage("UserID or docID is not Selected");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			if (addar == null || pan == null) {
				response.setResponseMessage("File is not Selected");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			Optional<User> user = userRepository.findById(userId);
			if (user.isEmpty()) {
				response.setResponseMessage("No user Found");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			Optional<Document> docs = documentRepository.findById(docId);
			if (docs.isEmpty()) {
				response.setResponseMessage("No Documents Found,Unable to Update");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			Document document = docs.get();
			String addars = storageService.store(addar);
			String pans = storageService.store(pan);
			document.setAadharCard(addars);
			document.setPanCard(pans);
			document.setUser(user.get());
			Document data = documentRepository.save(document);
			if (data != null) {
				response.setResponseMessage("Documents Updated successfully:----->" + data);
				response.setSuccess(true);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			response.setResponseMessage("Failed to Update Documents..");
			response.setSuccess(false);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setResponseMessage("SOMETHING_WENT_WRONG");
		response.setSuccess(false);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Method to Fetch Addarcard

	public ResponseEntity<byte[]> findDocument(String url) {
		try {

			Resource doc = storageService.load(url);

			if (doc != null && doc.exists()) {
				try (InputStream in = doc.getInputStream()) {
					byte[] imageBytes = IOUtils.toByteArray(in);

					// Detect the image type by inspecting its content
					String imageFormat = getImageFormat(imageBytes);

					// Set content type dynamically based on detected image format
					MediaType mediaType = MediaType.parseMediaType("image/" + imageFormat.toLowerCase());
					ResponseEntity<byte[]> imageResponseEntity = CustomerUtils.getImageResponseEntity(imageBytes,
							mediaType);
					return imageResponseEntity;
				} catch (IOException e) {
					e.printStackTrace();
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Method to detect the image format based on its content
	private String getImageFormat(byte[] imageBytes) throws IOException {
		try (InputStream is = new ByteArrayInputStream(imageBytes);
				ImageInputStream imageInputStream = ImageIO.createImageInputStream(is)) {
			Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);
			if (readers.hasNext()) {
				ImageReader reader = readers.next();
				return reader.getFormatName();
			}
		}
		throw new IOException("Unknown image format");
	}

	public ResponseEntity<CommonApiResponse> deleteDocument(Long docId) {
		CommonApiResponse response = new CommonApiResponse();
		try {
			if (docId == null) {
				response.setResponseMessage("missing input");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			Optional<Document> document = documentRepository.findById(docId);
			if (document.isEmpty()) {
				response.setResponseMessage("Documents not found, failed to delete the Documents");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			Document doc = document.get();
			// storageService.delete(doc.getAadharCard());
			// storageService.delete(doc.getPanCard());
			doc.setUser(null);
			documentRepository.delete(doc);
			response.setResponseMessage("Documents Deleted Successful");
			response.setSuccess(true);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setResponseMessage("SOMETHING_WENT_WRONG");
		response.setSuccess(false);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<DocumentResponseDto> findDocuments() {
		DocumentResponseDto response = new DocumentResponseDto();
		try {

			List<Document> documents = documentRepository.findAll();
			if (CollectionUtils.isEmpty(documents)) {
				response.setResponseMessage("No Documents Found ..");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			response.setResponseMessage("Documents Fetched Sucesfully...");
			response.setSuccess(true);
			response.setDocuments(documents);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setResponseMessage("SOMETHING_WENT_WRONG");
		response.setSuccess(false);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	public ResponseEntity<DocumentResponseDto> findDocumentById(Long id) {
		DocumentResponseDto response = new DocumentResponseDto();
		try {

			Optional<Document> documents = documentRepository.findById(id);

			if (documents.isEmpty()) {
				response.setResponseMessage("No Documents Found ..");
				response.setSuccess(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			Document document = documents.get();
			response.setResponseMessage("Documents Fetched Sucesfully...");
			response.setSuccess(true);
			response.setDocument(document);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setResponseMessage("SOMETHING_WENT_WRONG");
		response.setSuccess(false);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
