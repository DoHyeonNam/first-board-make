package board.board.mapstruct;

import board.board.dto.BoardFileResDto;
import board.board.entity.BoardFileEntity;
import board.common.mapper.EntityResMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 엔티티와 DTO 간의 매핑을 자동화하기 위한 Mapper 인터페이스
 *
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
    // MapStruct 가 스프링의 컴포넌트로 등록되어야 함을 지정한다
    // unmappedTargetPolicy = ReportingPolicy.IGNORE 매핑 중에 대상이나 소스에서 발견되지 않는 매핑 대상을 무시하도록 지정한다
public interface BoardFileResMapStruct extends EntityResMapper<BoardFileEntity, BoardFileResDto> {

    @Mapping(source = "boardEntity.boardIdx", target = "boardIdx") // boardEntity.boardIdx 필드를 boardIdx로 매핑
    BoardFileResDto toDto(BoardFileEntity entity); // BoardFileEntity 객체를 BoardFileResDto 객체로 매핑하는데 사용

    List<BoardFileResDto> toDto(List<BoardFileEntity> entities); // BoardFileEntity 객체를 BoardFileResDto 객체로 매핑하는데 사용
}