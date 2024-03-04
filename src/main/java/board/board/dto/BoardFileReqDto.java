package board.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor // 매개변수가 없는 기본 생성자를 생성해줌
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 생성해줌
public class BoardFileReqDto {
	
	private int idx;

	private int boardIdx;
	
	private String originalFileName; // 업로드 된 파일의 원본 파일 이름을 나타내는 문자열 필드
	
	private String storedFilePath; // 서버에 저장된 파일의 경로를 나타내는 문자열 필드
	
	private long fileSize; // 업로드 된 파일의 크기를 나타내는 정수형 필드

	private String creatorId;

	private LocalDateTime createdDatetime = LocalDateTime.now();

	private String updaterId;

	private LocalDateTime updatedDatetime;

}
