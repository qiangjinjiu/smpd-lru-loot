package org.os.dpms.client;

import java.util.regex.Pattern;

import com.google.common.base.Strings;

/**
 * 肾脏病理旧的数字切片文件名格式,Aperio扫描仪
 * @author zhengjunjie
 *
 */
public class SzblONPatternAperio implements WsiFileNamePattern{

	@Override
	public Uploadmeta getUploadmeta() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * KB\d{7}\w{2,}_\\d{1,}.svs
	 */
	@Override
	public Pattern getFilenamePattern() {
		// TODO Auto-generated method stub
		String regex = "KB\\d{7}\\w{2,}_\\d{1,}.svs";
		Pattern p = Pattern.compile(regex);
		
		return p;
	}

	@Override
	public String getFilename() {
		// TODO Auto-generated method stub
		return "KB1809717Masson_236661.svs";
	}
	
	public static void main(String[] args) {
		WsiFileNamePattern wsip = new SzblONPatternAperio();
		boolean b = wsip.getFilenamePattern().matcher(wsip.getFilename()).matches();
		System.out.println(b);
		String[] s = wsip.getFilenamePattern().split(wsip.getFilename());
		System.out.println(s.length);
		for(String f : s) {
			System.out.println(f);
		}
		System.out.println(wsip.getFilenamePattern().pattern());
		
		String[] s1 = "KB1809717Masson_236661.svs".split("KB\\d{7}");
		for(String f : s1) {
			System.out.println(f);
		}
	}

}
