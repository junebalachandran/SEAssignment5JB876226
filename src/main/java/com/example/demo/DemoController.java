package com.example.demo;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DemoController {
	
	@Autowired
	private StudentRepository studentRepo;
	
	
	@GetMapping(value="/")
	public ModelAndView renderPage() {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("index");
		return mv;
		
	}
	@GetMapping(value="/facebook")
	public ModelAndView renderFB() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebookIndex");
		return mv;
		
	}
	
	@PostMapping(value="/student/add")
	public ModelAndView saveStudent(
			@RequestParam(name="name",required = true) String name , @RequestParam String email
			)
	{
		Student n = new Student();
		n.setName(name);
		n.setEmail(email);
		studentRepo.save(n);
		return new ModelAndView("redirect:/students");
	}
	@GetMapping(value="/students")
	public ModelAndView getAllStudents() {
		ModelAndView mv = new ModelAndView();
		List<Student> students = studentRepo.findAll();
		mv.addObject("students",students);
		mv.setViewName("allStudents");
		return mv;
		
	}
	
	@GetMapping(value="/student")
	public ModelAndView getOneStudent(@RequestParam(name="email",required=true) String email) {
		ModelAndView mv = new ModelAndView();
		try {
			Student s = studentRepo.findByEmail(email);
			mv.addObject("student",s);
			mv.setViewName("studentInfo");
			if(s == null) {
				throw new Exception("Error");
			}
		}
		catch(Exception e)
		{
			mv.addObject("error","Student Not Available");
			mv.setViewName("studentError");
			e.printStackTrace();
		}
		return mv;
	}


	@PostMapping(value="/facebookRedirect")
	public ModelAndView handleRedirect(
			@RequestParam(name="myId") String myId,
			@RequestParam(name="myName") String myName,
			@RequestParam(name="myFriends") String myFriends,
			@RequestParam(name="myEmail") String myEmail,
			HttpServletRequest req
			) {
		System.out.println(myId + myName + myEmail + myFriends);
		String[] splitted = myFriends.split("/");
		for(int i=0; i < splitted.length ; i++) {
			System.out.println(i+" : "+ splitted[i]);
		}
		return new ModelAndView("allStudents");
				}
}
	

/*
	
	@PostMapping(value="/upload")
	public ModelAndView uploadtoS3(
		@RequestParam("file") MultipartFile image
		){
		ModelAndView profilePage = new ModelAndView();
		BasicAWSCredentials cred = new BasicAWSCredentials("AKIAJM7ON37B2ZY4OS4A","kyw9Ozp4V84KVyyQ82FnHMpcGqS+fmhTOW2B+0Bn");
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(cred)).withRegion(Regions.US_EAST_2).build();
		try {
			PutObjectRequest putReq = new PutObjectRequest("junebalachandran",image.getOriginalFilename(),image.getInputStream(),new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead);
			s3client.putObject(putReq);
			String imgSrc = "http://"+"junebalachandran"+".s3.amazonaws.com/"+image.getOriginalFilename();
			profilePage.addObject("imgSrc", imgSrc);
			profilePage.setViewName("profilePage");
			return profilePage;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		profilePage.setViewName("index");
		return profilePage;
		
	}
	
}
*/
