package client;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.*;
import java.io.UnsupportedEncodingException;

public class ChatClientGUI extends JFrame {
    private ChatClient client;

    private JTextArea chatArea;
    private JTextField messageField;

    public ChatClientGUI(ChatClient client) {
        this.client = client;

        

        setTitle("Chat client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        DefaultCaret caret = (DefaultCaret) chatArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                postMessage(messageField.getText());
                messageField.setText("");// Clear the text field after sending
            }
        });

        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    postMessage(messageField.getText());
                    messageField.setText("");  // Clear the text field after sending
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);
    }

    public void postMessage(String message) {
        client.sendMessage(message);
    }

    public void displayMessage(String message) {
        chatArea.append(message + "\n");
    }
}
