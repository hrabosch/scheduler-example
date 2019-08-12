package org.hrabosch.example;

import org.hrabosch.example.model.Request;
import org.hrabosch.example.task.RequestTask;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

public class TestUtil {

    public static RequestTask generateRequestTask() {
                // Create task (simulate DB row) and create a RequestTask for Queue
                return new RequestTask(new Request(UUID.randomUUID(), generateString()));
    }

    public static String generateString() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return generatedString;
    }
}
