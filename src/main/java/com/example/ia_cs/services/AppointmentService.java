package com.example.ia_cs.services;

import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.CancelAppointmentRequestDTO;
import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.CreateAppointmentRequestDTO;
import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.ChangeAppointmentRequestDTO;
import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.CreateMedicalRequestDTO;
import com.example.ia_cs.dataTransferObjects.PeriodicUpdateDTOs.RescheduledAppointmentDTO;
import com.example.ia_cs.entities.*;
import com.example.ia_cs.repositories.AppointmentRepository;
import com.example.ia_cs.repositories.DoctorRepository;
import com.example.ia_cs.repositories.PatientRepository;
import com.example.ia_cs.repositories.VacationRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final VacationRequestRepository vacationRequestRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final List<RescheduledAppointmentDTO> rescheduledAppointmentsDTOs = new ArrayList<>();
    private final MedicalRecordService medicalRecordService;


    public void addToRescheduledAppointmentsList(RescheduledAppointmentDTO rescheduledAppointmentDTO){
        rescheduledAppointmentsDTOs.add(rescheduledAppointmentDTO);
    }

    public void removeFromRescheduledAppointmentsList(RescheduledAppointmentDTO rescheduledAppointmentDTO){
        rescheduledAppointmentsDTOs.remove(rescheduledAppointmentDTO);
    }

    public AppointmentService(AppointmentRepository appointmentRepository, VacationRequestRepository vacationRequestRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, MedicalRecordService medicalRecordService){
        this.appointmentRepository=appointmentRepository;
        this.vacationRequestRepository = vacationRequestRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.medicalRecordService = medicalRecordService;
    }

    public Appointment requestAppointment(CreateAppointmentRequestDTO request, long userID){
        Doctor doctor = getDoctorFromID(request.getDoctorID());
        Patient patient = patientRepository.getByUserID(userID);
        LocalDate date = request.getDate();
        LocalTime time = request.getTime();
        boolean occupied = isTimeSlotOccupied(doctor, patient, date, time);
        if (occupied){
            throw new RuntimeException("Either the doctor or patient are already occupied at this time.");
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(date);
        appointment.setAppointmentTime(time);
        appointment.setStatus(AppointmentStatus.BOOKED);

        return appointmentRepository.save(appointment);
    }

    public Appointment cancelAppointment(CancelAppointmentRequestDTO request){
        Optional<Appointment> optionalAppointment = appointmentRepository.getAppointmentByAppointmentID(request.getAppointmentID());
        if (optionalAppointment.isEmpty()){
            throw new RuntimeException("Appointment does not exist");
        }
        Appointment appointment = optionalAppointment.get();
        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointment;
    }

    public List<LocalDateTime> getOccupiedDateTimesFromDate(Doctor doctor, LocalDate date) {

        List<Appointment> appointments =
                appointmentRepository.findByDoctorBetweenDates(doctor, LocalDate.now(), LocalDate.now().plusDays(30));

        return appointments.stream().map(appointment -> LocalDateTime.of(appointment.getAppointmentDate(), appointment.getAppointmentTime())).toList();
    }

    public Appointment changeAppointmentDateTime(ChangeAppointmentRequestDTO request){
        Optional<Appointment> optionalAppointment = appointmentRepository.getAppointmentByAppointmentID(request.getAppointmentID());
        if (optionalAppointment.isEmpty()){
            throw new RuntimeException("Appointment does not exist");
        }
        Appointment appointment = optionalAppointment.get();
        LocalDate newDate = request.getNewDate();
        LocalTime newTime = request.getNewTime();
        boolean occupied = isTimeSlotOccupied(appointment.getDoctor(), appointment.getPatient(), newDate, newTime);
        if (occupied){
            throw new RuntimeException("Appointment cannot be changed to this time because the patient or doctor are not available.");
        }
        else{
            appointment.setAppointmentDate(newDate);
            appointment.setAppointmentTime(newTime);
        }
        return appointment;
    }

    public Doctor getDoctorFromID(long doctorID){
        Optional<Doctor> doctor = doctorRepository.getByDoctorID(doctorID);
        if (doctor.isPresent()){
            return doctor.get();
        }
        else {
            throw new RuntimeException("Doctor Not Found");
        }
    }

    public Patient getPatientFromID(long patientID){
        Optional<Patient> patient = patientRepository.getByPatientID(patientID);
        if (patient.isPresent()){
            return patient.get();
        }
        else {
            throw new RuntimeException("Doctor Not Found");
        }
    }

    public boolean isTimeSlotOccupied(Doctor doctor, Patient patient, LocalDate date, LocalTime time){
        return !((appointmentRepository.existsByDoctorAndDateAtTimeAndStatus(doctor, date, time, AppointmentStatus.BOOKED)) || (appointmentRepository.existsByPatientAndDateAtTimeAndStatus(patient, date, time, AppointmentStatus.BOOKED)) || (vacationRequestRepository.existsVacationRequestAtDateWithStatus(date, VacationRequestStatus.APPROVED)));
    }

    public LocalDateTime findNextAvailableDateTime(Doctor doctor){
        return appointmentRepository.findNextAvailableDateTimeFromDateForDoctor(LocalDate.now(), doctor);
    }

    public List<RescheduledAppointmentDTO> getAndRemoveRescheduledAppointments(Long patientID) {
        List<RescheduledAppointmentDTO> result = new ArrayList<>();
        Iterator<RescheduledAppointmentDTO> iterator =
                rescheduledAppointmentsDTOs.iterator();
        while (iterator.hasNext()) {
            RescheduledAppointmentDTO dto = iterator.next();
            if (dto.getPatientID() == patientID) {
                result.add(dto);
                iterator.remove();
            }
        }
        return result;
    }

    public MedicalRecord addMedicalRecord(CreateMedicalRequestDTO createMedicalRequestDTO){
        Optional<Patient> optionalPatient = patientRepository.getByPatientID(createMedicalRequestDTO.getPatientID());
        if (optionalPatient.isEmpty()){
            throw new RuntimeException("Patient not found");
        }
        Patient patient = optionalPatient.get();
        return medicalRecordService.addMedicalRecord(patient, createMedicalRequestDTO.getDate(), createMedicalRequestDTO.getDescription());
    }
}
