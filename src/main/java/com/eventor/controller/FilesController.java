package com.eventor.controller;

import com.eventor.model.FileInfo;
import com.eventor.model.User;
import com.eventor.payload.response.MessageResponse;
import com.eventor.repository.UserRepository;
import com.eventor.security.services.FilesStorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin("http://localhost:8081")
@RestController
@RequestMapping("/eventor")
public class FilesController {
    @Autowired
    FilesStorageService storageService;

    @Autowired
    UserRepository userRepository;

//    Upload 1 file
    @PostMapping("/files/upload/users/{id}")
    public ResponseEntity<MessageResponse> uploadFile(@PathVariable("id") int id, @RequestParam("file") MultipartFile file) { //Add user id here
        String message = "";
        try {
            Optional<User> userData = userRepository.findById(id);

            if (userData.isPresent()) {
                User currentUser = userData.get();
                String currentDate = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
                String newFileName = file.getOriginalFilename().replace(file.getOriginalFilename(), FilenameUtils.getBaseName(file.getOriginalFilename()).concat(currentDate) + "." + FilenameUtils.getExtension(file.getOriginalFilename())).toLowerCase();
                storageService.save(file, newFileName);
                currentUser.setProfile_img(newFileName);
                userRepository.save(currentUser);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
        }
    }

//    Upload many files
//@PostMapping("/files")
//public ResponseEntity<MessageResponse> uploadFiles(@RequestParam("files") MultipartFile[] files) {
//    String message = "";
//    try {
//        List<String> fileNames = new ArrayList<>();
//
//        Arrays.asList(files).stream().forEach(file -> {
//            storageService.save(file);
//            fileNames.add(file.getOriginalFilename());
//        });
//
//        message = "Uploaded the files successfully: " + fileNames;
//        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
//    } catch (Exception e) {
//        message = "Fail to upload files!";
//        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
//    }
//}

//    Applicable to multiple images - eg for event / service gallery
    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() { //modify to get all images by event / service id
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/files/users/{id}") //user id
    public ResponseEntity<FileInfo> getProfileImage(@PathVariable("id") int id) { //modify to get all images by event / service id
        Optional<User> userData = userRepository.findById(id);
        if (userData.isPresent()) {
            User currentUser = userData.get();
            FileInfo fileInfos;
            Stream<Path> foundImage = storageService.loadOne(currentUser.getProfileImg());

            Optional<FileInfo> optionalFileInfo = storageService.loadOne(currentUser.getProfileImg()).map(path -> {
                String filename = path.getFileName().toString();
                String url = MvcUriComponentsBuilder
                        .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

                return new FileInfo(filename, url);
            }).findFirst();
            if(optionalFileInfo.isPresent())
            {
                fileInfos = optionalFileInfo.get();
                return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    Applicable to single image - eg for user profile pic
@GetMapping("/files/file/{filename}")
@ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable("filename")String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
