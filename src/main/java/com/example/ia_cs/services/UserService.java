package com.example.ia_cs.services;

import com.example.ia_cs.dataTransferObjects.UserControllerDTOs.*;
import com.example.ia_cs.entities.*;
import com.example.ia_cs.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PersonRepository personRepository, PatientRepository patientRepository, RoleRepository roleRepository, DoctorRepository doctorRepository, AdministratorRepository administratorRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.patientRepository = patientRepository;
        this.roleRepository = roleRepository;
        this.doctorRepository = doctorRepository;
        this.administratorRepository = administratorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createPatientAccount(CreatePatientRequestDTO request){
        Person person;
        Optional<Person> optionalPerson = personRepository.findByNif(request.getNif());
        if (optionalPerson.isEmpty()){
            person = createNewPerson(request.getNif());
            createNewUser(request.getEmail(), request.getPasswordHash(), roleRepository.findByName(RoleName.PATIENT), person);
        }
        else{
            person = optionalPerson.get();
            if (patientRepository.findByPersonPersonID(person.getPersonID()).isPresent()){
                throw new RuntimeException("Patient Account Already Exists");
            }
            else{

                User user = userRepository.findByPerson(person).get();
                user.getRoles().add(roleRepository.findByName(RoleName.PATIENT));
                userRepository.save(user);
            }
        }

        Patient patient = createPatient(request, person);
        patientRepository.save(patient);
    }

    @Transactional
    public void createDoctorAccount(CreateDoctorRequestDTO request){
        Person person;
        Optional<Person> optionalPerson = personRepository.findByNif(request.getNif());
        if (optionalPerson.isEmpty()){
            person = createNewPerson(request.getNif());
            createNewUser(request.getEmail(), request.getPasswordHash(), roleRepository.findByName(RoleName.DOCTOR), person);
        }
        else{
            person = personRepository.findByNif(request.getNif()).get();
            if (doctorRepository.findByPersonPersonID(person.getPersonID()).isPresent()){
                throw new RuntimeException("Doctor Account Already Exists");
            }
            else{
                User user = userRepository.findByPerson(person).get();
                user.getRoles().add(roleRepository.findByName(RoleName.DOCTOR));
                userRepository.save(user);
            }
        }

        Doctor doctor = createDoctor(request, person);
        doctorRepository.save(doctor);
    }

    @Transactional
    public void createAdministratorAccount(CreateAdministratorRequestDTO request){
        Person person;
        Optional<Person> optionalPerson = personRepository.findByNif(request.getNif());
        if (optionalPerson.isEmpty()){
            person = createNewPerson(request.getNif());
            createNewUser(request.getEmail(), request.getPasswordHash(), roleRepository.findByName(RoleName.ADMINISTRATOR), person);
        }
        else{
            person = personRepository.findByNif(request.getNif()).get();
            if (administratorRepository.findByPersonPersonID(person.getPersonID()).isPresent()){
                throw new RuntimeException("Admin Account Already Exists");
            }
            else{
                User user = userRepository.findByPerson(person).get();
                user.getRoles().add(roleRepository.findByName(RoleName.ADMINISTRATOR));
                userRepository.save(user);
            }
        }

        Administrator administrator = createAdministrator(request, person);
        administratorRepository.save(administrator);
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

    public void createNewUser(String email, String password, Role userRole, Person person){
        User user = new User();
        user.setUserEmail(email);
        String passwordHash = passwordEncoder.encode(password);
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
}