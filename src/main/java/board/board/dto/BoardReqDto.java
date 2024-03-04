package board.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;


@Data
@NoArgsConstructor // 매개변수가 없는 기본 생성자를 생성해줌
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 생성해줌
public class BoardReqDto {

    private int boardIdx;

    private String title;

    private String contents;

    private int hitCnt;

    private String creatorId;

    private String createdDatetime;

    private String updaterId;

    private String updatedDatetime;

    private List<BoardFileReqDto> fileList;
}
