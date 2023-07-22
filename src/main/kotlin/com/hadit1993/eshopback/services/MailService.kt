package com.hadit1993.eshopback.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

enum class EmailType {
    AccountActivation, ResetPassword
}

@Service
class MailService(private val mailSender: JavaMailSender, @Value("\${client.baseurl}") val clientBaseUrl: String) {

    fun sendActivationUrl(recipient: String, tokenId: String, emailType: EmailType) {

        val messageSubject = if (emailType == EmailType.AccountActivation) "Account Activation" else "Reset password"
        val action = if (emailType == EmailType.AccountActivation) "activate your account" else "reset your password"
        val urlPath = if (emailType == EmailType.AccountActivation) "activation" else "reset-password"
        val message = SimpleMailMessage().apply {
            setTo(recipient)
            subject = messageSubject
            text = "Please click the following link to $action: ${clientBaseUrl}/${urlPath}/$tokenId"
        }

        mailSender.send(message)
    }
}

