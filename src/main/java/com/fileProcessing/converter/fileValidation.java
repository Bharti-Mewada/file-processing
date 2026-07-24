package com.fileProcessing.converter;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fileProcessing.entity.UserFile;
import com.fileProcessing.service.UploadService;

@RequestMapping("/file")
@RestController
public class fileValidation {

	private UploadService upService;

	public fileValidation(UploadService upService) {
		this.upService = upService;
	}

	@PostMapping("/upload")
	public UserFile fileUpload(MultipartFile mulFile) throws IOException,Exception {
		return upService.fileUpload(mulFile);

	}

}
