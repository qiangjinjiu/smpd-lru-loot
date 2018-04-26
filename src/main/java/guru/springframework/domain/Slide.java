package guru.springframework.domain;

import lombok.Getter;
import lombok.Setter;

public class Slide {
	@Getter @Setter private Long id;
	
	//upload meta
	@Getter @Setter private String caseId;
	@Getter @Setter private String slideId;
	@Getter @Setter private String file;
	@Getter @Setter private String title;
	@Getter @Setter private int expiry;
	@Getter @Setter private String md5sum;
	@Getter @Setter private String model="unknown";
	@Getter @Setter private String vendor="aperio";
	@Getter @Setter private Long fileSize;
	// upload meta
	
	@Getter @Setter private String rdvUrl;
	@Getter @Setter private String ingestCmd;
	@Getter @Setter private String localFilepath;
	@Getter @Setter private String folder;
	@Override
	public String toString() {
		return "Slide [id=" + id + ", caseId=" + caseId + ", slideId=" + slideId + ", file=" + file + ", title=" + title
				+ ", expiry=" + expiry + ", md5sum=" + md5sum + ", model=" + model + ", vendor=" + vendor
				+ ", fileSize=" + fileSize + ", rdvUrl=" + rdvUrl + ", ingestCmd=" + ingestCmd + ", localFilepath="
				+ localFilepath + ", folder=" + folder + "]";
	}
	

}
