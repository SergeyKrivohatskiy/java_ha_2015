package ru.spbau.skrivohatskiy.java.hw1.writers;

import java.io.Closeable;

import ru.spbau.skrivohatskiy.java.hw1.Message;

public interface MessageWriter extends Closeable {
    void writeMessage(Message msg);
}
