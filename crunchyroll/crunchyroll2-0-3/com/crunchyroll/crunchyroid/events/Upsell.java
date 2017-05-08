// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.events;

import android.widget.EditText;

public class Upsell
{
    public static class AccountCreatedEvent
    {
    }
    
    public static class AlreadyPremiumEvent
    {
        public final boolean isContinueToVideo;
        public final String positiveButton;
        
        public AlreadyPremiumEvent(final String positiveButton, final boolean isContinueToVideo) {
            this.positiveButton = positiveButton;
            this.isContinueToVideo = isContinueToVideo;
        }
    }
    
    public static class BackEvent
    {
    }
    
    public static class CancelEvent
    {
    }
    
    public static class CardAlreadyUsedEvent
    {
    }
    
    public static class CloseEvent
    {
    }
    
    public static class ContinueUpgradeEvent
    {
    }
    
    public static class CreateAccountEvent
    {
    }
    
    public static class ForgotPasswordEvent
    {
    }
    
    public static class LearnMoreEvent
    {
    }
    
    public static class LoggedInEvent
    {
    }
    
    public static class LoginEvent
    {
    }
    
    public static class MediaNotAvailableEvent
    {
        public final String message;
        
        public MediaNotAvailableEvent(final String message) {
            this.message = message;
        }
    }
    
    public static class NotQualifiedEvent
    {
    }
    
    public static class OkEvent
    {
    }
    
    public static class PaymentExceptionEvent
    {
        public final String message;
        
        public PaymentExceptionEvent(final String message) {
            this.message = message;
        }
    }
    
    public static class SignupExceptionEvent
    {
        public final String message;
        
        public SignupExceptionEvent(final String message) {
            this.message = message;
        }
    }
    
    public static class UpsellDismissedEvent
    {
    }
    
    public static class ValidationErrorEvent
    {
        public final EditText editText;
        public final String message;
        
        public ValidationErrorEvent(final String message, final EditText editText) {
            this.message = message;
            this.editText = editText;
        }
    }
}
