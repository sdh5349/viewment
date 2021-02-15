package com.web.curation.dto.memory;


import com.web.curation.domain.Memory;
import lombok.Getter;
import lombok.Setter;



/**
 * com.web.curation.dto.memory
 * MemoryDto.java
 * @date    2021-01-27 오후 4:00
 * @author  김종성
 *
 * @변경이력
 * 이주희 21-02-09 핀 삭제
 **/
@Getter
@Setter
public class MemoryDto {
    private Long memoryId;
    private String name;
    private int radius;
    private boolean notification;

    private double lat;
    private double lng;

    public MemoryDto(){}

    public MemoryDto(Memory memory){
        this.memoryId = memory.getId();
        this.name = memory.getName();
        this.radius = memory.getRadius();
        this.lat = memory.getLocation().getY();//위도
        this.lng = memory.getLocation().getX();//경도
    }
}
