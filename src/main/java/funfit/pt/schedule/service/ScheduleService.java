package funfit.pt.schedule.service;

import funfit.pt.exception.ErrorCode;
import funfit.pt.exception.customException.BusinessException;
import funfit.pt.relationship.entity.Relationship;
import funfit.pt.relationship.repository.RelationshipRepository;
import funfit.pt.schedule.dto.AddScheduleRequest;
import funfit.pt.schedule.dto.AddScheduleResponse;
import funfit.pt.schedule.entity.Schedule;
import funfit.pt.schedule.repository.ScheduleRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final RelationshipRepository relationshipRepository;

    public AddScheduleResponse addSchedule(AddScheduleRequest addScheduleRequest, long relationshipId, HttpServletRequest request) {
        Relationship relationship = relationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        validateDuplicate(addScheduleRequest.getDate(), relationship.getTrainerUserId());

        Schedule schedule = Schedule.create(relationship, addScheduleRequest.getDate(), addScheduleRequest.getMemo());
        scheduleRepository.save(schedule);
        return new AddScheduleResponse(schedule.getDate(), schedule.getMemo());
    }

    private void validateDuplicate(LocalDateTime date, long trainerUserId) {
        List<Schedule> schedules = scheduleRepository.findByTrainerUserId(trainerUserId);
        boolean isAlreadyExist = schedules.stream()
                .anyMatch(schedule -> schedule.getDate().equals(date));
        if (isAlreadyExist) {
            throw new BusinessException(ErrorCode.ALREADY_RESERVATION);
        }
    }
}
