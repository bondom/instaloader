package ua.instaloader.web;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ua.instaloader.service.InstagramPhotosService;

@RestController
public class AjaxController {
	
	@Autowired
	private InstagramPhotosService service;
	
	private final Logger logger = LoggerFactory.getLogger(AjaxController.class);
	@GetMapping("/loadimages")
	public Map<String,List<String>> imagesFromInstagrams(@RequestParam("instagrams[]") 
											 List<String> instagrams){
		List<String> notEmptyInstagrams =
				instagrams.stream().filter(acc -> {return !acc.equals("");}).collect(Collectors.toList());
		if(notEmptyInstagrams.size()==0){
			return new HashMap<>();
		}
		logger.debug("Inputted inst accs:{}",instagrams.toString());
		Map<String,List<String>> map = service.getImagesFromInstagrams(notEmptyInstagrams);
		return map;
	}
}
