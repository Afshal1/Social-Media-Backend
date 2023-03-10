package com.example.social.repo.sm_repos;

import com.example.social.entities.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Tuple;
import java.util.List;

@Repository
public interface NotificationsRepo extends JpaRepository<Notifications,Integer> {
    @Query(value = "select nfc.id,nfc.notification,u.name as sender_name,u.email as sender_email,u.profile_pic as sender_profile_pic,us.name as receiver_name,us.email as receiver_email,us.profile_pic as receiver_profile_pic,nfc.notification_status from notifications nfc inner join user u on nfc.notification_sender = u.user_id inner join user us on nfc.notification_receiver = us.user_id where us.email=:email ",nativeQuery = true)
    List<Tuple> findNotificationsByNotificationReceiverEmail(@Param("email")String email);
    @Query(value ="select nfc.notification_status from notifications nfc inner join user u on nfc.notification_sender = u.user_id inner join user us on nfc.notification_receiver=us.user_id  where u.email=:byUser and us.email=:ofUser  " ,nativeQuery = true)
    Tuple findNotificationStatusOfUser(@Param("byUser")String byUser,@Param("ofUser")String ofUser);

    Notifications findByNotificationSenderEmailAndAndNotificationReceiverEmailAndNotificationStatus(String senderEmail,String receiverEmail,String notificationStatus);
}
