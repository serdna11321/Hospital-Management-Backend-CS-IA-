package com.example.ia_cs.services;

import com.example.ia_cs.dataTransferObjects.UserControllerDTOs.*;
import com.example.ia_cs.entities.*;
import com.example.ia_cs.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;
    private final DoctorRepository doctorRepository;
    private final AdministratorRepository administratorRepository;

    public UserService(UserRepository userRepository, PersonRepository personRepository, PatientRepository patientRepository, RoleRepository roleRepository, DoctorRepository doctorRepository, AdministratorRepository administratorRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.patientRepository = patientRepository;
        this.roleRepository = roleRepository;
        this.doctorRepository = doctorRepository;
        this.administratorRepository = administratorRepository;
    }

    @Transactional
    public PatientDTO createPatientAccount(CreatePatientRequestDTO request){
        Person person;
        Optional<Person> optionalPerson = personRepository.findByNif(request.getNif());
        if (optionalPerson.isEmpty()){
            person = createNewPerson(request.getNif());
            createNewUser(request.getEmail(), request.getPasswordHash(), roleRepository.findByRoleName(RoleName.PATIENT), person);
        }
        else{
            person = personRepository.findByNif(request.getNif()).get();
            if (patientRepository.findByPersonID(person.getPersonID()).isPresent()){
                throw new RuntimeException("Patient Account Already Exists");
            }
            else{
                User user = userRepository.findByPerson(person).get();
                user.getRoles().add(roleRepository.findByRoleName(RoleName.PATIENT));
                userRepository.save(user);
            }
        }

        Patient patient = createPatient(request, person);
        PatientDTO patientDTO = createPatientDTO(patient);
        patientRepository.save(patient);
        return patientDTO;
    }

    @Transactional
    public DoctorDTO createDoctorAccount(CreateDoctorRequestDTO request){
        Person person;
        Optional<Person> optionalPerson = personRepository.findByNif(request.getNif());
        if (optionalPerson.isEmpty()){
            person = createNewPerson(request.getNif());
            createNewUser(request.getEmail(), request.getPasswordHash(), roleRepository.findByRoleName(RoleName.DOCTOR), person);
        }
        else{
            person = personRepository.findByNif(request.getNif()).get();
            if (doctorRepository.findByPersonID(person.getPersonID()).isPresent()){
                throw new RuntimeException("Doctor Account Already Exists");
            }
            else{
                User user = userRepository.findByPerson(person).get();
                user.getRoles().add(roleRepository.findByRoleName(RoleName.DOCTOR));
                userRepository.save(user);
            }
        }

        Doctor doctor = createDoctor(request, person);
        DoctorDTO doctorDTO = createDoctorDTO(doctor);
        doctorRepository.save(doctor);
        return doctorDTO;
    }

    @Transactional
    public AdministratorDTO createAdministratorAccount(CreateAdministratorRequestDTO request){
        Person person;
        Optional<Person> optionalPerson = personRepository.findByNif(request.getNif());
        if (optionalPerson.isEmpty()){
            person = createNewPerson(request.getNif());
            createNewUser(request.getEmail(), request.getPasswordHash(), roleRepository.findByRoleName(RoleName.ADMINISTRATOR), person);
        }
        else{
            person = personRepository.findByNif(request.getNif()).get();
            if (administratorRepository.existsWithPersonID(person.getPersonID())){
                throw new RuntimeException("Admin Account Already Exists");
            }
            else{
                User user = userRepository.findByPerson(person).get();
                user.getRoles().add(roleRepository.findByRoleName(RoleName.ADMINISTRATOR));
                userRepository.save(user);
            }
        }

        Administrator administrator = createAdministrator(request, person);
        AdministratorDTO administratorDTO = createAdministratorDTO(administrator);
        administratorRepository.save(administrator);
        return administratorDTO;
    }

    private Administrator createAdministrator(CreateAdministratorRequestDTO request, Person person) {
        Administrator administrator = new Administrator();
        administrator.setPerson(person);
        administrator.setFirstName(request.getFirstName());
        administrator.setLastName(request.getLastName());
        administrator.setStreetName(request.getStreetName());
        administrator.setCity(request.getCity());
        administrator.setStreetNumber(request.getStreetNumber());
        administrator.setTelephoneNumber(request.getTelephoneNumber());
        administrator.setDateOfBirth(request.getDateOfBirth());
        administrator.setPostcode(request.getPostcode());
        return administrator;
    }

    public Person createNewPerson(String nif){
        Person person = new Person();
        person.setNif(nif);
        personRepository.save(person);
        return person;
    }

    public void createNewUser(String email, String passwordHash, Role userRole, Person person){
        User user = new User();
        user.setUserEmail(email);
        user.setPasswordHash(passwordHash);
        user.getRoles().add(userRole);
        user.setPerson(person);
        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    private Patient createPatient(CreatePatientRequestDTO request, Person person) {
        Patient patient = new Patient();
        patient.setPerson(person);
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setStreetName(request.getStreetName());
        patient.setCity(request.getCity());
        patient.setStreetNumber(request.getStreetNumber());
        patient.setTelephoneNumber(request.getTelephoneNumber());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setPostcode(request.getPostcode());
        return patient;
    }

    private Doctor createDoctor(CreateDoctorRequestDTO request, Person person) {
        Doctor doctor = new Doctor();
        doctor.setPerson(person);
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setStreetName(request.getStreetName());
        doctor.setCity(request.getCity());
        doctor.setStreetNumber(request.getStreetNumber());
        doctor.setTelephoneNumber(request.getTelephoneNumber());
        doctor.setDateOfBirth(request.getDateOfBirth());
        doctor.setPostcode(request.getPostcode());
        doctor.setSpecialty(request.getSpecialty());
        return doctor;
    }

    private PatientDTO createPatientDTO(Patient patient){
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setCity(patient.getCity());
        patientDTO.setDateOfBirth(patient.getDateOfBirth());
        patientDTO.setFirstName(patient.getFirstName());
        patientDTO.setLastName(patient.getLastName());
        patientDTO.setPatientID(patient.getPatientID());
        patientDTO.setPostcode(patient.getPostcode());
        patientDTO.setStreetName(patient.getStreetName());
        patientDTO.setStreetNumber(patient.getStreetNumber());
        patientDTO.setTelephoneNumber(patient.getTelephoneNumber());
        return patientDTO;
    }

    private DoctorDTO createDoctorDTO(Doctor doctor){
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setCity(doctor.getCity());
        doctorDTO.setDateOfBirth(doctor.getDateOfBirth());
        doctorDTO.setFirstName(doctor.getFirstName());
        doctorDTO.setLastName(doctor.getLastName());
        doctorDTO.setDoctorID(doctor.getDoctorID());
        doctorDTO.setPostcode(doctor.getPostcode());
        doctorDTO.setStreetName(doctor.getStreetName());
        doctorDTO.setStreetNumber(doctor.getStreetNumber());
        doctorDTO.setTelephoneNumber(doctor.getTelephoneNumber());
        doctorDTO.setSpecialty(doctorDTO.getSpecialty());
        return doctorDTO;
    }

    private AdministratorDTO createAdministratorDTO(Administrator administrator){
        AdministratorDTO administratorDTO = new AdministratorDTO();
        administratorDTO.setCity(administrator.getCity());
        administratorDTO.setDateOfBirth(administrator.getDateOfBirth());
        administratorDTO.setFirstName(administrator.getFirstName());
        administratorDTO.setLastName(administrator.getLastName());
        administratorDTO.setAdministratorID(administrator.getAdministratorID());
        administratorDTO.setPostcode(administrator.getPostcode());
        administratorDTO.setStreetName(administrator.getStreetName());
        administratorDTO.setStreetNumber(administrator.getStreetNumber());
        administratorDTO.setTelephoneNumber(administrator.getTelephoneNumber());
        return administratorDTO;
    }
}