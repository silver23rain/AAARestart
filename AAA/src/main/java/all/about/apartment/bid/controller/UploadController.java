package all.about.apartment.bid.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import all.about.apartment.bid.util.MediaUtils;
import all.about.apartment.bid.util.uploadFileUtils;


/*@Controller*/
@RequestMapping("/bid/*")
public class UploadController {
	
	@Resource(name = "uploadPath")
	private String uploadPath;

	@RequestMapping(value="/uploadForm",method=RequestMethod.GET)
	public void uploadForm(){
		
	}

	@RequestMapping(value="/uploadForm",method=RequestMethod.POST)
	public String uploadForm(MultipartFile file,Model model)throws Exception{
		System.out.println("orginalNmae:" + file.getOriginalFilename());
		System.out.println("size: " + file.getSize());
		System.out.println("contentType: " + file.getContentType());
		
		String savedName = uploadFile(file.getOriginalFilename(),file.getBytes());
		
		model.addAttribute("savedName",savedName);
		
		return "uploadResult";
		
	}
	
	private String uploadFile(String originalName,byte[] fileDate)throws Exception{
		
		UUID uid = UUID.randomUUID();
		
		String savedName= uid.toString() + "_" + originalName;
		
		File target = new File(uploadPath,savedName);
		
		FileCopyUtils.copy(fileDate, target);
		
		return savedName;
	}
	
	@RequestMapping(value = "/uploadAjax",method=RequestMethod.GET)
	public void uploadAjax(){
		
	}
	
	 @ResponseBody
	 @RequestMapping(value ="/uploadAjax", method=RequestMethod.POST, 
	                  produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> uploadAjax(MultipartFile file)throws Exception{
		 
		 System.out.println("originalName : " + file.getOriginalFilename());

		return new ResponseEntity<>(
				uploadFileUtils.uploadFile(uploadPath, file.getOriginalFilename(),
						file.getBytes()),HttpStatus.CREATED);
	  }
	 
	 @ResponseBody
	 @RequestMapping(value="/displayFile")
	 public ResponseEntity<byte[]> displayFile(String fileName)throws Exception{
		 
		 InputStream in = null;
		 ResponseEntity<byte[]> entity = null;
		 
		 System.out.println("FILE NAME: " + fileName);
		 
		 try{
		      
		      String formatName = fileName.substring(fileName.lastIndexOf(".")+1);
		      
		      MediaType mType = MediaUtils.getMediaType(formatName);
		      
		      HttpHeaders headers = new HttpHeaders();
		      
		      in = new FileInputStream(uploadPath+fileName);
		      
		      if(mType != null){
		        headers.setContentType(mType);
		      }else{
		        
		        fileName = fileName.substring(fileName.indexOf("_")+1);       
		        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		        headers.add("Content-Disposition", "attachment; filename=\""+ 
		          new String(fileName.getBytes("UTF-8"), "ISO-8859-1")+"\"");
		      }

		        entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), 
		          headers, 
		          HttpStatus.CREATED);
		    }catch(Exception e){
		      e.printStackTrace();
		      entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		    }finally{
		      in.close();
		    }
		      return entity;    
		  }
	 
	 @ResponseBody
	 @RequestMapping(value="/deleteFile", method=RequestMethod.POST)
	  public ResponseEntity<String> deleteFile(String fileName){
	    
	   System.out.println("delete file: "+ fileName);
	    
	    String formatName = fileName.substring(fileName.lastIndexOf(".")+1);
	    
	    MediaType mType = MediaUtils.getMediaType(formatName);
	    
	    if(mType != null){      
	      
	      String front = fileName.substring(0,12);
	      String end = fileName.substring(14);
	      new File(uploadPath + (front+end).replace('/', File.separatorChar)).delete();
	    }
	    
	    new File(uploadPath + fileName.replace('/', File.separatorChar)).delete();
	    
	    
	    return new ResponseEntity<String>("deleted", HttpStatus.OK);
	  }  

}
