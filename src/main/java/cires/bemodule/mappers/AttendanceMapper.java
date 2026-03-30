package cires.bemodule.mappers;

import cires.bemodule.dtos.AttendanceDTO;
import cires.bemodule.entities.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "attendanceStatus", target = "attendanceStatus")
    @Mapping(source = "sessionParticipant", target = "sessionParticipant")
    AttendanceDTO toAttemdanceDto(Attendance attendance);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "attendanceStatus", target = "attendanceStatus")
    @Mapping(source = "sessionParticipant", target = "sessionParticipant")
    Attendance toAttendance(AttendanceDTO attendanceDTO);

}