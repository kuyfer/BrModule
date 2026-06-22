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
//
//    @Mapping(source = "sessionId",    target = "sessionId")
//    @Mapping(source = "sessionTitle", target = "sessionTitle")
//    @Mapping(source = "participantId", target = "participantId")
//    @Mapping(expression = "java(attendance.getParticipant().getFirstName() + \" \" + attendance.getParticipant().getLastName())",
//            target = "participantFullName")
    AttendanceResponse toAttendanceResponse(Attendance attendance);

    AttendanceResponse toResponse(Attendance saved);
}