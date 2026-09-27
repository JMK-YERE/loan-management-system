package com.loanapp.repository;

import com.loanapp.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    List<Announcement> findByActiveTrueOrderByDisplayOrderAsc();

    List<Announcement> findAllByOrderByDisplayOrderAsc();
}
