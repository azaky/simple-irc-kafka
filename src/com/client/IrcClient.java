package com.client;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public class IrcClient {

    private String nickname;
    private String groupId;
    private final AtomicBoolean isTerminated = new AtomicBoolean(false);
    private final Map<String, MessageConsumer> messageConsumerMap = new HashMap<>();

    public void launch() throws IOException, TimeoutException {
        // Get initial username and generate groupId
        handleNick(null);
        groupId = RandomStringUtils.randomAlphanumeric(16);

        Thread inputHandler = getInputHandler();
        inputHandler.start();
        try {
            inputHandler.join();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private void terminate() {
        isTerminated.set(true);
    }

    private void showMessage(String message) {
        System.out.println(message);
    }

    private Thread getInputHandler() {
        final Scanner scanner = new Scanner(System.in);
        return new Thread() {
            @Override
            public void run() {
                while (!isTerminated.get()) {
                    String line = scanner.nextLine();
                    handleInput(line);
                }
            }
        };
    }

    private void handleInput(String input) {
        List<String> groups;

        try {
            if ((groups = CommandRegexes.NICK.match(input)) != null) {
                handleNick(groups.get(0));
            } else if ((groups = CommandRegexes.JOIN.match(input)) != null) {
                String channelName = groups.get(0);
                handleJoin(channelName);
            } else if ((groups = CommandRegexes.LEAVE.match(input)) != null) {
                String channelName = groups.get(0);
                handleLeave(channelName);
            } else if (CommandRegexes.EXIT.match(input) != null) {
                handleExit();
            } else if ((groups = CommandRegexes.MESSAGE_CHANNEL.match(input)) != null) {
                String channelName = groups.get(0);
                String message = groups.get(1);
                handleMessageChannel(message, channelName);
            } else { // Assuming broadcast
                handleBroadcast(input);
            }
        } catch (Exception e) {
            showMessage("Something bad happened");
            e.printStackTrace();
        }
    }

    private void handleBroadcast(String message) {
        for (String channel : messageConsumerMap.keySet()){
            handleMessageChannel(message, channel);
        }
    }

    private void handleMessageChannel(String message, String channelName) {
        if (!messageConsumerMap.containsKey(channelName)) {
            showMessage("You are not registered to " + channelName);
            return;
        }
        Producer<String,String> producer = new Producer<>(Configs.getProducerConfig());
        KeyedMessage<String, String> keyedMessage = new KeyedMessage<>(
                channelName,
                String.format("[%s] (%s) %s", channelName, nickname, message));
        producer.send(keyedMessage);
        producer.close();
    }

    private void handleLeave(String channelName) {
        if (!messageConsumerMap.containsKey(channelName)) {
            showMessage("You are not registered to " + channelName);
            return;
        }
        MessageConsumer consumer = messageConsumerMap.get(channelName);
        consumer.shutdown();
        messageConsumerMap.remove(channelName);
    }

    private void handleJoin(String channelName) {
        if (messageConsumerMap.containsKey(channelName)) {
            showMessage("You already joined " + channelName);
            return;
        }
        MessageConsumer consumer = new MessageConsumer(groupId, channelName);
        new Thread(consumer).start();
        messageConsumerMap.put(channelName, consumer);
    }

    private void handleExit() {
        deleteNickname();
        for (MessageConsumer consumer : messageConsumerMap.values()) {
            consumer.shutdown();
        }
        showMessage("Bye bye!");
        terminate();
    }

    private void handleNick(String requestedNickname) {
        if (requestedNickname == null) {
            requestedNickname = "";
        }
        deleteNickname();
        String returnedNickname = requestNickname(requestedNickname);
        if (requestedNickname.isEmpty()) {
            showMessage("You have been assigned as [" + returnedNickname + "]. Welcome!");
        } else if (!requestedNickname.equals(returnedNickname)) {
            showMessage("ERROR: nickname " + requestedNickname + " has already taken");
            showMessage("You have been assigned as [" + returnedNickname + "] instead. Welcome!");
        } else {
            showMessage("Welcome [" + returnedNickname + "]!");
        }
        nickname = returnedNickname;
    }

    private String requestNickname(String requestedNickname) {
        if (requestedNickname.isEmpty() || checkNicknameExist(requestedNickname)) {
            String generatedNickname = RandomStringUtils.randomAlphanumeric(5);
            registerNickname(generatedNickname);
            return generatedNickname;
        } else {
            registerNickname(requestedNickname);
            return requestedNickname;
        }
    }

    private void registerNickname(String nick) {
        // do nothing
    }

    private boolean checkNicknameExist(String nick) {
        return false;
    }

    private void deleteNickname() {
        // do nothing
    }

}
