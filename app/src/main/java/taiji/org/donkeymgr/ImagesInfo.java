package taiji.org.donkeymgr;

import java.util.ArrayList;
import java.util.List;

public class ImagesInfo {
	public int count;
	private List<String> images = new ArrayList<>();
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
}
