package board.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor // 매개변수가 없는 기본 생성자를 생성해줌
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 생성해줌
public class BoardFileResDto {

    private int idx;

    private int boardIdx;

    private String originalFileName;

    private String storedFilePath;

    private long fileSize;

    private String creatorId;

    private LocalDateTime createdDatetime = LocalDateTime.now();

    private String updaterId;

    private LocalDateTime updatedDatetime;

    private List<BoardFileReqDto> fileList;

}
