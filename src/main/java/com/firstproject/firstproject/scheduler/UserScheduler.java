package com.firstproject.firstproject.scheduler;

import com.firstproject.firstproject.entity.Journal;
import com.firstproject.firstproject.entity.User;
import com.firstproject.firstproject.enums.Sentiment;
import com.firstproject.firstproject.model.SentimentData;
import com.firstproject.firstproject.repo.UserRepoImpl;
import com.firstproject.firstproject.service.EmailService;
import com.firstproject.firstproject.service.SentimentAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepoImpl userRepo;

    // No use-without any model
//    @Autowired
//    private SentimentAnalysis sentimentAnalysis;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Scheduled(cron = "0 0 9 ? * SUN")
    public void fetchUserAndSendMail() {

        // with proper kafka with fallback protection
//        List<User> users = userRepo.getUsersForSA();
//        for (User user : users) {
//            List<Journal> journalEntries = user.getJournalEntries();
//            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());
//            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
//            for (Sentiment sentiment : sentiments) {
//                if (sentiment != null)
//                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
//            }
//            Sentiment mostFrequentSentiment = null;
//            int maxCount = 0;
//            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
//                if (entry.getValue() > maxCount) {
//                    maxCount = entry.getValue();
//                    mostFrequentSentiment = entry.getKey();
//                }
//            }
//            if (mostFrequentSentiment != null) {
//                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days " + mostFrequentSentiment).build();
//                try{
//                    kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData);
//                }catch (Exception e){
//                    emailService.sendMail(sentimentData.getEmail(), "Sentiment for previous week", sentimentData.getSentiment());
//                }
//            }
            /* with not fallback protection if kafka doesn't work all will be fail
            * if (mostFrequentSentiment != null) {
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days " + mostFrequentSentiment).build();
                kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData);
            }*/
//        }

        // simple but with no kafka
        List<User> users = userRepo.getUsersForSA();
        for(User user: users) {
            List<Journal> journalEntries = user.getJournalEntries();

            // filtering sentiments of the user of last 7 days, and then retrieving all the sentiments and putting them into this list
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x->x.getSentiment()).collect(Collectors.toList());

            // counting every sentiment of the user
            Map<Sentiment, Integer> sentimentCount = new HashMap<>();
            for(Sentiment sentiment: sentiments) {
                if(sentiment != null)
                    sentimentCount.put(sentiment, sentimentCount.getOrDefault(sentiment, 0) + 1);
            }

            // seeing which sentiment is max or frequent
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for(Map.Entry<Sentiment, Integer> entry : sentimentCount.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if(mostFrequentSentiment != null) {
                emailService.sendMail(user.getEmail(), "Sentiment of last 7 days", mostFrequentSentiment.toString());
            }
        }


        // first simple logic
//        List<User> users = userRepo.getUsersForSA(); // fetching users who have emails added and agreed to receive an email
//
//        for(User user: users) {
//            List<Journal> journals = user.getJournalEntries();
//            // List<Journal> collect = journals.stream().filter(x-> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).collect(Collectors.toList());
//
//            //-> filtering journals which are saved by user in last 7 days
//            List<Journal> filteredJournals = journals.stream().filter(x-> x.getDate().isAfter(LocalDateTime.now().minusDays(7))).toList();
//
//            //-> simply getting the main content of the journals
//            // List<String> contentOfJournals = filteredJournals.stream().map(e->e.getContent()).toList();
//            List<String> contentOfJournals = filteredJournals.stream().map(Journal::getContent).toList();
//
//            //-> making one single string from all the content
//            String joinedContent = String.join(" ", contentOfJournals);
//
//            // getting the sentiment by analysing the content of the user
//            String sentiment = sentimentAnalysis.getSentiment(joinedContent);
//
//            emailService.sendMail(/*user.getEmail()*/ "bob12@gmail.com", "Your last 7 Days Journal sentiment", "You are adding so many journals we are so happy, \n" + sentiment /* or joined content <- which is users journals*/);
//        }



    }
}
