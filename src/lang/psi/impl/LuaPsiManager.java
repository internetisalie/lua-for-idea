///*
// * Copyright 2010 Jon S Akhtar (Sylvanaar)
// *
// *   Licensed under the Apache License, Version 2.0 (the "License");
// *   you may not use this file except in compliance with the License.
// *   You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// *   Unless required by applicable law or agreed to in writing, software
// *   distributed under the License is distributed on an "AS IS" BASIS,
// *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *   See the License for the specific language governing permissions and
// *   limitations under the License.
// */
//
//package com.sylvanaar.idea.Lua.lang.psi.impl;
//
//import com.intellij.ProjectTopics;
//import com.intellij.openapi.components.ServiceManager;
//import com.intellij.openapi.diagnostic.Logger;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.roots.ModuleRootEvent;
//import com.intellij.openapi.roots.ModuleRootListener;
//import com.intellij.openapi.util.Computable;
//import com.intellij.psi.*;
//import com.intellij.psi.impl.PsiManagerEx;
//import com.intellij.psi.search.GlobalSearchScope;
//import com.intellij.psi.search.PsiShortNamesCache;
//import com.intellij.psi.util.TypeConversionUtil;
//import com.intellij.util.ConcurrencyUtil;
//import com.intellij.util.Function;
//import com.intellij.util.IncorrectOperationException;
//import com.intellij.util.containers.ConcurrentWeakHashMap;
//import com.intellij.util.containers.ContainerUtil;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//
//import java.util.*;
//
///**
// * @author ven
// */
//public class LuaPsiManager {
//  private static final Logger LOG = Logger.getInstance("#Lua.LuaPsiManager");
//  private final Project myProject;
//
//  private Map<String, List<PsiMethod>> myDefaultMethods;
//
//  private static final String DEFAULT_METHODS_QNAME = "org.codehaus.Lua.runtime.DefaultLuaMethods";
//  private static final String DEFAULT_STATIC_METHODS_QNAME = "org.codehaus.Lua.runtime.DefaultLuaStaticMethods";
//  private static final String SWING_BUILDER_QNAME = "Lua.swing.SwingBuilder";
//
//  private GrTypeDefinition myArrayClass;
//
//  private final ConcurrentWeakHashMap<LuaPsiElement, PsiType> myCalculatedTypes = new ConcurrentWeakHashMap<LuaPsiElement, PsiType>();
//  private volatile boolean myRebuildGdkPending = true;
//  private final LuaShortNamesCache myCache;
//
//  private final TypeInferenceHelper myTypeInferenceHelper;
//
//  private static final String SYNTHETIC_CLASS_TEXT = "class __ARRAY__ { public int length }";
//
//  public LuaPsiManager(Project project) {
//    myProject = project;
//    myCache = ContainerUtil.findInstance(project.getExtensions(PsiShortNamesCache.EP_NAME), LuaShortNamesCache.class);
//
//    ((PsiManagerEx)PsiManager.getInstance(myProject)).registerRunnableToRunOnAnyChange(new Runnable() {
//      public void run() {
//        dropTypesCache();
//      }
//    });
//
//    myTypeInferenceHelper = new TypeInferenceHelper(myProject);
//
//    myProject.getMessageBus().connect().subscribe(ProjectTopics.PROJECT_ROOTS, new ModuleRootListener() {
//      public void beforeRootsChange(ModuleRootEvent event) {
//      }
//
//      public void rootsChanged(ModuleRootEvent event) {
//        dropTypesCache();
//        myRebuildGdkPending = true;
//      }
//    });
//  }
//
//  public TypeInferenceHelper getTypeInferenceHelper() {
//    return myTypeInferenceHelper;
//  }
//
//  public void dropTypesCache() {
//    myCalculatedTypes.clear();
//  }
//
//  @NotNull
//  private Map<String, List<PsiMethod>> buildGDK() {
//    final java.util.HashMap<String, List<PsiMethod>> newMap = new java.util.HashMap<String, List<PsiMethod>>();
//
//    PsiClass defaultMethodsClass =
//      JavaPsiFacade.getInstance(myProject).findClass(DEFAULT_METHODS_QNAME, GlobalSearchScope.allScope(myProject));
//    if (defaultMethodsClass != null) {
//      for (PsiMethod method : defaultMethodsClass.getMethods()) {
//        if (method.isConstructor()) continue;
//        addDefaultMethod(method, newMap, false);
//      }
//
//    }
//
//    PsiClass defaultStaticMethodsClass =
//      JavaPsiFacade.getInstance(myProject).findClass(DEFAULT_STATIC_METHODS_QNAME, GlobalSearchScope.allScope(myProject));
//    if (defaultStaticMethodsClass != null) {
//      for (PsiMethod method : defaultStaticMethodsClass.getMethods()) {
//        if (method.isConstructor()) continue;
//        addDefaultMethod(method, newMap, true);
//      }
//    }
//
//    addSwingBuilderMethods(newMap);
//    return newMap;
//  }
//
//  private static void addDefaultMethod(PsiMethod method, java.util.HashMap<String, List<PsiMethod>> map, boolean isStatic) {
//    if (!method.hasModifierProperty(PsiModifier.PUBLIC)) return;
//
//    PsiParameter[] parameters = method.getParameterList().getParameters();
//    LOG.assertTrue(parameters.length > 0);
//    PsiType thisType = TypeConversionUtil.erasure(parameters[0].getType());
//    String thisCanonicalText = thisType.getCanonicalText();
//    LOG.assertTrue(thisCanonicalText != null);
//    List<PsiMethod> hisMethods = map.get(thisCanonicalText);
//    if (hisMethods == null) {
//      hisMethods = new ArrayList<PsiMethod>();
//      map.put(thisCanonicalText, hisMethods);
//    }
//    hisMethods.add(new GrGdkMethodImpl(method, isStatic));
//  }
//
//  private static final String[] SWING_WIDGETS_METHODS =
//    {"action", "Lua.swing.impl.DefaultAction", "actions", "java.util.List", "map", "java.util.Map", "buttonGroup",
//      "javax.swing.ButtonGroup", "bind", "org.codehaus.Lua.binding.BindingUpdatable", "model",
//      "org.codehaus.Lua.binding.ModelBinding", "widget", "java.awt.Component", "container", "java.awt.Container", "bean",
//      "java.lang.Object", "dialog", "javax.swing.JDialog", "fileChooser", "javax.swing.JFileChooser", "frame", "javax.swing.JFrame",
//      "optionPane", "javax.swing.JOptionPane", "window", "javax.swing.JWindow", "button", "javax.swing.JButton", "checkBox",
//      "javax.swing.JCheckBox", "checkBoxMenuItem", "javax.swing.JCheckBoxMenuItem", "menuItem", "javax.swing.JMenuItem", "radioButton",
//      "javax.swing.JRadioButton", "radioButtonMenuItem", "javax.swing.JRadioButtonMenuItem", "toggleButton", "javax.swing.JToggleButton",
//
//      "editorPane", "javax.swing.JEditorPane", "label", "javax.swing.JLabel", "passwordField", "javax.swing.JPasswordField", "textArea",
//      "javax.swing.JTextArea", "textField", "javax.swing.JTextField", "textPane", "javax.swing.JTextPane",
//
//      "colorChooser", "javax.swing.JColorChooser", "comboBox", "javax.swing.JComboBox", "desktopPane", "javax.swing.JDesktopPane",
//      "formattedTextField", "javax.swing.JFormattedTextField", "internalFrame", "javax.swing.JInternalFrame", "layeredPane",
//      "javax.swing.JLayeredPane", "list", "javax.swing.JList", "menu", "javax.swing.JMenu", "menuBar", "javax.swing.JMenuBar", "panel",
//      "javax.swing.JPanel", "popupMenum", "javax.swing.JPopupMenu", "progressBar", "javax.swing.JProgressBar", "scrollBar",
//      "javax.swing.JScrollBar", "scrollPane", "javax.swing.JScrollPane", "separator", "javax.swing.JSeparator", "slider",
//      "javax.swing.JSlider", "spinner", "javax.swing.JSpinner", "splitPane", "javax.swing.JSplitPane", "tabbedPane",
//      "javax.swing.JTabbedPane", "table", "javax.swing.JTable", "tableColumn", "javax.swing.table.TableColumn", "toolbar",
//      "javax.swing.JToolbar", "tree", "javax.swing.JTree", "viewport", "javax.swing.JViewport", "boundedRangeModel",
//      "javax.swing.DefaultBoundedRangeModel", "spinnerDateModel", "javax.swing.SpinnerDateModel", "spinnerListModel",
//      "javax.swing.SpinnerListModel", "spinnerNumberModel", "javax.swing.SpinnerNumberModel", "tableModel", "javax.swing.table.TableModel",
//      "propertyColumn", "javax.swing.table.TableColumn", "closureColumn", "javax.swing.table.TableColumn", "borderLayout",
//      "java.awt.BorderLayout", "cardLayout", "java.awt.CardLayout", "flowLayout", "java.awt.FlowLayout", "gridBagLayout",
//      "java.awt.GridBagLayout", "gridLayout", "java.awt.GridLayout", "overlayLayout", "java.swing.OverlayLayout", "springLayout",
//      "java.swing.SpringLayout", "gridBagConstraints", "java.awt.GridBagConstraints", "gbc", "java.awt.GridBagConstraints", "boxLayout",
//      "javax.swing.BoxLayout", "box", "javax.swing.Box", "hbox", "javax.swing.Box", "hglue", "java.awt.Component", "hstrut",
//      "java.awt.Component", "vbox", "javax.swing.Box", "vglue", "java.awt.Component", "vstrut", "java.awt.Component", "glue",
//      "java.awt.Component", "rigidArea", "java.awt.Component", "tableLayout", "Lua.swing.impl.TableLayoutRow", "tr",
//      "Lua.swing.impl.TableLayoutRow", "td", "Lua.swing.impl.TableLayoutCell",};
//
//  private void addSwingBuilderMethods(Map<String, List<PsiMethod>> myDefaultMethods) {
//    PsiFileFactory factory = PsiFileFactory.getInstance(myProject);
//
//    StringBuilder classText = new StringBuilder();
//    classText.append("class SwingBuilder {\n");
//    for (int i = 0; i < SWING_WIDGETS_METHODS.length / 2; i++) {
//      String methodName = SWING_WIDGETS_METHODS[2 * i];
//      String returnTypeText = SWING_WIDGETS_METHODS[2 * i + 1];
//      classText.append("public ").append(returnTypeText).append(' ').append(methodName).append("() {} \n");
//    }
//
//    classText.append('}');
//
//    final PsiJavaFile file = (PsiJavaFile)factory.createFileFromText("Dummy.java", classText.toString());
//    final PsiClass clazz = file.getClasses()[0];
//
//    final PsiMethod[] methods = clazz.getMethods();
//    List<PsiMethod> result = new ArrayList<PsiMethod>(methods.length);
//    for (PsiMethod method : methods) {
//      method.putUserData(GrMethod.BUILDER_METHOD, true);
//      result.add(method);
//    }
//
//    myDefaultMethods.put(SWING_BUILDER_QNAME, result);
//  }
//
//  public List<PsiMethod> getDefaultMethods(String qName) {
//    if (myRebuildGdkPending) {
//      final Map<String, List<PsiMethod>> gdk = buildGDK();
//      if (myRebuildGdkPending) {
//        myDefaultMethods = gdk;
//        myRebuildGdkPending = false;
//      }
//    }
//
//    List<PsiMethod> methods = myDefaultMethods.get(qName);
//    if (methods == null) return Collections.emptyList();
//    return methods;
//  }
//
//  public List<PsiMethod> getDefaultMethods(PsiClass psiClass) {
//    List<PsiMethod> list = new ArrayList<PsiMethod>();
//    getDefaultMethodsInner(psiClass, new java.util.HashSet<PsiClass>(), list);
//    return list;
//  }
//
//  public void getDefaultMethodsInner(PsiClass psiClass, Set<PsiClass> watched, List<PsiMethod> methods) {
//    if (watched.contains(psiClass)) return;
//    watched.add(psiClass);
//    methods.addAll(getDefaultMethods(psiClass.getQualifiedName()));
//    for (PsiClass aClass : psiClass.getSupers()) {
//      getDefaultMethodsInner(aClass, watched, methods);
//    }
//  }
//
//  public static LuaPsiManager getInstance(Project project) {
//    return ServiceManager.getService(project, LuaPsiManager.class);
//  }
//
//  @Nullable
//  public <T extends LuaPsiElement> PsiType getType(T element, Function<T, PsiType> calculator) {
//    PsiType type = myCalculatedTypes.get(element);
//    if (type == null) {
//      type = calculator.fun(element);
//      if (type == null) {
//        type = PsiType.NULL;
//      }
//      type = ConcurrencyUtil.cacheOrGet(myCalculatedTypes, element, type);
//    }
//    if (!type.isValid()) {
//      LOG.error("Type is invalid: " + type + "; element: " + element + " of class " + element.getClass());
//    }
//    return PsiType.NULL.equals(type) ? null : type;
//  }
//
//  public GrTypeDefinition getArrayClass() {
//    if (myArrayClass == null) {
//      try {
//        myArrayClass = LuaPsiElementFactory.getInstance(myProject).createTypeDefinition(SYNTHETIC_CLASS_TEXT);
//      }
//      catch (IncorrectOperationException e) {
//        LOG.error(e);
//      }
//    }
//
//    return myArrayClass;
//  }
//
//  private static final ThreadLocal<List<PsiElement>> myElementsWithTypesBeingInferred = new ThreadLocal<List<PsiElement>>() {
//    protected List<PsiElement> initialValue() {
//      return new ArrayList<PsiElement>();
//    }
//  };
//
//  public static PsiType inferType(PsiElement element, Computable<PsiType> computable) {
//    final List<PsiElement> curr = myElementsWithTypesBeingInferred.get();
//    try {
//      curr.add(element);
//      return computable.compute();
//    }
//    finally {
//      curr.remove(element);
//    }
//  }
//
//  public static boolean isTypeBeingInferred(PsiElement element) {
//    return myElementsWithTypesBeingInferred.get().contains(element);
//  }
//
//  public LuaShortNamesCache getNamesCache() {
//    return myCache;
//  }
//}