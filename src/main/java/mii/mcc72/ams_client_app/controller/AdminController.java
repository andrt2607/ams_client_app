package mii.mcc72.ams_client_app.controller;

import mii.mcc72.ams_client_app.models.dto.SubmissionDTO;
import mii.mcc72.ams_client_app.services.CategoryService;
import mii.mcc72.ams_client_app.services.EmployeeService;
import mii.mcc72.ams_client_app.util.FileUploadUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private CategoryService categoryService;
    private EmployeeService employeeService;
    @GetMapping("/submission")
    public String submission(Authentication authentication , Model model) {
        model.addAttribute("user",authentication.getName());
        model.addAttribute("isActive", "submission");
        return "admin/submission";
    }
    @GetMapping("/penSubmission")
    public String PenSubmission(Authentication authentication , Model model) {
        model.addAttribute("user",authentication.getName());
        model.addAttribute("isActive", "penSubmission");
        return "admin/pen_submission";
    }
    @GetMapping("/revSubmission")
    public String RevSubmission(Authentication authentication , Model model) {
        model.addAttribute("user",authentication.getName());
        model.addAttribute("isActive", "revSubmission");
        return "admin/rev_submission";
    }
    @GetMapping("/submissionform")
    public String SubmissionForm(SubmissionDTO submissionDTO, Authentication authentication , Model model) {
        model.addAttribute("user",authentication.getName());
        model.addAttribute("isActive", "submission");
        model.addAttribute("categories", categoryService.getAll());
        return "admin/submission_form";
    }

    @PostMapping("/submissionAsset")
    public RedirectView submissionAsset(SubmissionDTO submissionDTO, Authentication authentication, @RequestParam("file") MultipartFile multipartFile) throws IOException {

        String fileName = UUID.randomUUID().toString().substring(0,3)+"_"+ StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = "src/main/resources/static/img/" +authentication.getName();
        submissionDTO.setImage(authentication.getName()+"/"+fileName);
        employeeService.createSubmissionRequest(submissionDTO);
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        return new RedirectView("/admin/submission", true);
    }
}