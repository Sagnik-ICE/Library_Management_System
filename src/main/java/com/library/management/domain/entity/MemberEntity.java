package com.library.management.domain.entity;

import com.library.management.domain.enums.MemberStatus;
import com.library.management.domain.vo.Email;
import com.library.management.domain.vo.PhoneNumber;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Member Entity - Domain model representing a library member
 * 
 * @author Library Management System
 * @version 2.0
 */
public class MemberEntity {
    private Long id;
    private String uniqueMemberId;
    private String firstName;
    private String lastName;
    private String fatherName;
    private String motherName;
    private LocalDate dateOfBirth;
    private Email email;
    private PhoneNumber contactNumber;
    private String address;
    private String gender;
    private String profilePicturePath;
    private MemberStatus status;
    private LocalDate membershipDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String createdBy;
    private String updatedBy;

    private MemberEntity() {}

    // Business methods
    public boolean isActive() {
        return status == MemberStatus.ACTIVE;
    }

    public void activate() {
        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = MemberStatus.INACTIVE;
    }

    public void suspend() {
        this.status = MemberStatus.SUSPENDED;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getAge() {
        if (dateOfBirth == null) return 0;
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    public boolean canBorrowBooks() {
        return isActive();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUniqueMemberId() {
        return uniqueMemberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Email getEmail() {
        return email;
    }

    public PhoneNumber getContactNumber() {
        return contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUniqueMemberId(String uniqueMemberId) {
        this.uniqueMemberId = uniqueMemberId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setContactNumber(PhoneNumber contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public void setMembershipDate(LocalDate membershipDate) {
        this.membershipDate = membershipDate;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberEntity that = (MemberEntity) o;
        return Objects.equals(id, that.id) && 
               Objects.equals(uniqueMemberId, that.uniqueMemberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uniqueMemberId);
    }

    @Override
    public String toString() {
        return "MemberEntity{" +
                "id=" + id +
                ", uniqueMemberId='" + uniqueMemberId + '\'' +
                ", name='" + getFullName() + '\'' +
                ", status=" + status +
                '}';
    }

    // Builder
    public static class Builder {
        private final MemberEntity member = new MemberEntity();

        public Builder id(Long id) {
            member.id = id;
            return this;
        }

        public Builder uniqueMemberId(String uniqueMemberId) {
            member.uniqueMemberId = uniqueMemberId;
            return this;
        }

        public Builder firstName(String firstName) {
            member.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            member.lastName = lastName;
            return this;
        }

        public Builder fatherName(String fatherName) {
            member.fatherName = fatherName;
            return this;
        }

        public Builder motherName(String motherName) {
            member.motherName = motherName;
            return this;
        }

        public Builder dateOfBirth(LocalDate dateOfBirth) {
            member.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder email(Email email) {
            member.email = email;
            return this;
        }

        public Builder contactNumber(PhoneNumber contactNumber) {
            member.contactNumber = contactNumber;
            return this;
        }

        public Builder address(String address) {
            member.address = address;
            return this;
        }

        public Builder gender(String gender) {
            member.gender = gender;
            return this;
        }

        public Builder profilePicturePath(String profilePicturePath) {
            member.profilePicturePath = profilePicturePath;
            return this;
        }

        public Builder status(MemberStatus status) {
            member.status = status;
            return this;
        }

        public Builder membershipDate(LocalDate membershipDate) {
            member.membershipDate = membershipDate;
            return this;
        }

        public Builder createdAt(LocalDate createdAt) {
            member.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDate updatedAt) {
            member.updatedAt = updatedAt;
            return this;
        }

        public Builder createdBy(String createdBy) {
            member.createdBy = createdBy;
            return this;
        }

        public Builder updatedBy(String updatedBy) {
            member.updatedBy = updatedBy;
            return this;
        }

        public MemberEntity build() {
            if (member.uniqueMemberId == null || member.uniqueMemberId.trim().isEmpty()) {
                throw new IllegalStateException("Unique Member ID is required");
            }
            if (member.firstName == null || member.firstName.trim().isEmpty()) {
                throw new IllegalStateException("First name is required");
            }
            if (member.lastName == null || member.lastName.trim().isEmpty()) {
                throw new IllegalStateException("Last name is required");
            }
            if (member.email == null) {
                throw new IllegalStateException("Email is required");
            }
            if (member.status == null) {
                member.status = MemberStatus.ACTIVE;
            }
            return member;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
