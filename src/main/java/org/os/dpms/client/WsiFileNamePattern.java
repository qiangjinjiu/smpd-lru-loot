package org.os.dpms.client;

import java.util.regex.Pattern;

/**
 * 数字切片文件名模式
 * @author zhengjunjie
 *
 */
public interface WsiFileNamePattern {
	
	public Pattern getFilenamePattern();
	
	public String getFilename();
	
	public Uploadmeta getUploadmeta();
}
