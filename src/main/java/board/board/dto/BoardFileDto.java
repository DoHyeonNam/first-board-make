package board.board.dto;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "BoardDto : 게시글 파일", description = "게시글 파일")
@Data
public class BoardFileDto {
	
	private int idx;
	
	private int boardIdx;
	
	private String originalFileName;
	
	private String storedFilePath;
	
	private long fileSize;

	private MultipartFile filelist; // multipart 수정된 부분
}
