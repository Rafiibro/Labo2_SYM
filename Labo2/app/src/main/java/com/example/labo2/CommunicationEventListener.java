package com.example.labo2;

import java.util.EventListener;

/**
 * Example of the interface definition of an event listener
 */
public interface CommunicationEventListener extends EventListener {

    boolean handleServerResponse(String response);
}
