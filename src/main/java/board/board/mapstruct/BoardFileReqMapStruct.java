package board.board.mapstruct;

import board.board.dto.BoardFileReqDto;
import board.board.entity.BoardFileEntity;
import board.common.mapper.EntityReqMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardFileReqMapStruct extends EntityReqMapper<BoardFileEntity, BoardFileReqDto>
{
    BoardFileReqMapStruct MAPPER = Mappers.getMapper(BoardFileReqMapStruct.class);
}
