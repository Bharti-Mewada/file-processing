package com.fileProcessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fileProcessing.entity.UserFile;

@Repository
public interface FileUploadRepo extends JpaRepository<UserFile, Integer>{
	
	
	
	
	

}
