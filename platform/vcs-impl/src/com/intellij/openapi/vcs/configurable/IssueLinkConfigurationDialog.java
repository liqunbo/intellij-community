// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.openapi.vcs.configurable;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vcs.IssueNavigationLink;
import com.intellij.ui.DocumentAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yole
 */
public class IssueLinkConfigurationDialog extends DialogWrapper {
  private JPanel myPanel;
  private JTextField myIssueIDTextField;
  private JTextField myIssueLinkTextField;
  private JLabel myErrorLabel;
  private JTextField myExampleIssueIDTextField;
  private JTextField myExampleIssueLinkTextField;

  protected IssueLinkConfigurationDialog(Project project) {
    super(project, false);
    init();
    myIssueIDTextField.getDocument().addDocumentListener(new DocumentAdapter() {
      @Override
      protected void textChanged(@NotNull DocumentEvent e) {
        updateFeedback();
      }
    });
    myExampleIssueIDTextField.getDocument().addDocumentListener(new DocumentAdapter() {
      @Override
      protected void textChanged(@NotNull final DocumentEvent e) {
        updateFeedback();
      }
    });
  }

  private void updateFeedback() {
    myErrorLabel.setText(" ");
    try {
      Pattern p = Pattern.compile(myIssueIDTextField.getText());
      if (myIssueIDTextField.getText().length() > 0) {
        final Matcher matcher = p.matcher(myExampleIssueIDTextField.getText());
        if (matcher.matches()) {
          myExampleIssueLinkTextField.setText(matcher.replaceAll(myIssueLinkTextField.getText()));
        }
        else {
          myExampleIssueLinkTextField.setText("<no match>");
        }
      }
    }
    catch(Exception ex) {
      myErrorLabel.setText("Invalid regular expression: " + ex.getMessage());
      myExampleIssueLinkTextField.setText("");
    }
    setOKActionEnabled(myErrorLabel.getText().equals(" "));
  }

  @Override
  @Nullable
  protected JComponent createCenterPanel() {
    return myPanel;
  }

  @Nullable
  @Override
  protected String getHelpId() {
    return "reference.settings.vcs.issue.navigation.add.link";
  }

  public IssueNavigationLink getLink() {
    return new IssueNavigationLink(myIssueIDTextField.getText(), myIssueLinkTextField.getText());
  }

  public void setLink(final IssueNavigationLink link) {
    myIssueIDTextField.setText(link.getIssueRegexp());
    myIssueLinkTextField.setText(link.getLinkRegexp());
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return myIssueIDTextField;
  }
}