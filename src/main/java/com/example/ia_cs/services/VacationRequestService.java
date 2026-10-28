package com.example.ia_cs.services;

import com.example.ia_cs.dataTransferObjects.PeriodicUpdateDTOs.RescheduledAppointmentDTO;
import com.example.ia_cs.dataTransferObjects.VacationRequestControllerDTOs.EvaluateVacationRequestDTO;
import com.example.ia_cs.dataTransferObjects.VacationRequestControllerDTOs.CreateVacationRequestDTO;
import com.example.ia_cs.entities.Appointment;
import com.example.ia_cs.entities.Doctor;
import com.example.ia_cs.entities.VacationRequest;
import com.example.ia_cs.entities.VacationRequestStatus;
import com.example.ia_cs.repositories.AppointmentRepository;
import com.example.ia_cs.repositories.DoctorRepository;
import com.example.ia_cs.repositories.UserRepository;
import com.example.ia_cs.repositories.VacationRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class VacationRequestService {

    private final VacationRequestRepository vacationRequestRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final AppointmentService appointmentService;


    public VacationRequestService(VacationRequestRepository vacationRequestRepository, AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, UserRepository userRepository, AppointmentService appointmentService) {
        this.vacationRequestRepository = vacationRequestRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.appointmentService = appointmentService;
    }

    public VacationRequest requestVacation(CreateVacationRequestDTO request, long userID){
        Optional<Doctor> optionalDoctor = doctorRepository.findByPersonPersonID(userRepository.findByUserID(userID).get().getPerson().getPersonID());
        if (optionalDoctor.isEmpty()) throw new RuntimeException("Doctor does not exist");
        Doctor doctor = optionalDoctor.get();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        if (existsVacationRequestBetweenDatesAndNotStatusAndDoctor(VacationRequestStatus.DENIED, doctor, startDate, endDate)){
            throw new RuntimeException("There is already an approved or pending vacation request within these dates");
        }
        VacationRequest vacationRequest = new VacationRequest();
        vacationRequest.setDoctor(doctor);
        vacationRequest.setStartDate(startDate);
        vacationRequest.setStatus(VacationRequestStatus.PENDING);

         markAffectedAppointments(doctor, startDate, endDate, vacationRequest);
         return vacationRequest;
    }

    public void markAffectedAppointments(Doctor doctor, LocalDate startDate, LocalDate endDate, VacationRequest vacationRequest){
        List<Appointment> affectedAppointments = appointmentRepository.findByDoctorBetweenDates(doctor, startDate, endDate);
        for (Appointment appointment : affectedAppointments){
            appointment.setAffectingVacationRequest(vacationRequest);
        }
    }

    public VacationRequest approveVacationRequest(EvaluateVacationRequestDTO request){
        Optional<VacationRequest> optionalVacationRequest = vacationRequestRepository.findByVacationRequestID(request.getVacationRequestID());
        if (optionalVacationRequest.isEmpty()) throw new RuntimeException("Vacation Request does not exist");
        VacationRequest vacationRequest = optionalVacationRequest.get();
        vacationRequest.setStatus(VacationRequestStatus.APPROVED);
        rescheduleAffectedAppointments(vacationRequest);
        return vacationRequest;
    }

    public VacationRequest denyVacationRequest(EvaluateVacationRequestDTO request){
        Optional<VacationRequest> optionalVacationRequest = vacationRequestRepository.findByVacationRequestID(request.getVacationRequestID());
        if (optionalVacationRequest.isEmpty()) throw new RuntimeException("Vacation Request does not exist");
        VacationRequest vacationRequest = optionalVacationRequest.get();
        vacationRequest.setStatus(VacationRequestStatus.DENIED);
        removeAppointmentsFromAffectedAppointments(vacationRequest);
        return vacationRequest;
    }

    public void rescheduleAffectedAppointments(VacationRequest vacationRequest){
        for (Appointment appointment : appointmentRepository.findByAffectingVacationRequest(vacationRequest)){
            RescheduledAppointmentDTO rescheduledAppointmentDTO = new RescheduledAppointmentDTO();
            Doctor doctor = appointment.getDoctor();
            rescheduledAppointmentDTO.setOldDate(appointment.getAppointmentDate());
            rescheduledAppointmentDTO.setOldTime(appointment.getAppointmentTime());
            rescheduledAppointmentDTO.setDoctorName(doctor.getFirstName() + doctor.getLastName());
            rescheduledAppointmentDTO.setDoctorID(doctor.getDoctorID());
            rescheduledAppointmentDTO.setPatientID(appointment.getPatient().getPatientID());
            LocalDateTime newDateTime = appointmentService.findNextAvailableDateTime(vacationRequest.getDoctor());
            LocalDate newDate = newDateTime.toLocalDate();
            LocalTime newTime = newDateTime.toLocalTime();
            appointment.setAppointmentTime(newTime);
            appointment.setAppointmentDate(newDate);
            rescheduledAppointmentDTO.setNewDate(newDate);
            rescheduledAppointmentDTO.setNewTime(newTime);
            appointmentService.addToRescheduledAppointmentsList((rescheduledAppointmentDTO));
        }
    }

    public void removeAppointmentsFromAffectedAppointments(VacationRequest vacationRequest){
        List<Appointment> affectedAppointments = appointmentRepository.findByDoctorBetweenDates(vacationRequest.getDoctor(), vacationRequest.getStartDate(), vacationRequest.getEndDate());
        for (Appointment appointment : affectedAppointments){
            appointment.setAffectingVacationRequest(null);
        }
    }

    private boolean existsVacationRequestBetweenDatesAndNotStatusAndDoctor(VacationRequestStatus status, Doctor doctor, LocalDate startDate, LocalDate endDate){
        for (VacationRequest vacationRequest : vacationRequestRepository.findAll()){
            if (vacationRequest.getDoctor() == doctor) {
                if (vacationRequest.getStartDate().isAfter(startDate) && vacationRequest.getStartDate().isBefore(endDate) || vacationRequest.getStartDate().isBefore(startDate) && vacationRequest.getEndDate().isAfter(startDate)){
                    if (vacationRequest.getStatus()!=status){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
