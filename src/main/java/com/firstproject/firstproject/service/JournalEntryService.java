/*
-> MAIN THEORY ABOUT IOC CONTAINER AND BEANS AND HOW THEY WORK
* An IoC (Inversion of Control) container is a core component within a software framework that manages the lifecycle of objects (called "beans") by creating them, configuring their dependencies through Dependency Injection (DI), and controlling when they are initialized and destroyed, essentially taking over the responsibility of object creation and dependency management from the developer, leading to loosely coupled and more maintainable code.
Key points about IoC containers:
Dependency Injection:
The primary mechanism used by IoC containers to manage dependencies between objects; instead of an object creating its own dependencies, the IoC container injects them at runtime based on configuration.
Configuration Metadata:
IoC containers read configuration information from sources like XML files, annotations, or Java code to understand how to create and wire objects.
Benefits:
Loose Coupling: By injecting dependencies, objects become less dependent on specific implementations, making it easier to switch components without affecting other parts of the system.
Testability: Since dependencies are injected, it becomes easier to mock objects for unit testing.
Maintainability: Code becomes cleaner and easier to understand as the responsibility of object creation and dependency management is handled by the container.
How an IoC container works:
1. Configuration:
The developer defines the application components (beans) and their dependencies in the configuration metadata.
2. Initialization:
When the application starts, the IoC container reads the configuration and creates instances of the defined beans.
3. Dependency Injection:
When a bean needs another bean as a dependency, the container injects the required object by looking up its configuration.
4. Lifecycle Management:
The container can also manage the lifecycle of beans by performing actions like initialization and destruction based on defined lifecycle methods.
*
*
* Annotations used for the Spring Boot IoC container include:
@Component: Marks a class as a Spring-managed component.
@Service: Specialization of @Component for service layer classes.
@Repository: Specialization of @Component for data access layer classes.
@Controller: Specialization of @Component for controller classes in MVC applications.
@RestController: Combination of @Controller and @ResponseBody, indicating a controller that returns data directly in RESTful APIs.
@Configuration: Indicates a class that provides Spring configuration.
@Bean: Defines a bean within a @Configuration class, specifying how to create and manage an object.
@Autowired: Enables automatic dependency injection of beans.
@Primary: When multiple beans of the same type are available, @Primary indicates the preferred bean to inject.
@EnableCaching: Enables caching functionality.

-> when the spring boot program starts executing then the ioc checks every file, packege and if it finds any annotation
that says that is bean or object then ioc adds that object into it's container
* */

package com.firstproject.firstproject.service;

import com.firstproject.firstproject.entity.User;
import com.firstproject.firstproject.repo.JournalEntryRepo;
import com.firstproject.firstproject.entity.Journal;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//@Service //annotation just to identify this class as service class, or we can say we are giving synonym (which is a stereoType)

@Component // also used to identify this class as bean or object
public class JournalEntryService {
    /*THIS @Autowired thing is called dependency injection which is a part of IOC container */
    @Autowired
    JournalEntryRepo journalEntryRepo;

    @Autowired
    UserService userService;

    // logger from slf4j
//    private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    // treats whole function as a single operation and if something bad happens then we can roll back the whole process because if the data store in one table and not in other than the database will be inconsistent
    @Transactional
    public void saveEntry(Journal journal, String userName) {
        try{
            User user = userService.findByUserName(userName);
            journal.setDate(LocalDateTime.now()); // setting the date to now which refers to current time of localSystem and saves it in journal object
            Journal savedJournal = journalEntryRepo.save(journal); // saving the object data into db AND WILL RETURN IT AS WELL
            user.getJournalEntries().add(savedJournal);
            userService.saveUser(user);
        } catch (Exception e) {
             e.printStackTrace();
             throw new RuntimeException("Error saving journal entry");
            // log.error("exception", e);
        }
    }
    //Overloaded method used when editing the journal because we don't want to perform any changes in user collection and make useless calls to db
    public void saveEntry(Journal journal) {
        try{
            journal.setDate(LocalDateTime.now());
            journalEntryRepo.save(journal); //saving the journal without effecting user thing
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Journal> getJournals() {
        return journalEntryRepo.findAll();
    }

    public Optional<Journal> findById(ObjectId id) {
        // Optional<Journal> result = journalEntryRepo.findById(id);
        // if(result.isPresent()) {
        // return result.get();
        // }else { return null;}
        // -> single line expression of above
        // return result;
        return journalEntryRepo.findById(id);
    }

    @Transactional
    public ResponseEntity<?> deleteByid(ObjectId id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = userService.findByUserName(userName);
        List<Journal> collected = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).toList();
        /*
        Journal journal = new Journal();
        journal.setId(id);
        user.getJournalEntries().remove(journal);
        userService.saveUser(user);
        */
        if(!collected.isEmpty()) {
            user.getJournalEntries().removeIf(x -> x.getId().equals(id)); //searching for the element in array if we find same id then it will remove it
            journalEntryRepo.deleteById(id);
            userService.saveUser(user); //save edited user where we removed the reference key of journal
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        throw new RuntimeException("Journal not found for id " + id);
    }
}
