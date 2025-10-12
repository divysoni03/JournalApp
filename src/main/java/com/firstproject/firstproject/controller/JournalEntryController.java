
package com.firstproject.firstproject.controller;

import com.firstproject.firstproject.entity.Journal;
import com.firstproject.firstproject.entity.User;
import com.firstproject.firstproject.service.JournalEntryService;
import com.firstproject.firstproject.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
@Tag(name = "JournalEntry APIs")
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService; // when we define this the spring gives us an instance of this object that IOC created
    //so we don't have to create lots of new objects

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Journal> saveJournal(@RequestBody Journal journal) {
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userName = auth.getName();
            journalEntryService.saveEntry(journal, userName);
            return new ResponseEntity<>(journal, HttpStatus.CREATED);
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getJournals")
    public ResponseEntity< List<Journal> > getJournalsOfUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        // getting journalEntries of specific user
        User user = userService.findByUserName(userName);
        List<Journal> allJournals = user.getJournalEntries();
        if (allJournals != null && !allJournals.isEmpty()) {
            return new ResponseEntity<>(allJournals, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Journal> getJournalById(@PathVariable String myId) {
        ObjectId id = new ObjectId(myId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = userService.findByUserName(userName);
        List<Journal> collected = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).toList();
        if(!collected.isEmpty()) {
           Optional<Journal> journalEntry = journalEntryService.findById(id);
           if(journalEntry.isPresent()) {
               return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
           }
        }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteJournal(@PathVariable ObjectId id) {
        /* where ResponseEntity<?> means we don't have to define any specific entity to return in response, we can return any object*/
        try{
            return journalEntryService.deleteByid(id);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Journal> updateJournal(@PathVariable ObjectId id, @RequestBody Journal newJournal) { //this userName will be used for authentication in future
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        User user = userService.findByUserName(userName);
        List<Journal> collected = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).toList();

        if(!collected.isEmpty()) {
            Journal oldJournal = journalEntryService.findById(id).orElse(null);
            if(oldJournal != null){
                oldJournal.setTitle((newJournal.getTitle() != null && !newJournal.getTitle().equals("")) ? newJournal.getTitle() : oldJournal.getTitle());
                oldJournal.setContent((newJournal.getContent() != null && !newJournal.getTitle().equals("")) ? newJournal.getContent() : oldJournal.getContent());

                // Saving the journal after editing it
                journalEntryService.saveEntry(oldJournal);
                return new ResponseEntity<>(oldJournal, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
