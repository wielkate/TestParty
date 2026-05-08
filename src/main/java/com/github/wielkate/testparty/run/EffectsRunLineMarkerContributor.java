package com.github.wielkate.testparty.run;

import com.intellij.execution.lineMarker.RunLineMarkerContributor;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiElement;

@SuppressWarnings("removal")
public class EffectsRunLineMarkerContributor extends RunLineMarkerContributor {

    @Override
    public Info getInfo(PsiElement element) {
        if (!"add".equals(element.getText())) {
            return null;
        }

        final var text = String.format("Run %s() with Effects", element.getText());
        AnAction effectsAction = new AnAction(
                text,
                "Run test with effects enabled",
                AllIcons.Actions.Execute
        ) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                System.out.println(text);
            }
        };

        return new Info(
                AllIcons.RunConfigurations.TestState.Run,
                psiElement -> "Run test with effects enabled",
                effectsAction
        );
    }
}