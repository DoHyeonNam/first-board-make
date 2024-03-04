package board.common.mapper;

import java.util.List;

public interface DtoResMapper<Dreq,Dres> {
    Dres toDto(Dreq dto);
    List<Dres> toDto(List<Dreq> dtos);
}
