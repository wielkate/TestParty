package com.github.wielkate.testparty.run;

import com.intellij.execution.lineMarker.ExecutorAction;
import com.intellij.execution.lineMarker.RunLineMarkerContributor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public final class MyRunLineMarkerContributor extends RunLineMarkerContributor {

    @Override
    public @NotNull Info getInfo(@NotNull PsiElement element) {

        AnAction action = ExecutorAction.getActionList().get(0);
        AnAction[] actions = new AnAction[]{action};
        return new Info(null, actions, psi -> "Run Test with Effects");
    }
}
