package com.example.ia_cs.services;

import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.CancelAppointmentRequestDTO;
import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.CreateAppointmentRequestDTO;
import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.ChangeAppointmentRequestDTO;
import com.example.ia_cs.dataTransferObjects.PeriodicUpdateDTOs.RescheduledAppointmentDTO;
import com.example.ia_cs.entities.*;
import com.example.ia_cs.repositories.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final VacationRequestRepository vacationRequestRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final List<RescheduledAppointmentDTO> rescheduledAppointmentsDTOs = new ArrayList<>();
    private final UserRepository userRepository;


    public void addToRescheduledAppointmentsList(RescheduledAppointmentDTO rescheduledAppointmentDTO){
        rescheduledAppointmentsDTOs.add(rescheduledAppointmentDTO);
    }

    public void removeFromRescheduledAppointmentsList(RescheduledAppointmentDTO rescheduledAppointmentDTO){
        rescheduledAppointmentsDTOs.remove(rescheduledAppointmentDTO);
    }

    public AppointmentService(AppointmentRepository appointmentRepository, VacationRequestRepository vacationRequestRepository, DoctorRepository doctorRepository, PatientRepository patientRepository,  UserRepository userRepository){
        this.appointmentRepository=appointmentRepository;
        this.vacationRequestRepository = vacationRequestRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    public void requestAppointment(CreateAppointmentRequestDTO request, long userID){
        Doctor doctor = getDoctorFromID(request.getDoctorID());
        Patient patient = patientRepository.findByPersonPersonID(userRepository.findByUserID(userID).get().getPerson().getPersonID()).get();
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

        appointmentRepository.save(appointment);
    }

    public void cancelAppointment(CancelAppointmentRequestDTO request){
        Optional<Appointment> optionalAppointment = appointmentRepository.findByAppointmentID(request.getAppointmentID());
        if (optionalAppointment.isEmpty()){
            throw new RuntimeException("Appointment does not exist");
        }
        Appointment appointment = optionalAppointment.get();
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }

    public List<LocalDateTime> getOccupiedDateTimesFromDate(Doctor doctor, LocalDate date) {

        List<Appointment> appointments =
                appointmentRepository.findByDoctorBetweenDates(doctor, LocalDate.now(), LocalDate.now().plusDays(30));

        return appointments.stream().map(appointment -> LocalDateTime.of(appointment.getAppointmentDate(), appointment.getAppointmentTime())).toList();
    }

    public List<LocalDateTime> getAvailableSlots(LocalDate date, Long doctorId) {
        List<LocalDateTime> occupiedSlots = getOccupiedDateTimesFromDate(doctorRepository.findByDoctorID(doctorId).get(), date);
        Set<LocalDateTime> occupiedSet = new HashSet<>(occupiedSlots);

        List<LocalDateTime> availableSlots = new ArrayList<>();

        LocalTime workStart = LocalTime.of(9, 0);   // 9:00 AM
        LocalTime workEnd = LocalTime.of(17, 0);   // 5:00 PM
        int slotDurationMinutes = 30;

        LocalDate currentDate = date;
        LocalDate finalDate = date.plusDays(30);

        while (!currentDate.isAfter(finalDate)){
            LocalDateTime currentSlot = currentDate.atTime(workStart);
            LocalDateTime endOfDay = currentDate.atTime(workEnd);

            while (currentSlot.isBefore(endOfDay)) {
                if (!occupiedSet.contains(currentSlot)) {
                    availableSlots.add(currentSlot);
                }

                currentSlot = currentSlot.plusMinutes(slotDurationMinutes);
            }
            currentDate=currentDate.plusDays(1);
        }

        return availableSlots;
    }

    public void changeAppointmentDateTime(ChangeAppointmentRequestDTO request){
        Optional<Appointment> optionalAppointment = appointmentRepository.findByAppointmentID(request.getAppointmentID());
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
    }

    public Doctor getDoctorFromID(long doctorID){
        Optional<Doctor> doctor = doctorRepository.findByDoctorID(doctorID);
        if (doctor.isPresent()){
            return doctor.get();
        }
        else {
            throw new RuntimeException("Doctor Not Found");
        }
    }

    public boolean isTimeSlotOccupied(Doctor doctor, Patient patient, LocalDate date, LocalTime time){
        return !((appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTimeAndStatus(doctor, date, time, AppointmentStatus.BOOKED)) || (appointmentRepository.existsByPatientAndAppointmentDateAndAppointmentTimeAndStatus(patient, date, time, AppointmentStatus.BOOKED)) || (vacationRequestRepository.existsVacationRequestByDateAndStatus(date, VacationRequestStatus.APPROVED)));
    }

    public LocalDateTime findNextAvailableDateTime(Doctor doctor){
        List<LocalDateTime> occupiedSlots = getOccupiedDateTimesFromDate(doctorRepository.findByDoctorID(doctor.getDoctorID()).get(), LocalDate.now());
        Set<LocalDateTime> occupiedSet = new HashSet<>(occupiedSlots);

        LocalDate currentDate = LocalDate.now();
        LocalTime workStart = LocalTime.of(9, 0);   // 9:00 AM
        LocalTime workEnd = LocalTime.of(17, 0);   // 5:00 PM
        int slotDurationMinutes = 30;
        while (true){
            LocalDateTime currentSlot = currentDate.atTime(workStart);
            LocalDateTime endOfDay = currentDate.atTime(workEnd);

            while (currentSlot.isBefore(endOfDay)) {
                if (!occupiedSet.contains(currentSlot)) {
                    return currentSlot;
                }

                currentSlot = currentSlot.plusMinutes(slotDurationMinutes);
            }
            currentDate=currentDate.plusDays(1);

        }
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
}
