package cires.bemodule.mappers;

import cires.bemodule.dtos.views.AttendanceDTO;
import cires.bemodule.entities.Attendance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    AttendanceDTO toAttemdanceDto(Attendance attendance);

    Attendance toAttendance(AttendanceDTO attendanceDTO);

}