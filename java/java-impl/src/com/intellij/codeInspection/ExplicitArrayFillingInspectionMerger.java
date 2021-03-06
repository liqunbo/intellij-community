// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.codeInspection;

import com.intellij.codeInspection.ex.InspectionElementsMergerBase;
import com.intellij.lang.annotation.HighlightSeverity;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

public class ExplicitArrayFillingInspectionMerger extends InspectionElementsMergerBase {

  @NotNull
  @Override
  public String getMergedToolName() {
    return "ExplicitArrayFilling";
  }

  @NotNull
  @Override
  public String[] getSourceToolNames() {
    return new String[]{"Java8ArraySetAll"};
  }

  @Override
  protected boolean isEnabledByDefault(@NotNull String sourceToolName) {
    return false;
  }

  @Override
  protected Element transformElement(@NotNull String sourceToolName, @NotNull Element sourceElement, @NotNull Element toolElement) {
    if (HighlightSeverity.WARNING.getName().equals(sourceElement.getAttributeValue("level"))) {
      toolElement.addContent(new Element("option").setAttribute("name", "mySuggestSetAll").setAttribute("value", "true"));
    }
    return toolElement;
  }
}
