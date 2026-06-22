package cires.bemodule.mappers;

import cires.bemodule.dtos.responses.AttendanceResponse;
import cires.bemodule.dtos.views.AttendanceDTO;
import cires.bemodule.entities.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    AttendanceDTO toAttemdanceDto(Attendance attendance);

    Attendance toAttendance(AttendanceDTO attendanceDTO);

    @Mapping(source = "session.id",           target = "sessionId")
    @Mapping(source = "session.title",        target = "sessionTitle")
    @Mapping(source = "participant.id",       target = "participantId")
    AttendanceResponse toAttendanceResponse(Attendance attendance);

    AttendanceResponse toResponse(Attendance saved);
}