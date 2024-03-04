package board.board.mapstruct;

import board.board.dto.BoardReqDto;
import board.board.dto.BoardResDto;
import board.common.mapper.DtoResMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardDtoReqMapStruct extends DtoResMapper<BoardResDto, BoardReqDto> {

}
