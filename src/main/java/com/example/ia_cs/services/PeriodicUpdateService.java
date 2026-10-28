package com.example.ia_cs.services;

import com.example.ia_cs.dataTransferObjects.PeriodicUpdateDTOs.*;
import com.example.ia_cs.entities.*;
import com.example.ia_cs.repositories.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PeriodicUpdateService {

    private final AppointmentService appointmentService;
    private final DoctorRepository doctorRepository;
    private final RoleRepository roleRepository;
    private final PatientRepository patientRepository;
    private final ConnectionService connectionService;
    private static final int UPDATE_RATE = 1000*60*15;
    private final AppointmentRepository appointmentRepository;
    private final VacationRequestRepository vacationRequestRepository;
    private final UserRepository userRepository;
    private final UpdateSenderService updateSenderService;

    public PeriodicUpdateService(AppointmentService appointmentService, DoctorRepository doctorRepository, RoleRepository roleRepository, PatientRepository patientRepository, ConnectionService connectionService, AppointmentRepository appointmentRepository, VacationRequestRepository vacationRequestRepository, UserRepository userRepository, UpdateSenderService updateSenderService) {
        this.appointmentService = appointmentService;
        this.doctorRepository = doctorRepository;
        this.roleRepository = roleRepository;
        this.patientRepository = patientRepository;
        this.connectionService = connectionService;
        this.appointmentRepository = appointmentRepository;
        this.vacationRequestRepository = vacationRequestRepository;
        this.userRepository = userRepository;
        this.updateSenderService = updateSenderService;
    }

    @Scheduled(fixedRate = UPDATE_RATE)
    public void periodicUpdate(){
        for (User user : userRepository.findAll()){
            int userID = Math.toIntExact(user.getUserID());
            if (connectionService.isConnected((Math.toIntExact(user.getUserID())))){
                updateSenderService.sendUpdate(userID, createUpdateForUser(user));
            }
        }
    }

    public UpdateDTO createUpdateForUser(User user){
        UpdateDTO updateDTO = new UpdateDTO();
        if (user.getRoles().contains(roleRepository.findByName(RoleName.PATIENT))){
            updateDTO.setDoctorAvailability(prepareDoctorOccupiedDTO());
            updateDTO.setPatientAppointments(preparePatientAppointments(patientRepository.findByPersonPersonID(user.getPerson().getPersonID()).get()));
            updateDTO.setRescheduledAppointments(appointmentService.getAndRemoveRescheduledAppointments(patientRepository.findByPersonPersonID(user.getPerson().getPersonID()).get().getPatientID()));
        }
        if (user.getRoles().contains(roleRepository.findByName(RoleName.DOCTOR))){
            updateDTO.setDoctorAppointments(prepareDoctorAppointments(doctorRepository.findByPersonPersonID(user.getPerson().getPersonID()).get()));
            updateDTO.setDoctorVacationRequests(prepareDoctorVacationRequests(doctorRepository.findByPersonPersonID(user.getPerson().getPersonID()).get()));
        }
        if (user.getRoles().contains(roleRepository.findByName(RoleName.ADMINISTRATOR))){
            updateDTO.setAdministratorVacationRequests(prepareAdministratorVacationRequests());
        }
        return updateDTO;
    }

    private List<DoctorOccupiedDTO> prepareDoctorOccupiedDTO(){
        List<DoctorOccupiedDTO> doctorOccupiedDTOs = new ArrayList<>();
        for (Doctor doctor : doctorRepository.findAll()){
            DoctorOccupiedDTO doctorOccupiedDTO = new DoctorOccupiedDTO();
            doctorOccupiedDTO.setOccupiedDateTimes(appointmentService.getOccupiedDateTimesFromDate(doctor, LocalDate.now()));
            doctorOccupiedDTO.setDoctorID(doctor.getDoctorID());
            doctorOccupiedDTOs.add(doctorOccupiedDTO);
        }
        return doctorOccupiedDTOs;
    }

    private List<AppointmentDTO> preparePatientAppointments(Patient patient){
        List<AppointmentDTO> appointmentDTOs = new ArrayList();
        for (Appointment appointment : appointmentRepository.findByPatient(patient)){
            AppointmentDTO appointmentDTO = new AppointmentDTO();
            appointmentDTO.setAppointmentID(appointment.getAppointmentID());
            appointmentDTO.setDoctorName(appointment.getDoctor().getFirstName());
            appointmentDTO.setDate(appointment.getAppointmentDate());
            appointmentDTO.setTime(appointment.getAppointmentTime());
            appointmentDTO.setSpecialty(appointment.getDoctor().getSpecialty());
            appointmentDTO.setAppointmentStatus(appointment.getStatus());
            appointmentDTOs.add(appointmentDTO);
        }
        return appointmentDTOs;
    }

    private List<AppointmentDoctorDTO> prepareDoctorAppointments(Doctor doctor){
        List<AppointmentDoctorDTO> appointmentDoctorDTOs = new ArrayList<>();
        for (Appointment appointment : appointmentRepository.findByDoctor(doctor)){
            AppointmentDoctorDTO appointmentDoctorDTO = new AppointmentDoctorDTO();
            appointmentDoctorDTO.setAppointmentID(appointment.getAppointmentID());
            appointmentDoctorDTO.setDate(appointment.getAppointmentDate());
            appointmentDoctorDTO.setPatientName(appointment.getPatient().getFirstName());
            appointmentDoctorDTO.setTime(appointment.getAppointmentTime());
            appointmentDoctorDTOs.add(appointmentDoctorDTO);
        }
        return appointmentDoctorDTOs;
    }

    private List<VacationRequestDTO> prepareDoctorVacationRequests(Doctor doctor){
        List<VacationRequestDTO> vacationRequestDTOs = new ArrayList<>();
        for (VacationRequest vacationRequest : vacationRequestRepository.findByDoctor(doctor)){
            VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
            vacationRequestDTO.setDoctorName(doctor.getFirstName());
            vacationRequestDTO.setVacationRequestID(vacationRequest.getVacationRequestID());
            vacationRequestDTO.setStartDate(vacationRequest.getStartDate());
            vacationRequestDTO.setEndDate(vacationRequest.getEndDate());
            vacationRequestDTO.setVacationRequestStatus((vacationRequest.getStatus()));
            vacationRequestDTOs.add(vacationRequestDTO);
        }
        return vacationRequestDTOs;
    }

    private List<VacationRequestDTO> prepareAdministratorVacationRequests(){
        List<VacationRequestDTO> vacationRequestDTOs = new ArrayList<>();
        for (VacationRequest vacationRequest : vacationRequestRepository.findAll()){
            VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
            vacationRequestDTO.setDoctorName(vacationRequest.getDoctor().getFirstName());
            vacationRequestDTO.setVacationRequestID(vacationRequest.getVacationRequestID());
            vacationRequestDTO.setStartDate(vacationRequest.getStartDate());
            vacationRequestDTO.setEndDate(vacationRequest.getEndDate());
            vacationRequestDTO.setVacationRequestStatus((vacationRequest.getStatus()));
            vacationRequestDTOs.add(vacationRequestDTO);
        }
        return vacationRequestDTOs;
    }
}
