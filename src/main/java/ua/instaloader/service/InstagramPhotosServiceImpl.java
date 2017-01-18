package ua.instaloader.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ua.instaloader.service.exception.InvalidUrlException;


@Service
public class InstagramPhotosServiceImpl implements InstagramPhotosService{

	private final static String INSTAGRAM_URL="www.instagram.com/";
	
	private final Logger logger = LoggerFactory.getLogger(InstagramPhotosServiceImpl.class);
	
	@Override
	public Map<String,List<String>>  getImagesFromInstagrams(List<String> instagrams) {
		logger.info("Retrieved accounts of instagram: " + instagrams.toString());
		ExecutorService executor = Executors
				.newFixedThreadPool(instagrams.size());
		List<CompletableFuture<List<String>>> 
							futuresOfAllInstagrams =instagrams
								.stream()
								.map(instagramAcc -> INSTAGRAM_URL+instagramAcc)
								.map(instagramUrl -> CompletableFuture
											.supplyAsync(() -> downloadInstagramPage(instagramUrl), executor)
											.handle((ok,ex)->{
												if(ok==null){
													return "";
												} return ok;
											}))
								.map(htmlInstaFuture -> htmlInstaFuture.thenApply(
														htmlInsta -> getListOfUrlsOfPhotos(htmlInsta)))
								.collect(Collectors.toList());
		CompletableFuture<List<List<String>>> futureOfLists 
								= sequence(futuresOfAllInstagrams);
		Map<String,List<String>> map = new LinkedHashMap<>((int)Math.ceil(instagrams.size()/0.75));
		try {
			List<List<String>> result= futureOfLists.get();
			for(int i=0;i<instagrams.size();i++){
				String acc = instagrams.get(i);
				List<String> listOfUrls = (i<result.size())?result.get(i):null;
				map.put(acc, listOfUrls);
				logger.debug("Result Map:key={},value={}",acc,listOfUrls);
			}
			return map;
		} catch (InterruptedException | ExecutionException e) {
			logger.error((e.getCause()==null)?"":e.getCause()+":"+e.getMessage());
			return null;
		}
	}
	
	
	private String downloadInstagramPage(final String fullUrlOfAccount){
		String site="";
		try {
			site = IOUtils.toString(new URL("https://"+fullUrlOfAccount).openStream(), 
									 StandardCharsets.UTF_8.toString());
			logger.trace("Html page of instagram acc with url={}:",site);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new InvalidUrlException(e);
		}
		return site;
	}
	
	private List<String> getListOfUrlsOfPhotos(String htmlInstagram){
		if(htmlInstagram.equals("")){
			return new ArrayList<>();
		}
		//get scripts from body
		Pattern pattern = Pattern.compile("<script type=\"text/javascript\">.*</script>");
		Matcher matcher = pattern.matcher(htmlInstagram.substring(htmlInstagram.indexOf("</head>")));
		String scripts="";
		if(matcher.find() && scripts.equals("")){
			scripts = matcher.group();
		}
		
		//get url of all big images from scripts
		Pattern patternImage=Pattern.compile("(https:\\/\\/scontent\\.cdninstagram\\.com\\/t51\\.2885-[0-9][0-9]\\/e[0-9][0-9]\\/)([a-z]??([0-9]++\\.){1,}+[0-9]++\\/)*+(([0-9]*+_)+n.jpg)");
		Matcher imageUrlMatcher = patternImage.matcher(scripts);
		List<String> urlsOfImages = new ArrayList<String>();
		while(imageUrlMatcher.find()){
			String url=imageUrlMatcher.group();
			urlsOfImages.add(url);
			logger.debug("Parsed url of photo: "+url);
		}
		return urlsOfImages;
	}
	
	private <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
	    CompletableFuture<Void> allDoneFuture =
	        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
	    return allDoneFuture.thenApply(v ->
	            futures.stream()
	            	   .map(future -> future.join())
	            	   .collect(Collectors.<T>toList()));
	}
}
