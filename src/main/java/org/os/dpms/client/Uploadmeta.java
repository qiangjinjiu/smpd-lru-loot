package org.os.dpms.client;

import lombok.Getter;
import lombok.Setter;

/**
 * DPMS的uploadmeta
 * @author zhengjunjie
 *
 */
public class Uploadmeta {
	@Getter @Setter private String caseId;
	@Getter @Setter private String slideId;
	@Getter @Setter private String file;
	@Getter @Setter private String title;
	@Getter @Setter private int expiry;
	@Getter @Setter private String md5sum;
	@Getter @Setter private String model;
	@Getter @Setter private String vendor;
	@Getter @Setter private Long fileSize;
}
