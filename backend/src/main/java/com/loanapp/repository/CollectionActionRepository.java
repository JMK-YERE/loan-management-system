package com.loanapp.repository;
import com.loanapp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CollectionActionRepository extends JpaRepository<CollectionAction,Long>{List<CollectionAction> findByCollectionCaseOrderByCreatedAtDesc(CollectionCase c);}
