import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class lab {
    // Store token applications in a static list
    static List<TokenApplication> tokens = new ArrayList<>();

    // Class for token application
    static class TokenApplication {
        String name, testType, mobile, bloodGroup;
        TokenApplication(String n, String t, String m, String b) {
            name = n; testType = t; mobile = m; bloodGroup = b;
        }
        public String toString() {
            return "Name: " + name + ", Test: " + testType + ", Mobile: " + mobile + ", Blood Group: " + bloodGroup;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hospital Laboratory Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JButton staffButton = new JButton("Staff Login");
        JButton patientButton = new JButton("Patient Login");
        buttonPanel.add(staffButton); buttonPanel.add(patientButton);
        frame.add(buttonPanel, gbc);

        gbc.gridwidth = 1; gbc.gridy++;
        frame.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        JTextField userText = new JTextField(15);
        frame.add(userText, gbc);

        gbc.gridx = 0; gbc.gridy++;
        frame.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField passText = new JPasswordField(15);
        frame.add(passText, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        frame.add(loginButton, gbc);

        final String[] loginType = {""}; // <-- MAKE SURE IT'S AN ARRAY!

        staffButton.addActionListener(e -> {
            loginType[0] = "staff";
            JOptionPane.showMessageDialog(frame, "Staff Login selected.", "Info", JOptionPane.INFORMATION_MESSAGE);
            userText.setText(""); passText.setText("");
            setLimit(userText, 12); setLimit(passText, 10);
        });

        patientButton.addActionListener(e -> {
            loginType[0] = "patient";
            JOptionPane.showMessageDialog(frame, "Patient Login selected.", "Info", JOptionPane.INFORMATION_MESSAGE);
            userText.setText(""); passText.setText("");
            setLimit(userText, 6); setLimit(passText, 10);
        });

        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passText.getPassword());
            if ("staff".equals(loginType[0])) {
                if (!username.matches("[A-Za-z]{1,12}") && !(password.length() <= 10 && password.matches("^(?=.*[A-Za-z])(?=.*\\d).+$")))
                    showError(frame, "Invalid staff username and password."); 
                else if (!username.matches("[A-Za-z]{1,12}"))
                    showError(frame, "Staff username must be up to 12 letters only.");
                else if (!(password.length() <= 10 && password.matches("^(?=.*[A-Za-z])(?=.*\\d).+$")))
                    showError(frame, "Staff password must be 10 or fewer chars, containing both letters and numbers.");
                else {
                    showSuccess(frame, "Staff login successful!");
                    showStaffMenu(frame);
                }
            } else if ("patient".equals(loginType[0])) {
                if (!username.matches("\\d{6}") && !(password.length() <= 10 && password.matches("^(?=.*[A-Za-z])(?=.*\\d).+$")))
                    showError(frame, "Invalid patient username and password.");
                else if (!username.matches("\\d{6}"))
                    showError(frame, "Patient username must be 6 numbers only.");
                else if (!(password.length() <= 10 && password.matches("^(?=.*[A-Za-z])(?=.*\\d).+$")))
                    showError(frame, "Patient password must be 10 or fewer chars and contain both letters and numbers.");
                else {
                    showSuccess(frame, "Patient login successful!");
                    showTokenForm(frame);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select Staff or Patient Login first.", "Select Login Type", JOptionPane.WARNING_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    // Helper to set input length limit
    private static void setLimit(JTextField field, int max) {
        field.setDocument(new javax.swing.text.PlainDocument() {
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                if ((getLength() + str.length()) <= max) super.insertString(offs, str, a);
            }
        });
    }
    // Helper to show error dialog
    private static void showError(JFrame frame, String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
    // Helper to show success dialog
    private static void showSuccess(JFrame frame, String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    // Patient: Show token application form
    private static void showTokenForm(JFrame frame) {
        JDialog dialog = new JDialog(frame, "Apply for Lab Token", true);
        dialog.setLayout(new GridLayout(5,2,5,5));
        JTextField nameField = new JTextField();
        JComboBox<String> testTypeBox = new JComboBox<>(new String[]{"Blood Test","Other Lab Test"});
        JTextField mobileField = new JTextField();
        JComboBox<String> bloodGroupBox = new JComboBox<>(new String[]{"A+","B+","AB+","O+","A-","B-","AB-","O-"});
        JButton applyBtn = new JButton("Apply");
        dialog.add(new JLabel("Name:")); dialog.add(nameField);
        dialog.add(new JLabel("Test Type:")); dialog.add(testTypeBox);
        dialog.add(new JLabel("Mobile No:")); dialog.add(mobileField);
        dialog.add(new JLabel("Blood Group:")); dialog.add(bloodGroupBox);
        dialog.add(new JLabel("")); dialog.add(applyBtn);
        applyBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String testType = (String) testTypeBox.getSelectedItem();
            String mobile = mobileField.getText().trim();
            String bloodGroup = (String) bloodGroupBox.getSelectedItem();
            if (name.isEmpty() || mobile.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name and Mobile No are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            tokens.add(new TokenApplication(name, testType, mobile, bloodGroup));
            JOptionPane.showMessageDialog(dialog, "Token applied! Your lab request has been submitted.");
            dialog.dispose();
        });
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
    // Staff: Show tokens list in a separate window
    private static void showStaffMenu(JFrame frame) {
        JDialog dialog = new JDialog(frame, "Staff: View Applied Tokens", true);
        dialog.setLayout(new BorderLayout());
        JTextArea area = new JTextArea(10, 40);
        area.setEditable(false);
        String tokenList = tokens.isEmpty()
                ? "No tokens applied yet."
                : tokens.stream().map(TokenApplication::toString).collect(Collectors.joining("\n\n"));
        area.setText(tokenList);
        dialog.add(new JScrollPane(area), BorderLayout.CENTER);
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        dialog.add(closeBtn, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}
