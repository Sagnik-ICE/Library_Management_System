package com.library.management.repository.interfaces;

import com.library.management.domain.entity.MemberEntity;
import com.library.management.domain.enums.MemberStatus;

import java.util.List;
import java.util.Optional;

/**
 * MemberRepository - Data access interface for Member operations
 * 
 * @author Library Management System
 * @version 2.0
 */
public interface MemberRepository {
    
    /**
     * Save a new member or update existing member
     * @param member Member entity to save
     * @return Saved member entity with generated ID
     */
    MemberEntity save(MemberEntity member);
    
    /**
     * Find member by database ID
     * @param id Database ID
     * @return Optional containing member if found
     */
    Optional<MemberEntity> findById(Long id);
    
    /**
     * Find member by unique member ID
     * @param uniqueMemberId Unique identifier
     * @return Optional containing member if found
     */
    Optional<MemberEntity> findByUniqueMemberId(String uniqueMemberId);
    
    /**
     * Find member by email
     * @param email Member email
     * @return Optional containing member if found
     */
    Optional<MemberEntity> findByEmail(String email);
    
    /**
     * Find all members
     * @return List of all members
     */
    List<MemberEntity> findAll();
    
    /**
     * Find members by name (partial match)
     * @param name Name to search
     * @return List of matching members
     */
    List<MemberEntity> findByName(String name);
    
    /**
     * Find members by status
     * @param status Member status
     * @return List of members with status
     */
    List<MemberEntity> findByStatus(MemberStatus status);
    
    /**
     * Find all active members
     * @return List of active members
     */
    List<MemberEntity> findActiveMembers();
    
    /**
     * Update member details
     * @param member Member entity with updated fields
     * @return Updated member entity
     */
    MemberEntity update(MemberEntity member);
    
    /**
     * Update member status
     * @param memberId Database ID
     * @param status New status
     * @return true if updated successfully
     */
    boolean updateStatus(Long memberId, MemberStatus status);
    
    /**
     * Delete member by ID
     * @param id Database ID
     * @return true if deleted successfully
     */
    boolean deleteById(Long id);
    
    /**
     * Check if member with unique ID exists
     * @param uniqueMemberId Unique member identifier
     * @return true if exists
     */
    boolean existsByUniqueMemberId(String uniqueMemberId);
    
    /**
     * Check if member with email exists
     * @param email Email address
     * @return true if exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if member can borrow books
     * @param memberId Database ID
     * @return true if member is active and can borrow
     */
    boolean canBorrowBooks(Long memberId);
    
    /**
     * Get total count of members
     * @return Total number of members
     */
    long count();
    
    /**
     * Get count of active members
     * @return Number of active members
     */
    long countActive();
}
