package board.board.mapstruct;

import board.board.dto.BoardFileReqDto;
import board.board.dto.BoardFileResDto;
import board.common.mapper.DtoResMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardFileDtoReqMapStruct extends DtoResMapper<BoardFileResDto, BoardFileReqDto> {

}
