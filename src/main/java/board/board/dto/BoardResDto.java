package board.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("BOARD")
public class BoardResDto {


    private int boardIdx;


    private String title;


    private String contents;


    private int hitCnt;

    private String creatorId;


    private String createdDatetime;

    private String updaterId;

    private String updatedDatetime;

    private List<BoardFileResDto> fileList;
}
