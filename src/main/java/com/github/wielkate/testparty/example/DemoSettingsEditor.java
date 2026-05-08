package com.github.wielkate.testparty.example;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DemoSettingsEditor extends SettingsEditor<DemoRunConfiguration> {

    private final JPanel myPanel;
    private final TextFieldWithBrowseButton scriptPathField;

    public DemoSettingsEditor() {
        scriptPathField = new TextFieldWithBrowseButton();
        scriptPathField.addBrowseFolderListener(null,
                FileChooserDescriptorFactory.createSingleFileDescriptor().withTitle("Select Script File"));
        myPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent("Script file", scriptPathField)
                .getPanel();
    }

    @Override
    protected void resetEditorFrom(DemoRunConfiguration demoRunConfiguration) {
        scriptPathField.setText(demoRunConfiguration.getScriptName());
    }

    @Override
    protected void applyEditorTo(@NotNull DemoRunConfiguration demoRunConfiguration) {
        demoRunConfiguration.setScriptName(scriptPathField.getText());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myPanel;
    }

}