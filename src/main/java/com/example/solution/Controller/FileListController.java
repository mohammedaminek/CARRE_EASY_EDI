package com.example.solution.Controller;

import com.example.solution.Entites.UploadedFile;
import com.example.solution.repositories.UploadedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class FileListController {

    @Autowired
    private UploadedFileRepository uploadedFileRepository;
    @GetMapping("/user/file-list")
    public String fileList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<UploadedFile> files = uploadedFileRepository.findByUsername(username);
        System.out.println();
        model.addAttribute("files", files);
        return "file_list";
    }
    @GetMapping("/user/delete-file/{id}")
    public String DeleteFile(Model model,@PathVariable Long id) {
        Optional<UploadedFile> fileOptional = uploadedFileRepository.findById(id);
        if (fileOptional.isPresent()) {
            UploadedFile uploadedFile = fileOptional.get();
            uploadedFileRepository.delete(uploadedFile);
        } else {
            return "error";
        }
        return "success";
    }

    @GetMapping("/user/download/{type}/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id, @PathVariable String type) {
        Optional<UploadedFile> fileOptional = uploadedFileRepository.findById(id);
        if (fileOptional.isPresent()) {
            UploadedFile uploadedFile = fileOptional.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            System.out.println(type);
            if  (type.equals("clientFile")){
                headers.setContentDispositionFormData("attachment", uploadedFile.getFileName());
                return new ResponseEntity<>(uploadedFile.getFileData(), headers, HttpStatus.OK);
            }
            if(type.equals("canevaFile")){
                System.out.println("hellosdjklfsdjkljklsd");
                headers.setContentDispositionFormData("attachment", "canevas:"+uploadedFile.getFileName());
                return new ResponseEntity<>(uploadedFile.getFileCarre(), headers, HttpStatus.OK);
            }
        }
            return ResponseEntity.notFound().build();
    }
}