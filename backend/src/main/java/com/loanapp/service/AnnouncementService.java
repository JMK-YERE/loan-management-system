package com.loanapp.service;

import com.loanapp.dto.AnnouncementRequest;
import com.loanapp.model.Announcement;
import com.loanapp.model.User;
import com.loanapp.repository.AnnouncementRepository;
import com.loanapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notifications;

    // Kwa wote (public)
    public List<Announcement> getPublicAnnouncements() {
        return announcementRepository.findByActiveTrueOrderByDisplayOrderAsc();
    }

    // Kwa Admin
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAllByOrderByDisplayOrderAsc();
    }

    public Announcement getById(Long id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tangazo halipatikani"));
    }

    public Announcement create(AnnouncementRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Mtumiaji hajapatikana"));

        Announcement a = Announcement.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .tag(request.getTag() != null ? request.getTag() : "Habari")
                .color(request.getColor() != null ? request.getColor() : "from-blue-500 to-indigo-600")
                .emoji(request.getEmoji() != null ? request.getEmoji() : "📢")
                .ctaText(request.getCtaText())
                .ctaLink(request.getCtaLink())
                .imageUrl(request.getImageUrl())
                .active(request.getActive() != null ? request.getActive() : true)
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .createdBy(user)
                .build();

        return announcementRepository.save(a);
    }

    public Announcement update(Long id, AnnouncementRequest request) {
        Announcement a = getById(id);

        if (request.getTitle() != null) a.setTitle(request.getTitle());
        if (request.getContent() != null) a.setContent(request.getContent());
        if (request.getTag() != null) a.setTag(request.getTag());
        if (request.getColor() != null) a.setColor(request.getColor());
        if (request.getEmoji() != null) a.setEmoji(request.getEmoji());
        if (request.getCtaText() != null) a.setCtaText(request.getCtaText());
        if (request.getCtaLink() != null) a.setCtaLink(request.getCtaLink());
        if (request.getImageUrl() != null) a.setImageUrl(request.getImageUrl());
        if (request.getActive() != null) a.setActive(request.getActive());
        if (request.getDisplayOrder() != null) a.setDisplayOrder(request.getDisplayOrder());

        return announcementRepository.save(a);
    }

    public int broadcastToBorrowers(Long id) {
        Announcement a = getById(id);
        int sent = 0;
        for (User borrower : userRepository.findByRole(User.Role.BORROWER)) {
            if (!Boolean.TRUE.equals(borrower.getActive()) || borrower.getStatus() != User.UserStatus.APPROVED) continue;
            String body = "JmkLoanApp: " + a.getTitle() + " - " + a.getContent();
            boolean email = notifications.sendEmail(borrower.getEmail(), "JmkLoanApp - " + a.getTitle(), "<p>Habari " + borrower.getFullName() + ",</p><p>" + a.getContent() + "</p>");
            boolean sms = notifications.sendSms(borrower.getPhone(), body);
            if (email || sms) sent++;
        }
        return sent;
    }

    public void delete(Long id) {
        announcementRepository.deleteById(id);
    }
}
