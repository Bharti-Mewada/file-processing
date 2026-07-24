package com.fileProcessing.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fileProcessing.entity.UserFile;
import com.fileProcessing.repository.FileUploadRepo;
import com.fileProcessing.utility.FileConstant;
import com.fileProcessing.utility.FileValidationException;

import jakarta.persistence.criteria.Path;

@Service
public class UploadService {

	private FileUploadRepo filRepo;
	@Autowired
	private FileConstant fileconstant;

	@Value("${file.upload-dir}")
	private String fileUploadedPath;

	@Value("${file.transaction.header}")
	private String fileTransactionHeader;

	@Value("${file.customer.header}")
	private String fileCustomerHeader;

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

	public UploadService(FileUploadRepo filRepo) {
		this.filRepo = filRepo;
	}

	private UserFile saveMetadata(MultipartFile multiPart) {

		UserFile userFile = new UserFile();

		userFile.setFileName(multiPart.getOriginalFilename());

		userFile.setFileSize(multiPart.getSize());

		if (multiPart.getOriginalFilename().endsWith(fileconstant.CustomerFileExtension)) {
			userFile.setFileType("M");
		} else {
			userFile.setFileType("A");
		}

		userFile.setResponse("Success");
		userFile.setStatus("Uploaded");

		return filRepo.save(userFile);
	}

	private File saveFileToDisk(MultipartFile multiPart) throws Exception {

		File directory = new File(fileUploadedPath);

		if (!directory.exists()) {
			directory.mkdirs();
		}

		File file = new File(directory, multiPart.getOriginalFilename());

		try (InputStream inputStream = multiPart.getInputStream(); FileOutputStream fos = new FileOutputStream(file)) {

			byte[] buffer = new byte[1024];
			int bytesRead;

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesRead);
			}
		}

		return file;
	}

	private void validateFileNameAndSize(MultipartFile multiPart) throws Exception {

		String fileName = multiPart.getOriginalFilename();

		if (multiPart.getSize() == 0) {
			throw new FileValidationException("File is empty");
		}

		int startIndex = fileName.lastIndexOf('_') + 1;
		int endIndex = fileName.lastIndexOf('.');

		String date = fileName.substring(startIndex, endIndex);

		if (date.length() != 8) {
			throw new FileValidationException("Invalid date format");
		}

		boolean validCustomer = fileName.startsWith(fileconstant.fileNameCustomer)
				&& fileName.endsWith(fileconstant.CustomerFileExtension);

		boolean validTransaction = fileName.startsWith(fileconstant.filenameTransaction)
				&& fileName.endsWith(fileconstant.TransactionFileExtension);

		if (!validCustomer && !validTransaction) {
			throw new FileValidationException("Invalid file name : " + fileName);
		}
	}

	private void validateTransactionFile(BufferedReader br) throws Exception {

		String header = br.readLine();

		if (!header.equals(fileTransactionHeader)) {
			throw new FileValidationException("Transaction file header not correct");
		}

		String line;
		int rowCount = 1;

		while ((line = br.readLine()) != null) {

			rowCount++;

			String[] column = line.split(",");

			

			if (column[0].isBlank()) {
				throw new FileValidationException("Transaction Id mandatory at row " + rowCount);
			}

			if (column[1].isBlank()) {
				throw new FileValidationException("Account Number mandatory at row " + rowCount);
			}

			if (column[2].isBlank()) {
				throw new FileValidationException("Amount mandatory at row " + rowCount);
			}
		}
	}

	private void validateCustomerFile(BufferedReader br) throws Exception {

		String header = br.readLine();

		if (!header.equals(fileCustomerHeader)) {
			throw new FileValidationException("Customer file header not correct");
		}

		String line;
		int rowCount = 1;

		while ((line = br.readLine()) != null) {

			rowCount++;

			String[] column = line.split(",");

			

			if (column[0].isBlank()) {
				throw new FileValidationException("Customer Id mandatory at row " + rowCount);
			}

			if (column[2].isBlank()) {
				throw new FileValidationException("Email mandatory at row " + rowCount);
			}

			if (!EMAIL_PATTERN.matcher(column[2]).matches()) {
				throw new FileValidationException("Invalid email at row " + rowCount);
			}
		}
	}

	public UserFile fileUpload(MultipartFile multiPart) throws Exception {

		validateFileNameAndSize(multiPart);

		File file = saveFileToDisk(multiPart);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(multiPart.getInputStream()))) {

			String fileName = multiPart.getOriginalFilename();

			if (fileName.startsWith(fileconstant.fileNameCustomer)) {
				validateCustomerFile(br);
			} else if (fileName.startsWith(fileconstant.filenameTransaction)) {
				validateTransactionFile(br);
			}
		}

		return saveMetadata(multiPart);
	}

}
