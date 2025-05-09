package com.firstproject.firstproject.scheduler;

import com.firstproject.firstproject.entity.Journal;
import com.firstproject.firstproject.entity.User;
import com.firstproject.firstproject.repo.UserRepoImpl;
import com.firstproject.firstproject.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepoImpl userRepo;

    public void fetchUserAndSendMail() {
        List<User> users = userRepo.getUsersForSA(); // fetching users who have emails added and agreed to receive an email

        for(User user: users) {
            List<Journal> journals = user.getJournalEntries();
            // List<Journal> collect = journals.stream().filter(x-> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).collect(Collectors.toList());

            //-> filtering journals which are saved by user in last 7 days
            List<Journal> filteredJournals = journals.stream().filter(x-> x.getDate().isAfter(LocalDateTime.now().minusDays(7))).toList();

            //-> simply getting the main content of the journals
            // List<String> contentOfJournals = filteredJournals.stream().map(e->e.getContent()).toList();
            List<String> contentOfJournals = filteredJournals.stream().map(Journal::getContent).toList();

            //-> making one single string from all the content
            String joinedContent = String.join(" ", contentOfJournals);

            emailService.sendMail(/*user.getEmail()*/ "bob12@gmail.com", "Your last 7 Days Journal", "You are adding so many journals we are so happy, \n" + joinedContent);
        }
    }
}
