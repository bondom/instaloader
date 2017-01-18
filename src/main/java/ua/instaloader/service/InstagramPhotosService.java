package ua.instaloader.service;

import java.util.List;
import java.util.Map;

public interface InstagramPhotosService {
	
	/**
	 * Gets photos from accounts of instagram, specified in {@code instagrams}.
	 * <p>Returns Map where keys - values of retrieved {@code instagrams},
	 * values - Lists of urls of photos
	 * @param instagrams - List of accounts of instagram(without any slashes)
	 */
	public Map<String,List<String>> getImagesFromInstagrams(List<String> instagrams);
}
